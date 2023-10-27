package com.datacenter.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author code2tan
 * Date: [2023/10/28 1:34]
 * Description: Knife4jConfig
 */
@Configuration
@EnableKnife4j
// @Profile()
public class Knife4jConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/**")
                .build();
    }

}