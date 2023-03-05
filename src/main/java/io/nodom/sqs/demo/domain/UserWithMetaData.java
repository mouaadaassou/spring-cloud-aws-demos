package io.nodom.sqs.demo.domain;


import lombok.Builder;
import org.springframework.messaging.Message;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record UserWithMetaData(Message<Users> originalMessage, Users user) {

    public static List<Message<Users>> getOriginalMessageList(List<UserWithMetaData> userWithMetaDataList) {
        return userWithMetaDataList.stream().map(UserWithMetaData::originalMessage).collect(Collectors.toList());
    }
}
