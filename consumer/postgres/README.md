# consumer-postgres
This app consumes all kafka topics and stores the values in the database. After creating new topics, restart this application and a table will be generated automatically.

## How to prepare
Required libraries:

- python3
- postgresql-dev
- librdkafka

Required non-standard python packages:
- psycopg2
- avro-python3
- confluent_kafka
- kafka_connector

Install all required libraries and python packages.

## Getting started
First, clone this project to your local PC. Then, you can run `consumer.py`. For parameterization, environment variables are used.
```
$ git clone https://github.com/smueller18/solar-thermal-climate-system.git
$ cd solar-thermal-climate-system/consumer/postgres
$ python3 consumer.py
```

| variable | default | type | info |
| --- | --- | --- | --- |
| POSTGRES_HOST | postgres | string |   |
| POSTGRES_PORT | 5432 | int |   |
| POSTGRES_DB | postgres | string |   |
| POSTGRES_USER | postgres | string |   |
| POSTGRES_PW | postgres | string |   |
| KAFKA_HOSTS | kafka:9092 | string |   |
| SCHEMA_REGISTRY_URL | http://schema_registry:8082 | string |  |
| CONSUMER_GROUP | postgres | string |   |
| TOPIC_PREFIX | test. | string | The prefix is part of a regular expression. The char `.` is replaced with `\.`, all other special characters are not allowed. |
| LOGGING_LEVEL | INFO | string | one of CRITICAL, ERROR, WARNING, INFO, DEBUG |
