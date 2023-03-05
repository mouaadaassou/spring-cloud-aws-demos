package io.nodom.sqs.demo.domain;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;

@XmlRootElement(name = "user")
public record User(@XmlElement(name = "username") String username,
                   @XmlElement(name = "password") String password,
                   @XmlSchemaType(name = "string") Subscription subscription,
                   @XmlSchemaType(name = "string") AccountPlan accountPlan) {
}
