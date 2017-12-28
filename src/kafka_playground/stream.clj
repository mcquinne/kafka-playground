(ns kafka-playground.stream
  (:import [org.apache.kafka.streams StreamsBuilder KafkaStreams StreamsConfig]
           [org.apache.kafka.streams.kstream Printed]
           [org.apache.kafka.common.serialization Serdes])
  (:require [kafka-playground.kafka :refer [topics bootstrap-servers]])
  (:gen-class))

(defn create-stream-config
  [id]
  (StreamsConfig.
   {StreamsConfig/APPLICATION_ID_CONFIG,    id
    StreamsConfig/BOOTSTRAP_SERVERS_CONFIG, bootstrap-servers
    StreamsConfig/KEY_SERDE_CLASS_CONFIG,   (-> (Serdes/String) .getClass .getName)
    StreamsConfig/VALUE_SERDE_CLASS_CONFIG, (-> (Serdes/String) .getClass .getName)}))

(defn create-printer-stream
  [topic]
  (let [config (create-stream-config (str topic "-printer"))
        builder (StreamsBuilder.)
        stream (.stream builder topic)
        printer (-> (Printed/toSysOut) (.withLabel topic))]
    (.print stream printer)
    (KafkaStreams. (.build builder) config)))
