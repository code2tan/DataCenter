package com.datacenter.spider;

import com.datacenter.github.spider.GithubSpider;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class GithubSpiderTest {
    @Resource
    GithubSpider githubSpider;

    @Test
    void getData() {
        try {
            githubSpider.getData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}