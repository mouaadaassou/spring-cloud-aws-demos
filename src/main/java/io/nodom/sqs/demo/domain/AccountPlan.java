package io.nodom.sqs.demo.domain;



import lombok.Getter;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@Getter
@XmlType(name = "accountPlan")
@XmlEnum
public enum AccountPlan {
    @XmlEnumValue("yearly")
    YEARLY("yearly"),
    @XmlEnumValue("monthly")
    MONTHLY("monthly");

    private final String accountPlan;

    AccountPlan(String accountPlan) {
        this.accountPlan = accountPlan;
    }
}
