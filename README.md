# quarkus-aws-events

This is an example project to demonstrate a simple event based communication between two quarkus projects.
SNS is used as a event publishing system, while SQS is used to store messages in a queue.

## Starting the project:

**NOTE: If the projects are started in the wrong order, the event communication will not work!**

**The consumer starts a localstack instance with both the SNS and SQS devservice. 
The producer then uses the same localstack instance to publish the messages.**

```shell script
cd sqs-event-consumer
quarkus dev
```
open a new terminal
```shell script
cd sns-event-producer
quarkus dev
```

## Expected behaviour:
The event publisher publishes messages regularly. Those messages are received and logged by the consumer.