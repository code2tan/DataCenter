package com.datacenter.github.spider;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.datacenter.github.entity.GithubRepo;
import com.datacenter.github.entity.GithubRepoBaseInfo;
import com.datacenter.github.service.GithubRepoBaseInfoService;
import com.datacenter.github.service.GithubRepoService;
import com.datacenter.utils.ThreadPoolsUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Component
public class RepoBaseInfoSpider {

    @Resource
    GithubRepoService githubRepoService;
    @Resource
    GithubRepoBaseInfoService githubRepoBaseInfoService;

    private final ExecutorService pool;

    public RepoBaseInfoSpider() {
        pool = ThreadPoolsUtil.getOrInitExecutors("GITHUB_REPO_SPIDER", 8);
    }

    public void start() {
        LambdaQueryWrapper<GithubRepo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(GithubRepo::getIsUpdatedToday, 0);
        List<GithubRepo> githubRepoList = githubRepoService.list(lambdaQueryWrapper);

        // 获取已经存在的数据
        List<Integer> GithubRepoIdList = githubRepoList.stream().map(GithubRepo::getId).toList();
        LambdaQueryWrapper<GithubRepoBaseInfo> existDataQueryWrapper = new LambdaQueryWrapper<>();
        existDataQueryWrapper.in(GithubRepoBaseInfo::getGithubRepoId, GithubRepoIdList);
        List<GithubRepoBaseInfo> existData = githubRepoBaseInfoService.list(existDataQueryWrapper);
        Map<Integer, Integer> githubRepoId2GithubRepoBaseInfoId = Optional.ofNullable(existData)
                .orElseGet(ArrayList::new)
                .stream()
                .collect(Collectors.toMap(GithubRepoBaseInfo::getGithubRepoId, GithubRepoBaseInfo::getId,
                        (origin, current) -> current));
        // 数据爬取
        List<GithubRepoBaseInfo> resultList = new ArrayList<>();
        Collection<RepoBaseInfoSpiderTask> repoBaseInfoSpiderTaskList = Optional.of(githubRepoList)
                .orElseGet(ArrayList::new)
                .stream()
                .map(RepoBaseInfoSpiderTask::new)
                .toList();
        try {
            List<Future<GithubRepoBaseInfo>> futures = pool.invokeAll(repoBaseInfoSpiderTaskList);
            for (Future<GithubRepoBaseInfo> future : futures) {
                GithubRepoBaseInfo result = future.get(10, TimeUnit.SECONDS);
                result.setId(githubRepoId2GithubRepoBaseInfoId.getOrDefault(result.getGithubRepoId(), null));
                resultList.add(result);

            }
            githubRepoBaseInfoService.saveOrUpdateBatch(resultList);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
