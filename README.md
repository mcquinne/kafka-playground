# kafka-playground
For playing! For learning!

## Usage

1. Install docker and docker-compose
1. Use docker-compose to fire up zookeeper and kafka:
    ```bash
    # Zookeeper needs to be up and running before Kafka will start
    docker-compose up -d zookeeper
    docker-compose up -d kafka

    # Optional, if you want to see their logs:
    docker-compose logs -f
    ```
1. Use [leiningen](https://leiningen.org/) to run the application
    ```bash
    ./lein run
    ```
1. Publish some metadata
    ```bash
    curl -X PUT -H "Content-Type: application/json" http://localhost:3000/granule -d '{"id": "1", "message": "Hello, world!"}'
    ```
1. Retrieve it
   ```bash
   curl http://localhost:3000/granule/1
   ```
  - Note: It appears to take a few minutes for the Kafka Streams topology to
  register itself as a consumer, create its local table storage, and start
  processing messages.

## Resources

### Fundamentals
- [Simplicity Matters](https://www.youtube.com/watch?v=rI8tNMsozo0)
- [The Log: What every software engineer should know about real-time data's unifying abstraction](https://engineering.linkedin.com/distributed-systems/log-what-every-software-engineer-should-know-about-real-time-datas-unifying)
- [Turning the database inside out](https://www.confluent.io/blog/turning-the-database-inside-out-with-apache-samza/)
- [Why Local State is a Fundamental primitive in Stream Processing](https://www.oreilly.com/ideas/why-local-state-is-a-fundamental-primitive-in-stream-processing)
- [How to Beat the CAP Theorem](http://nathanmarz.com/blog/how-to-beat-the-cap-theorem.html)

### Examples/Walkthroughs
- This entire [series](https://www.confluent.io/blog/data-dichotomy-rethinking-the-way-we-treat-data-and-services/) from [confluent](https://www.confluent.io/blog/build-services-backbone-events/) which [culminates](https://www.confluent.io/blog/apache-kafka-for-service-architectures/) in a  [walkthrough](https://www.confluent.io/blog/chain-services-exactly-guarantees/) of [building](https://www.confluent.io/blog/messaging-single-source-truth/) a streaming [system](https://www.confluent.io/blog/leveraging-power-database-unbundled/) is [great](https://www.confluent.io/blog/building-a-microservices-ecosystem-with-kafka-streams-and-ksql/).
