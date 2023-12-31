package com.datacenter.github.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author code2tan
 * Date: [2023/10/27 4:24]
 * Description: github_repo_base_info table
 */
@TableName(value ="github_repo_base_info")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GithubRepoBaseInfo implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * github repo id
     */
    @TableField
    private Integer githubRepoId;

    /**
     * 项目名
     */
    private String name;

    /**
     * fork数
     */
    private Integer fork;

    /**
     * star数
     */
    private Integer stars;

    /**
     * issues数
     */
    private Integer issues;

    /**
     * 拉取请求数
     */
    private Integer pullRequests;

    /**
     * 讨论话题数
     */
    private Integer discussions;

    /**
     * 简介
     */
    private String about;

    /**
     * 官网
     */
    private String website;

    /**
     * 开发语言
     */
    private String languagesJson;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private Integer isDelete;
}