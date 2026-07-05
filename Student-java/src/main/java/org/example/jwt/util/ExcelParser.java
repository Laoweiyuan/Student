package org.example.jwt.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.jwt.entity.Student;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelParser {

    public static List<Student> parse(InputStream inputStream) throws Exception {
        List<Student> students = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String studentNo = getCellValue(row.getCell(0));
                String name = getCellValue(row.getCell(1));
                String sexStr = getCellValue(row.getCell(2));
                String ageStr = getCellValue(row.getCell(3));

                if (studentNo.isEmpty() || name.isEmpty()) continue;

                Student s = new Student();
                s.setStudentNo(studentNo);
                s.setName(name);
                s.setSex("男".equals(sexStr) ? 1 : 0);
                s.setAge(Integer.parseInt(ageStr));
                students.add(s);
            }
        }
        return students;
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue().trim();
            case NUMERIC:
                double v = cell.getNumericCellValue();
                if (v == Math.floor(v) && !Double.isInfinite(v)) return String.valueOf((long) v);
                return String.valueOf(v);
            default: return "";
        }
    }
}