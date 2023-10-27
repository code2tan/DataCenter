package com.datacenter.github.spider;

import com.datacenter.github.entity.GithubRepo;
import com.datacenter.github.entity.GithubRepoBaseInfo;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;


@SpringBootTest
class RepoBaseInfoSpiderTest {
    @Resource
    RepoBaseInfoSpiderTask repoBaseInfoSpiderTask;

    @Test
    void getData() {
        String url = "https://github.com/apache/kafka";
        GithubRepo githubRepo = new GithubRepo(1, url, 1, new Date(), new Date(), 0);
        GithubRepoBaseInfo data = repoBaseInfoSpiderTask.dataParse(githubRepo);
        System.out.println(data);
    }
}