package com.datacenter.spider.github.records;

/**
 * @param about         简介
 * @param website       官网
 * @param version       最新版本
 * @param languagesJson 开发语言
 */
public record LayoutSidebar(String about, String website, String version, String languagesJson) {
}
