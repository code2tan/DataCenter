package com.datacenter.spider.github.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datacenter.spider.github.entity.GithubRepo;
import com.datacenter.spider.github.service.GithubRepoService;
import com.datacenter.spider.github.mapper.GithubRepoMapper;
import org.springframework.stereotype.Service;

/**
* @author angle
* @description 针对表【github_repo】的数据库操作Service实现
* @createDate 2023-10-27 04:03:59
*/
@Service
public class GithubRepoServiceImpl extends ServiceImpl<GithubRepoMapper, GithubRepo>
    implements GithubRepoService {

}




