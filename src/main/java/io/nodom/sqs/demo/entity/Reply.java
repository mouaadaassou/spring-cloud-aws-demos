package io.nodom.sqs.demo.entity;

import lombok.Setter;
import lombok.ToString;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;


@Setter
@ToString
@DynamoDbBean
public class Reply {

    private String id;
    private String postedBy;
    private String message;
    private String replyDatetime;

    @DynamoDbPartitionKey
    @DynamoDbAttribute(value = "Id")
    public String getId() {
        return id;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute(value = "ReplyDateTime")
    public String getReplyDatetime() {
        return this.replyDatetime;
    }


    @DynamoDbAttribute(value = "PostedBy")
    public String getPostedBy() {
        return postedBy;
    }


    @DynamoDbAttribute(value = "Message")
    public String getMessage() {
        return message;
    }
}
