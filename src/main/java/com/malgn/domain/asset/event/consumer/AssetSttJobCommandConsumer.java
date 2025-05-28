package com.malgn.domain.asset.event.consumer;

import java.util.Arrays;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.malgn.cqrs.consumer.CommandConsumer;
import com.malgn.cqrs.event.Event;
import com.malgn.cqrs.event.EventPayload;
import com.malgn.cqrs.event.EventType;
import com.malgn.cqrs.event.handler.DelegatingCommandHandler;
import com.malgn.domain.asset.event.AssetSttJobEventType;
import com.malgn.domain.asset.event.AssetSttJobEventType.Topic;

@Slf4j
@RequiredArgsConstructor
@Component
public class AssetSttJobCommandConsumer implements CommandConsumer {

    private static final List<EventType> SUPPORTED_EVENT_TYPES = Arrays.asList(AssetSttJobEventType.values());

    private final DelegatingCommandHandler handler;

    @Transactional
    @KafkaListener(topics = Topic.ASSET_STT_JOB)
    @Override
    public void listen(String message, Acknowledgment ack) {

        log.debug("received message - {}", message);

        Event<EventPayload> event = Event.fromJson(message, SUPPORTED_EVENT_TYPES);

        if (event == null) {
            return;
        }

        handler.handle(event);

        ack.acknowledge();

    }

}
