package com.opendevup.core.shared.model.pagination;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.function.Function;

@Getter
@Builder
public class PaginatedResult<T> {
    private List<T> content;
    private int totalPages;
    private long totalElements;
    private int pageNumber;
    private int pageSize;
    private boolean last;
    private boolean first;
    private boolean empty;

    public <R> PaginatedResult<R> map(Function<T, R> mapper) {
        List<R> mappedContent = content.stream()
                .map(mapper)
                .toList();

        return PaginatedResult.<R>builder()
                .content(mappedContent)
                .totalPages(this.totalPages)
                .totalElements(this.totalElements)
                .pageNumber(this.pageNumber)
                .pageSize(this.pageSize)
                .last(this.last)
                .first(this.first)
                .empty(this.empty)
                .build();
    }
}
