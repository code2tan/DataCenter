package com.datacenter.controller;

import com.datacenter.github.spider.RepoBaseInfoSpider;
import com.datacenter.github.spider.RepoTrendingSpider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/github")
public class GithubController {
    final RepoBaseInfoSpider repoBaseInfoSpider;

    final RepoTrendingSpider repoTrendingSpider;

    /**
     * todo 临时校验
     */
    private final String sugar = "code2tan";

    public GithubController(RepoBaseInfoSpider repoBaseInfoSpider, RepoTrendingSpider repoTrendingSpider) {
        this.repoBaseInfoSpider = repoBaseInfoSpider;
        this.repoTrendingSpider = repoTrendingSpider;
    }

    @PostMapping("/trending")
    public String flushTrendingByHand(@RequestParam String param) {
        if (Objects.equals(param, sugar)) {
            log.info(String.format("[%s] flush by hand ===> start ", param));
            repoTrendingSpider.start();
            log.info(String.format("[%s] flush by hand ===> end ", param));
        }
        return String.format("flushed by %s", param);
    }

    @GetMapping("/repo")
    public String flushRepoBaseInfoByHand(@RequestParam String param) {
        if (Objects.equals(param, sugar)) {
            log.info(String.format("[%s] flush repo base info by hand ===> start ", param));
            repoBaseInfoSpider.start();
            log.info(String.format("[%s] flush repo base info by hand ===> end ", param));
        }
        return String.format("flushed by %s", param);
    }
}
