package io.nodom.sqs.demo;

import io.awspring.cloud.sqs.support.converter.*;
import org.springframework.util.Assert;
import software.amazon.awssdk.services.sqs.model.Message;

public class JAxbMessagingMessageConverter extends AbstractMessagingMessageConverter<Message> {

    @Override
    protected HeaderMapper<Message> createDefaultHeaderMapper() {
        return new SqsHeaderMapper();
    }

    @Override
    protected Object getPayloadToDeserialize(software.amazon.awssdk.services.sqs.model.Message message) {
        return message.body();
    }

    @Override
    public MessageConversionContext createMessageConversionContext() {
        return new SqsMessageConversionContext();
    }

    @Override
    protected software.amazon.awssdk.services.sqs.model.Message doConvertMessage(
            software.amazon.awssdk.services.sqs.model.Message messageWithHeaders, Object payload) {
        Assert.isInstanceOf(String.class, payload, "payload must be instance of String");
        return messageWithHeaders.toBuilder().body((String) payload).build();
    }
}