package com.kanyuServer.utils;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.stereotype.Component;
 
@Component
public class RateLimitService {
    private final RateLimiter rateLimiter = RateLimiter.create(10); // 每秒10个令牌
 
    public boolean tryRequest() {
        return rateLimiter.tryAcquire(); // 尝试获取令牌，没有可用则返回false
    }
}