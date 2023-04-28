package com.example.helloworld.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ExternalApiService {
    // 외부 서비스나 DB 호출하는 역할의 서비스

    public String getUserName(String userId) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Getting user name from other service...");

        if(userId.equals("A")) {
            return "Adam";
        }
        if(userId.equals("B")) {
            return "Bob";
        }

        return "";
    }

    // cacheManager로 캐싱
    @Cacheable(cacheNames = "userAgeCache", key = "#userId")
    public int getUserAge(String userId) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Getting user age from other service...");

        if(userId.equals("A")) {
            return 20;
        }
        if(userId.equals("B")) {
            return 32;
        }

        return 0;
    }
}
