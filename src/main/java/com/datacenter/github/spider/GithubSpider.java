package com.datacenter.github.spider;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.datacenter.Connecter;
import com.datacenter.github.entity.GithubRepo;
import com.datacenter.github.entity.GithubRepoBaseInfo;
import com.datacenter.github.records.ContainerHeader;
import com.datacenter.github.records.LayoutSidebar;
import com.datacenter.github.service.GithubRepoBaseInfoService;
import com.datacenter.github.service.GithubRepoService;
import com.google.gson.Gson;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Resource;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class GithubSpider {
    @Resource
    Connecter connecter;
    @Resource
    GithubRepoService githubRepoService;
    @Resource
    GithubRepoBaseInfoService githubRepoBaseInfoService;
    private static final Gson gson = new Gson();

    public void start() {
        LambdaQueryWrapper<GithubRepo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(GithubRepo::getIsUpdatedToday, 0);
        List<GithubRepo> list = githubRepoService.list(lambdaQueryWrapper);

        // 获取已经存在的数据
        List<Integer> GithubRepoIdList = list.stream().map(GithubRepo::getId).toList();
        LambdaQueryWrapper<GithubRepoBaseInfo> existDataQueryWrapper = new LambdaQueryWrapper<>();
        existDataQueryWrapper.in(GithubRepoBaseInfo::getGithubRepoId, GithubRepoIdList);
        List<GithubRepoBaseInfo> existData = githubRepoBaseInfoService.list(existDataQueryWrapper);
        Map<Integer, Integer> githubRepoId2GithubRepoBaseInfoId = Optional.ofNullable(existData)
                .orElseGet(ArrayList::new)
                .stream()
                .collect(Collectors.toMap(GithubRepoBaseInfo::getGithubRepoId, GithubRepoBaseInfo::getId,
                        (origin, current) -> current));
        // 数据爬取
        List<GithubRepoBaseInfo> dataList = new ArrayList<>();
        for (GithubRepo githubRepo : list) {
            try {
                GithubRepoBaseInfo data = getData(githubRepo);
                data.setId(githubRepoId2GithubRepoBaseInfoId.getOrDefault(data.getGithubRepoId(), null));
                dataList.add(data);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        githubRepoBaseInfoService.saveOrUpdateBatch(dataList);
    }

    /**
     * 获取数据
     *
     * @param githubRepo GithubRepo
     * @return GithubRepoBaseInfo
     *
     * @throws IOException e
     */
    public GithubRepoBaseInfo getData(GithubRepo githubRepo) throws IOException {
        Document doc = connecter.getConnect(githubRepo.getUrl()).get();
        if (doc.isBlock()) {
            return null;
        }
        Element body = doc.body();
        Elements applicationMain = body.select(".application-main");
        // 仓库容器头部数据
        Elements containerHeaderElements = applicationMain.select("#repository-container-header");
        ContainerHeader containerHeader = getContainerHeader(containerHeaderElements);
        // 侧边栏数据
        Elements layoutSidebarElements = applicationMain.select(".Layout-sidebar");
        LayoutSidebar layoutSidebar = getLayoutSidebar(layoutSidebarElements);

        return GithubRepoBaseInfo.builder()
                .githubRepoId(githubRepo.getId())
                .name(containerHeader.name())
                .fork(containerHeader.fork())
                .stars(containerHeader.stars())
                .issues(containerHeader.issues())
                .pullRequests(containerHeader.pullRequests())
                .discussions(containerHeader.discussions())
                .about(layoutSidebar.about())
                .website(layoutSidebar.website())
                .version(layoutSidebar.version())
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
        // 仓库名
        String name = Objects.requireNonNull(repositoryContainerHeader
                .attr("data-pjax", "#repo-content-pjax-container")
                .select("a").first()).text();
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
        return new ContainerHeader(name, Integer.parseInt(fork),
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
        String version = attr.select(".text-bold").get(1).text();
        List<String> languages = Optional.of(attr.select(".text-bold").select(".color-fg-default"))
                .orElseGet(Elements::new)
                .stream()
                .map(Element::text).toList();
        String languagesJson = gson.toJson(languages);
        return new LayoutSidebar(about, website, version, languagesJson);
    }

}
