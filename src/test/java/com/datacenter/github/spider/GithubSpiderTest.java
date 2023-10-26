package com.datacenter.github.spider;

import com.datacenter.github.entity.GithubRepo;
import com.datacenter.github.entity.GithubRepoBaseInfo;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Date;


@SpringBootTest
class GithubSpiderTest {
    @Resource
    GithubSpider githubSpider;

    @Test
    void start() {
        githubSpider.start();
    }

    @Test
    void getData() {
        String url = "https://github.com/Lymphatus/caesium-image-compressor";
        GithubRepo githubRepo = new GithubRepo(1, url, 1, new Date(), new Date(), 0);
        try {
            GithubRepoBaseInfo data = githubSpider.getData(githubRepo);
            System.out.println(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}