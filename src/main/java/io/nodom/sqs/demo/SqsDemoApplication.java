package io.nodom.sqs.demo;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import io.awspring.cloud.sqs.operations.TemplateAcknowledgementMode;
import io.awspring.cloud.sqs.support.converter.SqsMessagingMessageConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Slf4j
@SpringBootApplication
public class SqsDemoApplication implements AsyncConfigurer {

    @Value("${sqs.fake-queue-name}")
    private String queueName;

    public static void main(String[] args) {
        SpringApplication.run(SqsDemoApplication.class, args).close();
    }

    @Bean
    public CommandLineRunner commandLineRunner(FakeSqsClient fakeSqsClient) {
        return commandLineRunner -> {
            log.info("Running CommandLineRunner Bean...");
            fakeSqsClient.receiveMessage(240);
            log.info("Done Running CommandLineRunner Bean!");
        };
    }

    @Bean(name = "sqsAsyncClient")
    public SqsAsyncClient sqsAsyncClient() {
        return SqsAsyncClient.builder()
                .region(Region.EU_CENTRAL_1)
                .build();
    }


    @Bean(name = "sqsTemplate")
    public SqsTemplate sqsTemplate(SqsAsyncClient sqsAsyncClient) {
        SqsMessagingMessageConverter sqsMessagingMessageConverter = new SqsMessagingMessageConverter();
        sqsMessagingMessageConverter.setObjectMapper(new JsonMapper());
        sqsMessagingMessageConverter.setPayloadTypeHeader("io.nodom.sqs.demo.UserWithMetaData");

        return SqsTemplate.builder()
                .sqsAsyncClient(sqsAsyncClient)
                .configure(sqsTemplateOptions ->
                        sqsTemplateOptions.defaultQueue(queueName)
                                .acknowledgementMode(TemplateAcknowledgementMode.MANUAL)
                                .defaultPollTimeout(Duration.of(0, ChronoUnit.SECONDS))
                                .defaultMaxNumberOfMessages(10))
                .messageConverter(sqsMessagingMessageConverter)
                .build();
    }

}
