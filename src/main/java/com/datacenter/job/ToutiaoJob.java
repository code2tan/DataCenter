package com.datacenter.job;

import com.datacenter.toutiao.spider.ToutiaoTopListSpider;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ToutiaoJob {

    @Resource
    ToutiaoTopListSpider toutiaoTopListSpider;

    /**
     * 头条热榜
     */
    @Scheduled(cron = "0 0 21 * * ? ")
    public void toutiaoHatSpider() {
        log.info("start 头条[热榜]  spider =====>");
        toutiaoTopListSpider.start();
        log.info("end 头条[热榜] spider =====>");
    }

}
