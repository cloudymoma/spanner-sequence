# The demo of sequence number generation in Cloud Spanner

## Briefing

The demo uses [Twitter snowflake id](https://github.com/twitter-archive/snowflake/tree/snowflake-2010) to generate sequence number in Cloud Spanner, which helps to smooth the migration from RDBMS with the sequence to Cloud Spanner.

Snowflake id is open source solution for generating unique ID numbers at high scale.

The demo uses the [Java version](https://github.com/downgoon/snowflake) of Twitter snowflake id.

## Snowflake id description

The snowflake id is made up of these following components:

+ Cont = 0 1 bit
+ Epoch timestamp in millisecond precision - 41 bits
+ Configured machine id - 10 bits
+ Sequence number - 12 bits

The snowflake id is basically incremental, but cannot guarantee the id is consecutive. 

## How to run the demo

1. Create the table Orders in the Cloud Spanner

```aidl

CREATE TABLE Orders (
    OrderId INT64,
    UserName STRING(MAX),
    Amount FLOAT64,
    OrderTime TIMESTAMP,
) PRIMARY KEY (OrderId)
```
2. Run the demo

```aidl
 mvn compile exec:java -Dexec.mainClass=com.google.cloud.binguo.demo.App
```

3. Check the result in the Cloud Spanner console
```aidl
select * from Orders;
```

The sequence number looks like below
```aidl
579604939769401344
579604940381769728
579604940381769729
579604940381769730
579604940385964032
579604940385964033
```
## Suggestion

Demo is not used for production. Before using sequence in Cloud Spanner, be noted that sequence should not be the prefix column of primary key, which causes hotspot and impact the performance.
