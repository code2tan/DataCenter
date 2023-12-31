package com.datacenter.github.records;


/**
 * @param name         项目名称
 * @param fork         fork数
 * @param stars        star数
 * @param issues       issues 数
 * @param pullRequests 拉取请求数
 * @param discussions  讨论数
 */
public record ContainerHeader(Integer fork, Integer stars, Integer issues, Integer pullRequests,
                              Integer discussions) {
}
