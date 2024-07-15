package org.FelipeBert.TaskManager.dtos.AuthenticationDtos;

import lombok.Builder;

@Builder
public record TokenResponseDto(String token, String refreshToken) {
}
