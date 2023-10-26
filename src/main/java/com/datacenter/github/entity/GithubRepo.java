package com.datacenter.github.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author code2tan
 * Date: [2023/10/27 4:24]
 * Description: github_repo table
 */
@TableName(value = "github_repo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GithubRepo implements Serializable {
    @TableId(type = IdType.ASSIGN_ID)
    private Integer id;

    /**
     * github url
     */
    private String url;

    /**
     * 今天是否更新数据(0:否,1：是)
     */
    private Integer isUpdatedToday;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}