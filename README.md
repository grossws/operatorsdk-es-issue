# About

Demo repository for [Java Operator SDK](https://javaoperatorsdk.io) 3.2.3
managed dependent resource status update issue.

## Environment

* JDK 17
* Quarkus 2.13.3.Final
* Quarkiverse Java Operator SDK 4.0.3
* Java Operator SDK 3.2.3

# Running example

*NOTE*: Current configuration deploys CRD to active kubernetes context

1. Build jar with `./gradlew assemble`
2. Start operator with `java -jar build/quarkus-app/quarkus-run.jar`
3. In another terminal deploy sample CR: `kubectl apply -f prj.yaml`
4. Search logs for `secondary status:` and `fetched status:`.
