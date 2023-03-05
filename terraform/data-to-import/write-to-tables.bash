#!/bin/bash

aws dynamodb batch-write-item --request-items file://ProductCatalog.json

aws dynamodb batch-write-item --request-items file://Forum.json

aws dynamodb batch-write-item --request-items file://Thread.json

aws dynamodb batch-write-item --request-items file://Reply.json
