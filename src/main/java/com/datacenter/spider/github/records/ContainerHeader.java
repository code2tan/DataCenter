package com.datacenter.spider.github.records;


/**
 * @param name         项目名称
 * @param fork         fork数
 * @param stars        star数
 * @param issues       issues 数
 * @param pullRequests 拉取请求数
 * @param discussions  讨论数
 */
public record ContainerHeader(String name, String fork, String stars, String issues, String pullRequests,
                              String discussions) {
}
