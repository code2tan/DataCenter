package com.datacenter.utils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;
@Component
public class Connector {
    private static final String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36";
    private static final int timeout = 5 * 1000;

    public static Connection getConnect(String url) {
        // Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
        return Jsoup.connect(url)
                // .proxy(proxy)
                .userAgent(userAgent)
                .timeout(timeout);
    }
}
