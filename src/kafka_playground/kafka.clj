(ns kafka-playground.kafka
  (:import (org.apache.kafka.clients.consumer KafkaConsumer ConsumerConfig)
           (org.apache.kafka.clients.producer KafkaProducer ProducerConfig ProducerRecord))
  (:gen-class))

(defn create-producer
  "Creates a kafka producer"
  []
  (KafkaProducer.
   {ProducerConfig/BOOTSTRAP_SERVERS_CONFIG "localhost:9092"
    ProducerConfig/KEY_SERIALIZER_CLASS_CONFIG "org.apache.kafka.common.serialization.StringSerializer"
    ProducerConfig/VALUE_SERIALIZER_CLASS_CONFIG "org.apache.kafka.common.serialization.StringSerializer"
    ProducerConfig/CLIENT_ID_CONFIG "producer"}))

(defn create-consumer
  "Creates a kafka consumer, duh"
  []
  (KafkaConsumer.
   {ConsumerConfig/BOOTSTRAP_SERVERS_CONFIG "localhost:9092"
    ConsumerConfig/KEY_DESERIALIZER_CLASS_CONFIG "org.apache.kafka.common.serialization.StringDeserializer"
    ConsumerConfig/VALUE_DESERIALIZER_CLASS_CONFIG "org.apache.kafka.common.serialization.StringDeserializer"
    ConsumerConfig/GROUP_ID_CONFIG "foo"
    ConsumerConfig/CLIENT_ID_CONFIG "consumer"}))

(def topic "topic")

(defn create-record
  [key value]
  (ProducerRecord. topic key value))

(defn pub-and-sub
  "Sends a message, then subscribes to it and pulls it back down"
  []
  (let [producer (create-producer)
        consumer (create-consumer)
        msg (ProducerRecord. topic "abc" "this is a message")]
    (println @(.send producer msg))
    (.subscribe consumer [topic])
    (let [it (.iterator (.poll consumer 10000))]
      (while (.hasNext it)
        (as-> (.next it) msg
              (println (str "Received message " (.key msg) ": " (.value msg))))))
    (.close producer)
    (.close consumer)))
