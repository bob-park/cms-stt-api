package com.malgn.domain.audio.parser;

import static com.google.common.base.Preconditions.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;

import com.google.common.collect.Lists;

import com.malgn.time.TimeCode;

@Slf4j
public abstract class SrtFormatParser {

    private static final String GROUP_NAME_INDEX = "index";
    private static final String GROUP_NAME_START_TIME = "startTime";
    private static final String GROUP_NAME_END_TIME = "endTime";
    private static final String GROUP_NAME_TEXT = "text";

    private static final Pattern PATTERN =
        Pattern.compile(
            "(?<index>\\d+\\n)(?<startTime>\\d{2}:\\d{2}:\\d{2},\\d{3}) --> (?<endTime>\\d{2}:\\d{2}:\\d{2},\\d{3})\\n(?<text>.+)");

    public static List<TimeLineText> parse(String contents) {

        List<TimeLineText> texts = Lists.newArrayList();

        Matcher matcher = PATTERN.matcher(contents);

        while (matcher.find()) {
            String index = matcher.group(GROUP_NAME_INDEX);
            String startTime = matcher.group(GROUP_NAME_START_TIME);
            String endTime = matcher.group(GROUP_NAME_END_TIME);
            String text = matcher.group(GROUP_NAME_TEXT);

            log.debug("index: {}, startTime: {}, endTime: {}, text: {}", index, startTime, endTime, text);

            TimeCode startTimeCode = new TimeCode(startTime);
            TimeCode endTimeCode = new TimeCode(endTime);

            TimeLineText timeLineText =
                new TimeLineText(
                    startTimeCode.toSeconds(),
                    endTimeCode.toSeconds(),
                    text.trim());

            texts.add(timeLineText);
        }

        return texts;

    }

}
