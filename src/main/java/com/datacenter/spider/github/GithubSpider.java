package com.datacenter.spider.github;

import com.datacenter.Connecter;
import com.datacenter.spider.github.records.ContainerHeader;
import com.datacenter.spider.github.records.LayoutSidebar;
import com.google.gson.Gson;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Resource;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class GithubSpider {
    @Resource
    Connecter connecter;
    private static final Gson gson = new Gson();

    public void getData() throws IOException {
        String url = "https://github.com/Lymphatus/caesium-image-compressor";
        Document doc = connecter.getConnect(url).get();
        Element body = doc.body();
        Elements applicationMain = body.select(".application-main");
        // 仓库容器头部数据
        Elements containerHeaderElements = applicationMain.select("#repository-container-header");
        ContainerHeader containerHeader = getContainerHeader(containerHeaderElements);
        // 侧边栏数据
        Elements layoutSidebarElements = applicationMain.select(".Layout-sidebar");
        LayoutSidebar layoutSidebar = getLayoutSidebar(layoutSidebarElements);
        System.out.println(containerHeader);
        System.out.println(layoutSidebar);
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
        return new ContainerHeader(name, fork, stars, issues, pullRequests, discussions);
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
