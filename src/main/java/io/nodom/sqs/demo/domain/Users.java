package io.nodom.sqs.demo.domain;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "users")
public record Users(@XmlElement(name = "user")
                    List<User> user) {
}
