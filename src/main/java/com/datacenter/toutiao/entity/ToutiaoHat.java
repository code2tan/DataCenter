package com.datacenter.toutiao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName toutiao_hat
 */
@TableName(value = "toutiao_hat")
@Data
@Builder
public class ToutiaoHat implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String title;

    private String url;

    private Long hotValue;

    private String queryWord;

    private Date day;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}