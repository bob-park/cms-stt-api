package com.malgn.configure.transcode.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.core.io.Resource;

@ConfigurationProperties("transcode")
public record TranscodeProperties(@DefaultValue("file:///usr/bin") Resource lib) {
}
