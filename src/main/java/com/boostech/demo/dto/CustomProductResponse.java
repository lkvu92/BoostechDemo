package com.boostech.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * CustomProductResponse class for custom method get products advanced
 * @param <T>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomProductResponse<T> {
    private List<T> data;
    private Details details;

    @Data
    public static class Details {
        private long totalItems;
        private int currentPage;
        private int limit;
        private int totalPages;
        private String nextPageUrl;
        private String prevPageUrl;
        private String Status;
    }
}