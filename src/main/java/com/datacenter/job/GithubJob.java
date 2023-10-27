package com.datacenter.job;

import com.datacenter.github.spider.RepoBaseInfoSpider;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GithubJob {
    @Resource
    RepoBaseInfoSpider repoBaseInfoSpider;

    @Scheduled(cron = "0 0/10 * * * ?")
    public void spiderRun() {
        log.info("start github spider =====>");
        repoBaseInfoSpider.start();
        log.info("end github spider =====>");
    }
}
