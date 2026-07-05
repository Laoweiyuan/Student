package org.example.jwt.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import org.example.jwt.entity.Student;
import org.example.jwt.mapper.StudentMapper;
import org.example.jwt.util.ExcelParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class StudentImportService {

    private static final Logger log = LoggerFactory.getLogger(StudentImportService.class);

    @Autowired
    private StudentMapper studentMapper;

    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(
            4, 8, 60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(200),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    /**
     * 入口：读取 Excel 并多线程分片插入
     */
    public void importFromExcel(InputStream inputStream) throws Exception {
        List<Student> students = ExcelParser.parse(inputStream);
        if (students.isEmpty()) {
            log.warn("Excel 中没有有效数据");
            return;
        }

        int total = students.size();
        int batchSize = 1000;
        int shards = (total + batchSize - 1) / batchSize;
        log.info("开始导入 {} 条学生数据，分 {} 片，每片 {} 条", total, shards, batchSize);

        CountDownLatch latch = new CountDownLatch(shards);
        AtomicInteger successTotal = new AtomicInteger(0);
        AtomicInteger failTotal = new AtomicInteger(0);

        for (int i = 0; i < shards; i++) {
            final int start = i * batchSize;
            final int end = Math.min(start + batchSize, total);
            final List<Student> batch = students.subList(start, end);

            executor.submit(() -> {
                try {
                    batchInsertWithRetry(batch);
                    successTotal.addAndGet(batch.size());
                } catch (Exception e) {
                    log.error("分片插入失败", e);
                    failTotal.addAndGet(batch.size());
                } finally {
                    latch.countDown();
                }
            });
        }

        boolean finished = latch.await(30, TimeUnit.MINUTES);
        if (finished) {
            log.info("导入完成：成功 {} 条，失败 {} 条", successTotal.get(), failTotal.get());
        } else {
            log.error("导入超时");
        }
    }

    /**
     * 带重试、独立事务的批量插入
     */
    @DS("Student")
    @DSTransactional
    private void batchInsertWithRetry(List<Student> batch) {
        int maxRetries = 3;
        int retryCount = 0;
        while (retryCount < maxRetries) {
            try {
                studentMapper.batchInsert(batch);
                return;
            } catch (DuplicateKeyException e) {
                log.warn("分片中存在重复学号，已跳过");
                return;
            } catch (Exception e) {
                retryCount++;
                if (retryCount >= maxRetries) {
                    log.error("分片插入失败，已重试 {} 次", maxRetries, e);
                    throw new RuntimeException("批量插入失败", e);
                }
                try {
                    Thread.sleep(100L * retryCount);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}