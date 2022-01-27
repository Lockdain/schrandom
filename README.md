# Schrandom
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/e76dfd3f2eca4063a79c40018d1440ab)](https://www.codacy.com/gh/Lockdain/schrandom/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Lockdain/schrandom&amp;utm_campaign=Badge_Grade)

## A swiss-army knife all-in-one tool for Apache Kafka and Schema Registry related projects

### What is Schrandom?
Schrandom aims to become a solid "all-in-one" testing tool for those who extensively use Apache Avro as a main data serialization format and thus are also familiar with Schema Registries and Apache Kafka.

Schrandom addresses the following goals:
* Provides API to generate random records according to the schema (both provided explicitly or from Schema Registry) and publish them to Kafka topic;
* Allows generating load test profiles depending on your needs (you may define how many random messages you want to be generated or make the task continuous that may last forever providing you with a constant message stream);
* Allows validating messages against schema obtained both from Schema Registry or provided explicitly;

### API
Schrandom uses REST API as a control plane. Any function Schrandom provides can be accessed through the API.

Schrandom provides swagger documentation which is always accessible at `http://host:port/docs`.

The main endpoints are:
`schrandom/engage`: used to assign message-generating processes to the service.
It also splits into three branches:
* **/continuos** for everlasting tasks;
* **/externalSchema** for tasks that utilizes explicit AVRO schemas;
* **/limited** for bounded tasks that products a limited message sets;

A set of `/schrandom/disengage` endpoints are used to cancel current tasks from the execution.

Please see the latest swagger documentation as a reference.

### Deployment
Schrandom is a regular Scala application that can be deployed in any JVM-friendly environment.

We encourage you to use Schrandom with any containerization engine you want: K8S, Docker Swarm, etc.

Schrandom acts as a standalone microservice that interacts with your current AVRO-related infrastructure represented by Schema Registry and Apache Kafka.

You are able to use any packaging you want but we encourage you to go with `sbt-native-packager`.

Please see the diagram below:

### Under the hood
Schrandom uses `Monix` as a main execution environment/scheduler. Each task created using REST API in fact translates to a specific `Runnable` that assigns to the scheduler.

Thanks to `Cancelable` Futures provided by Monix it is possible to cancel any running task at any time.

Schrandom uses `Tapir` to describe HTTP-endpoints and generate swagger documentation along with `Netty` used as NIO-server.