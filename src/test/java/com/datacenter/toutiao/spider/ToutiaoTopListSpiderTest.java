package com.datacenter.toutiao.spider;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ToutiaoTopListSpiderTest {
    @Resource
    ToutiaoTopListSpider toutiaoTopListSpider;

    @Test
    public void dataParse() {
        toutiaoTopListSpider.dataParse();
    }
    @Test
    public void start() {
        toutiaoTopListSpider.start();
    }
}