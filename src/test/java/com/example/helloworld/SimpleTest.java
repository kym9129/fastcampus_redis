package com.example.helloworld;

import com.example.helloworld.service.RankingService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Disabled // 빌드할때 테스트가 돌아서 일단 비활성화 처리
@SpringBootTest
public class SimpleTest {

    @Autowired
    private RankingService rankingService;

    @Test
    void getRanks() {
        rankingService.getTopRank(1); // 최초연결 시 시간 더 걸리기 때문에 테스트 전 한번 호출해두기.

        // 1)
        Instant before = Instant.now();
        Long userRank = rankingService.getUserRanking("user_100");
        Duration elapsed = Duration.between(before, Instant.now());

        System.out.println(String.format("Rank(%d) - Took %d ms", userRank, elapsed.getNano() / 1000000));
        // 실행결과 : Rank(906547) - Took 3 ms

        // 2)
        before = Instant.now();
        List<String> topRankers = rankingService.getTopRank(10);
        elapsed = Duration.between(before, Instant.now());

        System.out.println(String.format("Rank - Took %d ms", elapsed.getNano() / 1000000));
        // 실행결과 : Rank - Took 2 ms
        // sorted set을 이용하여 매우 빠름
        // rank보다 range가 더 빠른 이유 : set의 head나 tail에서 가져오므로

    }

    @Test
    void insertScore() {
        for(int i=0; i<1000000; i++) {
            int score = (int)(Math.random() * 1000000); // 0 ~ 999999
            String userId = "user_" + i;

            rankingService.setUserScore(userId, score);
        }
    }

    // 레디스 미사용 시 성능 측정
    @Test
    void inMemorySortPerformance() {
        List<Integer> list = new ArrayList<>();
        for(int i=0; i<1000000; i++) {
            int score = (int)(Math.random() * 1000000); // 0 ~ 999999
            list.add(score);
        }
        Instant before = Instant.now();
        Collections.sort(list); // nlogn
        Duration elapsed = Duration.between(before, Instant.now());
        System.out.println((elapsed.getNano() / 1000000) + " ms"); // 424 ms
    }
}
