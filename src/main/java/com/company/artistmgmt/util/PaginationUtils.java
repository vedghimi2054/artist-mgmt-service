package com.company.artistmgmt.util;

public class PaginationUtils {
    private static final int DEFAULT_PAGE_NO = 0;
    private static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * Validates and sets default values for pagination parameters.
     *
     * @param pageNo the page number provided in the request
     * @param pageSize the page size provided in the request
     * @return an array containing validated pageNo and pageSize
     * @throws IllegalArgumentException if the provided values are invalid
     */
    public static int[] validateAndSetDefaults(Integer pageNo, Integer pageSize) {
        // Set defaults if null or invalid
        if (pageNo == null || pageNo < 0) {
            pageNo = DEFAULT_PAGE_NO;
        }

        if (pageSize == null || pageSize <= 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        return new int[]{pageNo, pageSize};
    }
}
