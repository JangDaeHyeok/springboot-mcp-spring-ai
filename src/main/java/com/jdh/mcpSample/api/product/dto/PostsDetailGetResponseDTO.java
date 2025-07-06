package com.jdh.mcpSample.api.product.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record PostsDetailGetResponseDTO(
        String result,
        String msg,
        Result data
) {

    public record Result(Long id,
                                String title,
                                String contents,
                                Integer viewCount,
                                Integer category,
                                String regDt,
                                String modDt,
                                List<CommentGetResponseDTO> comments,
                                String empName,
                                String deptName,
                                String deptPathNm) {

    }

}
