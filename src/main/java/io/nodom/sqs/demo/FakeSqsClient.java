package io.nodom.sqs.demo;


import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.common.collect.Lists;
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.nodom.sqs.demo.UserWithMetaData.getOriginalMessageList;


@Slf4j
@Component
@RequiredArgsConstructor
public class FakeSqsClient {

    @Value("${sqs.fake-queue-url}")
    private String queueUrl;
    private JsonMapper jsonMapper = new JsonMapper();

    private final SqsTemplate sqsTemplate;

    public void receiveMessage(int batchRequest) {
        Instant start = Instant.now();
        int numberOfIteration = (int) Math.ceil((double) batchRequest / 10);

        log.info("number of iterations: {}", numberOfIteration);
        List<UserWithMetaData> collect = IntStream.rangeClosed(1, numberOfIteration)
                .mapToObj(item -> sqsTemplate.receiveMany(queueUrl, User.class).stream()
                        .map(this::mapToDto).collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .peek(item -> log.info("item: {}", item.user()))
                .toList();


        log.info("received {} elements", collect.size());

        List<CompletableFuture<Void>> completableFutures = Lists.partition(getOriginalMessageList(collect), 10).stream()
                .map(Acknowledgement::acknowledgeAsync)
                .toList();

        CompletableFuture.allOf(completableFutures.toArray(item -> new CompletableFuture[0])).join();
        log.info("SQS READ/DELETE within {} Millis", Duration.between(start, Instant.now()).toMillis());
    }

    private UserWithMetaData mapToDto(Message<User> item) {
        return UserWithMetaData.builder()
                .user(item.getPayload())
                .originalMessage(item)
                .build();
    }
}
