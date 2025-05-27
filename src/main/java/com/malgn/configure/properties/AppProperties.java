package com.malgn.configure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@ConfigurationProperties("app")
public record AppProperties(Resource baseLocation) {
}
