package com.opendevup.core.shared.model.pagination;

import lombok.Builder;

@Builder
public record PageableQuery(int pageNumber, int pageSize) {
    public static PageableQuery of(int pageNumber, int pageSize) {
        return new PageableQuery(pageNumber, pageSize);
    }
}
