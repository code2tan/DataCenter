package com.datacenter.spider.github.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datacenter.spider.github.entity.GithubRepoBaseInfo;
import com.datacenter.spider.github.service.GithubRepoBaseInfoService;
import com.datacenter.spider.github.mapper.GithubRepoBaseInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author angle
* @description 针对表【github_repo_base_info】的数据库操作Service实现
* @createDate 2023-10-27 04:07:26
*/
@Service
public class GithubRepoBaseInfoServiceImpl extends ServiceImpl<GithubRepoBaseInfoMapper, GithubRepoBaseInfo>
    implements GithubRepoBaseInfoService{

}




