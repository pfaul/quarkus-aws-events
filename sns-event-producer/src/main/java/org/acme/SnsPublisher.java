package org.acme;

import io.quarkus.scheduler.Scheduled;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.CreateTopicResponse;

@ApplicationScoped
public class SnsPublisher {

    @Inject
    SnsClient snsClient;

    private String topicArn;

    @PostConstruct
    void createTopic() {
        CreateTopicResponse myTopic = snsClient.createTopic(req -> req.name("MyTopic"));
        topicArn = myTopic.topicArn();
    }

    @Scheduled(every = "30s")
    void publish() {
        snsClient.publish(req -> req.topicArn(topicArn).message("TestMessage " + System.currentTimeMillis()));
    }

}