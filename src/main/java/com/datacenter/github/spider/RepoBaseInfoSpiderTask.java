package com.datacenter.github.spider;

import com.datacenter.utils.Connector;
import com.datacenter.github.entity.GithubRepo;
import com.datacenter.github.entity.GithubRepoBaseInfo;
import com.datacenter.github.records.ContainerHeader;
import com.datacenter.github.records.LayoutSidebar;
import com.google.gson.Gson;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Resource;
import lombok.Data;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

@Data
public class RepoBaseInfoSpiderTask implements Callable<GithubRepoBaseInfo> {
    private static final Gson gson = new Gson();
    @Resource
    Connector connector;

    private GithubRepo githubRepo;

    private GithubRepoBaseInfo result;

    public RepoBaseInfoSpiderTask(GithubRepo githubRepo) {
        this.githubRepo = githubRepo;
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     *
     * @throws Exception if unable to compute a result
     */
    @Override
    public GithubRepoBaseInfo call() throws Exception {
        return dataParse(githubRepo);
    }


    /**
     * 获取数据
     *
     * @param githubRepo GithubRepoCache
     * @return GithubRepoBaseInfo
     *
     * @throws IOException e
     */
    public GithubRepoBaseInfo dataParse(GithubRepo githubRepo) throws IOException {
        Document doc = connector.getConnect(githubRepo.getUrl()).get();
        if (doc.isBlock()) {
            return null;
        }
        Element body = doc.body();

        String name = body.selectXpath("//*[@id=\"repository-container-header\"]/div[1]/div[1]/div[1]/strong/a").text();
        Elements applicationMain = body.select(".application-main");
        // 仓库容器头部数据
        Elements containerHeaderElements = applicationMain.select("#repository-container-header");
        ContainerHeader containerHeader = getContainerHeader(containerHeaderElements);
        // 侧边栏数据
        Elements layoutSidebarElements = applicationMain.select(".Layout-sidebar");
        LayoutSidebar layoutSidebar = getLayoutSidebar(layoutSidebarElements);

        return GithubRepoBaseInfo.builder()
                .githubRepoId(githubRepo.getId())
                .name(name)
                .fork(containerHeader.fork())
                .stars(containerHeader.stars())
                .issues(containerHeader.issues())
                .pullRequests(containerHeader.pullRequests())
                .discussions(containerHeader.discussions())
                .about(layoutSidebar.about())
                .website(layoutSidebar.website())
                .languagesJson(layoutSidebar.languagesJson())
                .build();
    }

    /**
     * 获取仓库容器头统计信息
     *
     * @param repositoryContainerHeader Elements
     * @return ContainerHeader
     */
    private ContainerHeader getContainerHeader(@Nonnull Elements repositoryContainerHeader) {
        Elements counter = repositoryContainerHeader.select(".Counter");
        String fork = counter.select("#repo-network-counter").attr("title");
        String stars = counter.select("#repo-stars-counter-star").attr("title");
        String issues = counter.select("#issues-repo-tab-count").attr("title");
        String pullRequests = counter.select("#pull-requests-repo-tab-count").attr("title");
        // 话题讨论
        String discussions = counter.select("#discussions-repo-tab-count").attr("title");
        discussions = discussions.replace("Not available", "0");
        // 数据处理
        final String zeroStr = "0";
        fork = fork.isBlank() ? zeroStr : fork.replaceAll(",", "");
        stars = stars.isBlank() ? zeroStr : stars.replaceAll(",", "");
        issues = issues.isBlank() ? zeroStr : issues.replaceAll(",", "");
        pullRequests = pullRequests.isBlank() ? zeroStr : pullRequests.replaceAll(",", "");
        discussions = discussions.isBlank() ? zeroStr : discussions.replaceAll(",", "");
        return new ContainerHeader(Integer.parseInt(fork),
                Integer.parseInt(stars),
                Integer.parseInt(issues),
                Integer.parseInt(pullRequests),
                Integer.parseInt(discussions)
        );
    }


    /**
     * 获取侧边栏数据
     *
     * @param layoutSidebar Elements
     * @return LayoutSidebar
     */
    private LayoutSidebar getLayoutSidebar(Elements layoutSidebar) {
        String about = layoutSidebar.select("p").text();
        Elements attr = layoutSidebar.attr("class", "my-3 d-flex flex-items-center");
        String website = attr.select(".text-bold").get(0).attr("href");
        List<String> languages = Optional.of(attr.select(".text-bold").select(".color-fg-default"))
                .orElseGet(Elements::new)
                .stream()
                .map(Element::text).toList();
        String languagesJson = gson.toJson(languages);
        return new LayoutSidebar(about, website, languagesJson);
    }


}
