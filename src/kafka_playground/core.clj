(ns kafka-playground.core
  (:import (org.apache.kafka.clients.consumer KafkaConsumer)
           (org.apache.kafka.clients.producer KafkaProducer ProducerRecord)
           (java.util Properties)
           (java.util.concurrent Executors))
  (:gen-class))

(defn- create-producer
  "Creates a kafka producer"
  []
  (let [props (Properties.)]
    (doto props
          (.put "bootstrap.servers" "localhost:9092")
          (.put "key.serializer" "org.apache.kafka.common.serialization.StringSerializer")
          (.put "value.serializer" "org.apache.kafka.common.serialization.StringSerializer")
          (.put "client.id" "producer")
          (.put "acks" "1"))
    (KafkaProducer. props)))

(defn- create-consumer
  "Creates a kafka consumer, duh"
  []
  (let [props (Properties.)]
    (doto props
          (.put "bootstrap.servers" "localhost:9092")
          (.put "key.deserializer" "org.apache.kafka.common.serialization.StringDeserializer")
          (.put "value.deserializer" "org.apache.kafka.common.serialization.StringDeserializer")
          (.put "group.id" "foo")
          (.put "client.id" "consumer"))
    (KafkaConsumer. props)))

(defn -main
  "Do the things!"
  [& args]
  (let [producer (create-producer)
        consumer (create-consumer)
        topic "topic"
        msg (ProducerRecord. topic "abc" "this is a message")]
    (println @(.send producer msg))
    (.subscribe consumer [topic])
    (let [it (.iterator (.poll consumer 10000))]
      (while (.hasNext it)
        (as-> (.next it) msg
              (println (str "Received message " (.key msg) ": " (.value msg))))))
    (.close producer)
    (.close consumer)))
