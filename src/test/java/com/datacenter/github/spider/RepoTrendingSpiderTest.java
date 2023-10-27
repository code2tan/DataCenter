package com.datacenter.github.spider;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RepoTrendingSpiderTest {
    @Resource
    RepoTrendingSpider repoTrendingSpider;

    @Test
    void start() {
        repoTrendingSpider.start();
    }
}