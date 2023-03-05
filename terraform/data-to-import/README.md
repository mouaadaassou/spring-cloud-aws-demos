# Workshop: Hands-on Labs for Amazon DynamoDB:

## Reading Item Collections using Query:
Item Collections are groups of items that share a Partition Key. By definition, Item Collections can only exists in tables that
have both a Partition key and a Sort Key.

We can read all or part of an Item Collection using the Query API. Reading in this context means, to read all or part of an Item Collection.

When we invoke the Query API we must specify a Key Condition Expression - similar to WHERE clause in SQL.

The Key Condition Expression will define the number of RCUs and RRUs that are consumed by out Query
