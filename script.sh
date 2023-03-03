#!/bin/bash

export AWS_PAGER=""

# shellcheck disable=SC2034
for index in {1..100}
do
  aws sqs send-message --queue-url "${QUEUE_URL}" --message-body '{"username": "andrew1", "password": "123456789"}' --message-group-id "group-$(uuidgen)" --message-deduplication-id "$(uuidgen)"
done
