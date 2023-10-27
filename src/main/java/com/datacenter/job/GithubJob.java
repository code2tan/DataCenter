package com.datacenter.job;

import com.datacenter.github.spider.RepoBaseInfoSpider;
import com.datacenter.github.spider.RepoTrendingSpider;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GithubJob {
    @Resource
    RepoBaseInfoSpider repoBaseInfoSpider;
    @Resource
    RepoTrendingSpider repoTrendingSpider;

    @Scheduled(cron = "0 0 0/12 * * ?")
    public void repoSpiderRun() {
        log.info("start github spider =====>");
        repoBaseInfoSpider.start();
        log.info("end github spider =====>");
    }

    @Scheduled(cron = "0 0 0 0/1 * ?")
    public void trendingSpiderRun() {
        log.info("start trending github spider =====>");
        repoTrendingSpider.start();
        log.info("end trending github spider =====>");
    }
}
