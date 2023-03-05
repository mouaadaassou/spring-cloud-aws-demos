package io.nodom.sqs.demo.domain;



import lombok.Getter;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@Getter
@XmlType(name = "subscription")
@XmlEnum
public enum Subscription {
    @XmlEnumValue("premiumSubscription")
    PREMIUM_SUBSCRIPTION("premiumSubscription"),
    @XmlEnumValue("regularSubscription")
    REGULAR_SUBSCRIPTION("regularSubscription");

    private final String subscriptionType;
    Subscription(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }
}
