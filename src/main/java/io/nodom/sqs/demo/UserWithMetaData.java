package io.nodom.sqs.demo;


import lombok.Builder;
import org.springframework.messaging.Message;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record UserWithMetaData(Message<User> originalMessage, User user) {

    static List<Message<User>> getOriginalMessageList(List<UserWithMetaData> userWithMetaDataList) {
        return userWithMetaDataList.stream().map(UserWithMetaData::originalMessage).collect(Collectors.toList());
    }
}
