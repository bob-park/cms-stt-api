package com.malgn.configure;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.malgn.configure.properties.AppProperties;

@RequiredArgsConstructor
@EnableScheduling
@EnableConfigurationProperties(AppProperties.class)
@Configuration
public class AppConfiguration {
}
