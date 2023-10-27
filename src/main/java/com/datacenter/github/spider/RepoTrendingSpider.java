package com.datacenter.github.spider;

import com.datacenter.utils.Connector;
import com.datacenter.cache.GithubRepoCache;
import com.datacenter.github.entity.GithubRepo;
import com.datacenter.github.service.GithubRepoService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author code2tan
 * Date: [2023/10/27 7:52]
 * Description:github trending
 */
@Slf4j
@Component
public class RepoTrendingSpider {
    @Resource
    GithubRepoCache githubRepoCache;
    @Resource
    GithubRepoService githubRepoService;

    public void start() {

        String trendingRrl = "https://github.com/trending";
        Document document = null;
        try {
            document = Connector.getConnect(trendingRrl).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Element body = document.body();
        Elements repoList = body.select(".Box-row");

        List<GithubRepo> repoTrendingList = new ArrayList<>();
        for (Element element : repoList) {
            String suffix = element.getElementsByTag("h2").select("a").attr("href");
            String allUrl = "https://github.com" + suffix;
            if (githubRepoCache.cache.add(allUrl)) {
                GithubRepo githubRepo = new GithubRepo();
                githubRepo.setUrl(allUrl);
                repoTrendingList.add(githubRepo);
            }
        }
        log.info(String.format("获取trending数据 [%d] 条==========>", repoTrendingList.size()));
        if (CollectionUtils.isNotEmpty(repoTrendingList)) {
            githubRepoService.saveBatch(repoTrendingList);
        }
    }
}
