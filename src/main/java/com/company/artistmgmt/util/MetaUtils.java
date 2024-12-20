package com.company.artistmgmt.util;

import com.company.artistmgmt.model.BaseResponse;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

import static com.company.artistmgmt.repository.QueryConst.*;
import static com.company.artistmgmt.repository.QueryConst.PAGE_SIZE;

public class MetaUtils {
    public static <T> void extractedMeta(int pageNo, int pageSize, BaseResponse<List<T>> response, long totalCount) {
        response.setSuccess(true);
        response.setTimestamp(LocalDateTime.now());
        response.setStatusCode(HttpStatus.OK.value());
        response.addMeta(TOTAL_COUNT, totalCount);
        response.addMeta(TOTAL_PAGES, (int) Math.ceil((double) totalCount / pageSize));
        response.addMeta(CURRENT_PAGE, pageNo);
        response.addMeta(PAGE_SIZE, pageSize);
    }
}