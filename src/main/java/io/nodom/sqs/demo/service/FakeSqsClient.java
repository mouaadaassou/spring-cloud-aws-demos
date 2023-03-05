package io.nodom.sqs.demo.service;


import com.google.common.collect.Lists;
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import io.nodom.sqs.demo.domain.UserWithMetaData;
import io.nodom.sqs.demo.domain.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.nodom.sqs.demo.domain.UserWithMetaData.getOriginalMessageList;


@Slf4j
@Component
@RequiredArgsConstructor
public class FakeSqsClient {

    @Value("${sqs.fake-queue-url}")
    private String queueUrl;

    private final SqsTemplate sqsTemplate;

    public void receiveMessage(int batchRequest) {
        Instant start = Instant.now();
        int numberOfIteration = (int) Math.ceil((double) batchRequest / 10);

        log.info("number of iterations: {}", numberOfIteration);
        List<UserWithMetaData> collect = IntStream.rangeClosed(1, numberOfIteration)
                .peek(iteration -> log.info("iteration number: {}", iteration))
                .mapToObj(item -> sqsTemplate.receiveMany(queueUrl, Users.class).stream()
                        .map(this::mapToDto).collect(Collectors.toList()))
                .takeWhile(items -> Objects.nonNull(items) && items.size() > 0)
                .flatMap(Collection::stream)
                .toList();


        log.info("received {} elements", collect.size());

        List<CompletableFuture<Void>> completableFutures = Lists.partition(getOriginalMessageList(collect), 10).stream()
                .map(Acknowledgement::acknowledgeAsync)
                .toList();

        CompletableFuture.allOf(completableFutures.toArray(item -> new CompletableFuture[0])).join();
        collect.forEach(item -> {
            log.info("user {} ", item.user());
            log.info("logging user: {}", ((software.amazon.awssdk.services.sqs.model.Message) item.originalMessage().getHeaders().get("Sqs_SourceData")).body());
        });
        log.info("SQS READ/DELETE within {} Millis", Duration.between(start, Instant.now()).toMillis());
    }

    private UserWithMetaData mapToDto(Message<Users> item) {
        return UserWithMetaData.builder()
                .user(item.getPayload())
                .originalMessage(item)
                .build();
    }
}
