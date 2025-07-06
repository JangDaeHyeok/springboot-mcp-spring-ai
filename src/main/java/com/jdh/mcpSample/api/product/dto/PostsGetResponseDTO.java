package com.jdh.mcpSample.api.product.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record PostsGetResponseDTO(
        String result,
        String msg,
        List<PostsDetailGetResponseDTO.Result> data
) {

    public record Result(Long id, String title, String contents, Integer viewCount, Integer category, String regDt, String modDt) {

    }

}
