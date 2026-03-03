package kr.tour.global.dto;

import org.springframework.http.HttpMethod;

public record HttpRequestInfo(HttpMethod method, String urlPattern) {
}