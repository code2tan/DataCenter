package com.datacenter.toutiao.spider;

import com.datacenter.toutiao.domain.DataProject;
import com.datacenter.toutiao.domain.Result;
import com.datacenter.toutiao.entity.ToutiaoHat;
import com.datacenter.toutiao.service.ToutiaoHatService;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author code2tan
 * Date: [2023/10/27 23:58]
 * Description: 头条热榜50
 */
@Slf4j
@Component
public class ToutiaoTopListSpider {
    private final static String url = "https://www.toutiao.com/hot-event/hot-board/?origin=toutiao_pc";
    private final OkHttpClient client = new OkHttpClient();
    Gson gson = new Gson();
    @Resource
    ToutiaoHatService toutiaoHatService;

    public void start() {
        List<DataProject> dataProjects = dataParse();
        List<ToutiaoHat> saveDataList = Optional.ofNullable(dataProjects)
                .orElseGet(ArrayList::new)
                .stream()
                .map(dataProject -> ToutiaoHat.builder()
                        .title(dataProject.title)
                        .url(dataProject.url)
                        .hotValue(Long.valueOf(dataProject.hotValue))
                        .queryWord(dataProject.queryWord)
                        .day(new Date())
                        .build())
                .toList();
        if (CollectionUtils.isNotEmpty(saveDataList)) {
            toutiaoHatService.saveBatch(saveDataList);
        }
    }

    public List<DataProject> dataParse() {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String jsonStr = response.body().string();
            return gson.fromJson(jsonStr, Result.class).data;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
