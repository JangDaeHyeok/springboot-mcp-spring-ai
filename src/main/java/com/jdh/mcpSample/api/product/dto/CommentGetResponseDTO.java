package com.jdh.mcpSample.api.product.dto;

import lombok.Builder;

@Builder
public record CommentGetResponseDTO(Long id, Long postId, String contents, String regDt, String modDt) {
}
