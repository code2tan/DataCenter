package com.datacenter.cache;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.datacenter.github.entity.GithubRepo;
import com.datacenter.github.service.GithubRepoService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class GithubRepoCache implements InitializingBean {
    @Resource
    GithubRepoService githubRepoService;
    public Set<String> cache = new HashSet<>();

    @Override
    public void afterPropertiesSet() {
        IPage<GithubRepo> page = new Page<>(1, 50);
        loadData(page);
        for (long i = 2; i <= page.getPages(); i++) {
            page.setCurrent(i);
            loadData(page);
        }
        log.info("加载仓库数量：" + cache.size());
    }

    private void loadData(IPage<GithubRepo> page) {

        List<GithubRepo> list = githubRepoService.list(page);
        cache.addAll(Optional.ofNullable(list)
                .orElseGet(ArrayList::new)
                .stream()
                .map(GithubRepo::getUrl)
                .filter(url -> !cache.contains(url))
                .toList());
    }

    public boolean addData(List<String> data) {
        return cache.addAll(data);
    }

    public boolean addData(String data) {
        return cache.add(data);
    }
}
