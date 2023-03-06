package io.nodom.sqs.demo.service;


import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import io.nodom.sqs.demo.entity.Reply;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;


@Slf4j
@Service
@RequiredArgsConstructor
public class ReplyService {
    private final DynamoDbTemplate dynamoDbTemplate;

    public void queryReplyWithKey(String partitionValue) {
        PageIterable<Reply> query = dynamoDbTemplate.query(QueryEnhancedRequest.builder()
                        .queryConditional(QueryConditional.keyEqualTo(Key.builder()
                                .partitionValue(partitionValue)
                                .build()))
                        .build(),
                Reply.class);

        query.items().stream()
                .forEach(item -> log.info("item: {}", item));
    }
}
