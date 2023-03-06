package io.nodom.sqs.demo.service;

import io.awspring.cloud.dynamodb.DynamoDbTableNameResolver;
import org.springframework.util.Assert;


public class CustomDynamoDbTableNameResolver implements DynamoDbTableNameResolver {

    @Override
    public String resolve(Class clazz) {
        Assert.notNull(clazz, "clazz is required");
        return clazz.getSimpleName();
    }
}
