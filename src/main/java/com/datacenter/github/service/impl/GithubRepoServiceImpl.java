package com.datacenter.github.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datacenter.github.entity.GithubRepo;
import com.datacenter.github.mapper.GithubRepoMapper;
import com.datacenter.github.service.GithubRepoService;
import org.springframework.stereotype.Service;

/**
 * @author code2tan
 * Date: [2023/10/27 4:23]
 * Description: Github 仓库配置
 */
@Service
public class GithubRepoServiceImpl extends ServiceImpl<GithubRepoMapper, GithubRepo>
    implements GithubRepoService {

}




