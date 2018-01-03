(ns kafka-playground.stream
  (:import [org.apache.kafka.clients.consumer ConsumerConfig]
           [org.apache.kafka.streams StreamsBuilder KafkaStreams StreamsConfig]
           [org.apache.kafka.streams.kstream Printed Materialized]
           [org.apache.kafka.streams.state QueryableStoreTypes]
           [org.apache.kafka.common.serialization Serdes])
  (:require [kafka-playground.kafka :refer [topics bootstrap-servers]])
  (:gen-class))

(def stores {:collection "collection-store"
             :granule    "granule-store"})

(defn create-stream-config
  [id]
  (StreamsConfig.
   {StreamsConfig/APPLICATION_ID_CONFIG     id
    StreamsConfig/BOOTSTRAP_SERVERS_CONFIG  bootstrap-servers
    StreamsConfig/KEY_SERDE_CLASS_CONFIG    (-> (Serdes/String) .getClass .getName)
    StreamsConfig/VALUE_SERDE_CLASS_CONFIG  (-> (Serdes/String) .getClass .getName)
    StreamsConfig/COMMIT_INTERVAL_MS_CONFIG 500
    ConsumerConfig/AUTO_OFFSET_RESET_CONFIG "earliest"}))

(defn build-streams-app
  [builder config]
  (KafkaStreams. (.build builder) config))

(defn create-printer-stream
  [topic]
  (let [config (create-stream-config (str topic "-printer"))
        builder (StreamsBuilder.)
        stream (.stream builder topic)
        printer (-> (Printed/toSysOut) (.withLabel topic))]
    (.print stream printer)
    (build-streams-app builder config)))

(defn app-builder
  []
  (let [builder (StreamsBuilder.)]
    (doto builder
          (.table (:collection topics) (Materialized/as "collection-store"))
          (.table (:granule topics) (Materialized/as "granule-store")))))

(def app (build-streams-app (app-builder) (create-stream-config "main-app")))

(defn get-by-key
  [type key]
  (let [store (get stores type)]
    (println "getting record from" store "with key" key)
    (-> app
        (.store store (QueryableStoreTypes/keyValueStore))
        (.get (str key)))))
