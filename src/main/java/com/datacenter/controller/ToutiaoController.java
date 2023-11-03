package com.datacenter.controller;

import com.datacenter.toutiao.spider.ToutiaoTopListSpider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/toutiao")
public class ToutiaoController {
    final ToutiaoTopListSpider toutiaoTopListSpider;

    /**
     * todo 临时校验
     */
    private final String sugar = "code2tan";

    public ToutiaoController(ToutiaoTopListSpider toutiaoTopListSpider) {
        this.toutiaoTopListSpider = toutiaoTopListSpider;
    }

    @PostMapping("/top/list")
    public String flushTrendingByHand(@RequestParam String param) {
        if (Objects.equals(param, sugar)) {
            log.info(String.format("[%s] flush by hand ===> start ", param));
            toutiaoTopListSpider.start();
            log.info(String.format("[%s] flush by hand ===> end ", param));
        }
        return String.format("flushed by %s", param);
    }
}
