#!/bin/bash

export AWS_PAGER=""

# shellcheck disable=SC2034
for index in {1..200}; do
  id="$(uuidgen)"
  user_password="$id"
  user_name="andrew-$id"
  aws sqs send-message --queue-url "${QUEUE_URL}" --message-body "<users>
                                                                 <user>
                                                                   <username>$user_name</username>
                                                                   <password>$user_password</password>
                                                                   <subscription>premiumSubscription</subscription>
                                                                   <accountPlan>yearly</accountPlan>
                                                                 </user>
                                                                 </users>
                                                                 " --message-group-id "group-$id" --message-deduplication-id "$id" | cat >> /dev/null
echo "$user_name", " - ", "$user_password"
done
