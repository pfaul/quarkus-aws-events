package org.acme;

import io.quarkus.logging.Log;
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
        topicArn = snsClient.listTopics().topics().get(0).topicArn();
        Log.info("Create topic with arn: " + topicArn);
    }

    @Scheduled(every = "30s")
    void publish() {
        String message = "TestMessage " + System.currentTimeMillis();
        snsClient.publish(req -> req.topicArn(topicArn).message(message));
        Log.info("Published: " + message);
    }

}