package com.flicker.bff.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
public class BffUserService {

    private final Util util; // Util 클래스 의존성 주입

    @Value("${user-review.baseurl}")
    private String userReviewBaseUrl; // 외부 API의 기본 URL
}
