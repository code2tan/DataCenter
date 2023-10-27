package com.datacenter.github.spider;

import com.datacenter.Connecter;
import com.datacenter.github.service.GithubRepoService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author code2tan
 * Date: [2023/10/27 7:52]
 * Description:github trending
 */
@Slf4j
@Component
public class RepoTrendingSpider {
    @Resource
    Connecter connecter;
    @Resource
    GithubRepoService githubRepoService;

    public void start() {

    }
}
