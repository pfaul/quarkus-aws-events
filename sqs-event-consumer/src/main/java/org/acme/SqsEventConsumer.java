package org.acme;

import io.quarkus.logging.Log;
import io.quarkus.scheduler.Scheduled;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.SubscribeResponse;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueResponse;
import software.amazon.awssdk.services.sqs.model.GetQueueAttributesResponse;
import software.amazon.awssdk.services.sqs.model.QueueAttributeName;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

@ApplicationScoped
public class SqsEventConsumer {

    @Inject
    SqsClient sqsClient;
    @Inject
    SnsClient snsClient;
    private String queueUrl;

    @PostConstruct
    void createQueue() {
        CreateQueueResponse response = sqsClient.createQueue(req -> req.queueName("MyQueue"));
        queueUrl = response.queueUrl();
        Log.info("Created queue with url: " + queueUrl);
        String queueArn = requestQueueArn(queueUrl);
        Log.info("Queue arn: " + queueArn);

        String topicArn = snsClient.listTopics().topics().get(0).topicArn();
        Log.info("Subscribing to topic arn: " + topicArn);
        SubscribeResponse subscribeResponse = snsClient.subscribe(req -> req.topicArn(topicArn)
                .protocol("sqs")
                .endpoint(queueArn));
        Log.info("Subscription returned this response: " + subscribeResponse);
    }

    private String requestQueueArn(String url) {
        GetQueueAttributesResponse attributeResponse = sqsClient.getQueueAttributes(req ->
                req.queueUrl(url).attributeNames(QueueAttributeName.QUEUE_ARN).build());
        return attributeResponse.attributes().get(QueueAttributeName.QUEUE_ARN);
    }

    @Scheduled(every = "5s")
    void receiveMessages() {
        ReceiveMessageResponse response = sqsClient.receiveMessage(req -> req.queueUrl(queueUrl));

        if (response.hasMessages()) {
            response.messages().forEach(Log::info);
        }
    }
}
