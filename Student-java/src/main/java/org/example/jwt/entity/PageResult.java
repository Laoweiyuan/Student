package org.example.jwt.entity;

import java.util.List;

public class PageResult<T> {
    private List<T> data;
    private Integer nextCursor;   // 下一页游标
    private boolean hasMore;   // 是否还有下一页

    public PageResult(List<T> data, Integer nextCursor, boolean hasMore) {
        this.data = data;
        this.nextCursor = nextCursor;
        this.hasMore = hasMore;
    }
    public List<T> getData() { return data; }
    public Integer getNextCursor() { return nextCursor; }
    public boolean isHasMore() { return hasMore; }
    // getter / setter 省略
}