# quarkus-aws-events

This is an example project to demonstrate a simple event based communication between two quarkus projects.
SNS is used as a event publishing system, while SQS is used to store messages in a queue.

## Starting the project:

```shell script
cd sns-event-producer
quarkus dev
```
open a new terminal
```shell script
cd sqs-event-consumer
quarkus dev
```

## Expected behaviour:
The event publisher publishes messages regularly. Those messages are received and logged by the consumer.

## Actual behaviour:
The localstack instance of the shared SNS Devservice is not able to publish the events to the SQS queue. Instead this exception is shown in the localstack container logs:

2024-02-20 09:18:07 2024-02-20T08:18:07.053  INFO --- [   asgi_gw_0] localstack.request.aws     : AWS sns.Publish => 200
2024-02-20 09:18:07 2024-02-20T08:18:07.231 ERROR --- [   asgi_gw_0] l.aws.handlers.logging     : exception during call chain: Service 'sqs' is not enabled. Please check your 'SERVICES' configuration variable.
2024-02-20 09:18:07 2024-02-20T08:18:07.231  INFO --- [   asgi_gw_0] l.aws.handlers.service     : Service 'sqs' is not enabled. Please check your 'SERVICES' configuration variable.
2024-02-20 09:18:07 2024-02-20T08:18:07.239  INFO --- [   sns_pub_0] l.services.sns.publisher   : Unable to forward SNS message to SQS: An error occurred (InternalFailure) when calling the GetQueueUrl operation: Service 'sqs' is not enabled. Please check your 'SERVICES' configuration variable. Traceback (most recent call last):
2024-02-20 09:18:07   File "/opt/code/localstack/localstack/services/sns/publisher.py", line 269, in _publish
2024-02-20 09:18:07     queue_url: str = sqs_queue_url_for_arn(subscriber["Endpoint"])
2024-02-20 09:18:07                      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
2024-02-20 09:18:07   File "/opt/code/localstack/localstack/utils/aws/arns.py", line 480, in sqs_queue_url_for_arn
2024-02-20 09:18:07     result = sqs_client.get_queue_url(QueueName=queue_name, QueueOwnerAWSAccountId=account_id)[
2024-02-20 09:18:07              ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
2024-02-20 09:18:07   File "/opt/code/localstack/.venv/lib/python3.11/site-packages/botocore/client.py", line 535, in _api_call
2024-02-20 09:18:07     return self._make_api_call(operation_name, kwargs)
2024-02-20 09:18:07            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
2024-02-20 09:18:07   File "/opt/code/localstack/.venv/lib/python3.11/site-packages/botocore/client.py", line 983, in _make_api_call
2024-02-20 09:18:07     raise error_class(parsed_response, operation_name)
2024-02-20 09:18:07 botocore.exceptions.ClientError: An error occurred (InternalFailure) when calling the GetQueueUrl operation: Service 'sqs' is not enabled. Please check your 'SERVICES' configuration variable.
2024-02-20 09:18:07 