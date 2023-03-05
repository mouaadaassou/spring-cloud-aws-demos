package io.nodom.sqs.demo;

import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import io.awspring.cloud.sqs.operations.TemplateAcknowledgementMode;
import io.awspring.cloud.sqs.support.converter.SqsMessagingMessageConverter;
import io.nodom.sqs.demo.domain.UserWithMetaData;
import io.nodom.sqs.demo.service.FakeSqsClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Slf4j
@SpringBootApplication
public class SqsDemoApplication {

    @Value("${sqs.fake-queue-name}")
    private String queueName;

    public static void main(String[] args) {
        SpringApplication.run(SqsDemoApplication.class, args).close();
    }

    @Bean
    public CommandLineRunner commandLineRunner(FakeSqsClient fakeSqsClient) {
        return commandLineRunner -> {
            log.info("Running CommandLineRunner Bean...");
            fakeSqsClient.receiveMessage(180);
            log.info("Done Running CommandLineRunner Bean!");
        };
    }

    @Bean(name = "sqsAsyncClient")
    public SqsAsyncClient sqsAsyncClient(AwsCredentialsProvider awsCredentialsProvider) {
        return SqsAsyncClient.builder()
                .credentialsProvider(awsCredentialsProvider)
                .build();
    }

    @Bean(name = "sqsTemplate")
    public SqsTemplate sqsTemplate(SqsAsyncClient sqsAsyncClient, XmlMapper xmlMapper) {
        return SqsTemplate.builder()
                .sqsAsyncClient(sqsAsyncClient)
                .configure(sqsTemplateOptions ->
                        sqsTemplateOptions.defaultQueue(queueName)
                                .acknowledgementMode(TemplateAcknowledgementMode.MANUAL)
                                .defaultPollTimeout(Duration.of(0, ChronoUnit.SECONDS))
                                .defaultMaxNumberOfMessages(10))
                .messageConverter(sqsMessagingMessageConverter(xmlMapper))
                .build();
    }

    private SqsMessagingMessageConverter sqsMessagingMessageConverter(XmlMapper xmlMapper) {
        SqsMessagingMessageConverter sqsMessagingMessageConverter = new SqsMessagingMessageConverter();
        sqsMessagingMessageConverter.setObjectMapper(xmlMapper);
        sqsMessagingMessageConverter.setPayloadTypeHeader(UserWithMetaData.class.getName());

        return sqsMessagingMessageConverter;
    }

    @Bean(name = "xmlMapper")
    public XmlMapper xmlMapper() {
        JacksonXmlModule xmlModule = new JacksonXmlModule();
        xmlModule.setDefaultUseWrapper(false);
        XmlMapper xmlMapper = new XmlMapper(xmlModule);
        xmlMapper.registerModule(new JaxbAnnotationModule());

        return xmlMapper;
    }

}
