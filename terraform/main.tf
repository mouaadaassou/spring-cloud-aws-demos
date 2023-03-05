provider "aws" {
  region = "eu-central-1"
}

resource "aws_dynamodb_table" "product-catalog" {
  name           = "ProductCatalog"
  billing_mode   = "PROVISIONED"
  read_capacity  = 10
  write_capacity = 5
  hash_key       = "Id"

  attribute {
    name = "Id"
    type = "N"
  }

  ttl {
    attribute_name = "TimeToExist"
    enabled        = false
  }

  tags = {
    name        = "ProductCatalog"
    Environment = "Workshop"
  }
}

resource "aws_dynamodb_table" "forum" {
  name           = "Forum"
  billing_mode   = "PROVISIONED"
  read_capacity  = 10
  write_capacity = 5
  hash_key       = "Name"

  attribute {
    name = "Name"
    type = "S"
  }

  ttl {
    attribute_name = "TimeToExist"
    enabled        = false
  }

  tags = {
    name        = "Forum"
    Environment = "Workshop"
  }
}

resource "aws_dynamodb_table" "thread" {
  name           = "Thread"
  billing_mode   = "PROVISIONED"
  read_capacity  = 10
  write_capacity = 5
  hash_key       = "ForumName"
  range_key      = "Subject"

  attribute {
    name = "ForumName"
    type = "S"
  }

  attribute {
    name = "Subject"
    type = "S"
  }

  ttl {
    attribute_name = "TimeToExist"
    enabled        = false
  }

  tags = {
    name        = "Thread"
    Environment = "Workshop"
  }
}

resource "aws_dynamodb_table" "Reply" {
  name           = "Reply"
  billing_mode   = "PROVISIONED"
  read_capacity  = 10
  write_capacity = 5
  hash_key       = "Id"
  range_key      = "ReplyDateTime"

  attribute {
    name = "Id"
    type = "S"
  }

  attribute {
    name = "ReplyDateTime"
    type = "S"
  }

  ttl {
    attribute_name = "TimeToExist"
    enabled        = false
  }

  tags = {
    name        = "Reply"
    Environment = "Workshop"
  }
}