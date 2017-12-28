(ns kafka-playground.kafka
  (:import [org.apache.kafka.clients.consumer KafkaConsumer ConsumerConfig]
           [org.apache.kafka.clients.producer KafkaProducer ProducerConfig ProducerRecord]
           [org.apache.kafka.streams.kstream Printed]
           [org.apache.kafka.streams StreamsBuilder KafkaStreams StreamsConfig]
           [org.apache.kafka.common.serialization Serdes])
  (:gen-class))

(def ^String topic "topic")

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

(defn create-record
  [key value]
  (ProducerRecord. topic key value))

(defn create-stream-config
  []
  (StreamsConfig.
   {StreamsConfig/APPLICATION_ID_CONFIG, "stream-app"
    StreamsConfig/BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"
    StreamsConfig/KEY_SERDE_CLASS_CONFIG,   (.getName (.getClass (Serdes/String)))
    StreamsConfig/VALUE_SERDE_CLASS_CONFIG, (.getName (.getClass (Serdes/String)))}))

(defn create-printer-stream
  []
  (let [config (create-stream-config)
        builder (StreamsBuilder.)
        stream (.stream builder topic)]
    (.print stream (Printed/toSysOut))
    (KafkaStreams. (.build builder) config)))

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
