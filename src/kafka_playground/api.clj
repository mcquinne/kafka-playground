(ns kafka-playground.api
  (:require [kafka-playground.kafka :refer [create-record create-producer]])
  (:gen-class))


(defn kafka-pub-handler
  "Ring handler which publishes request body to kafka"
  [request]
  (let [producer (create-producer)
        msg (create-record "abc" (str (:body request)))]
    {:status 200
     :body {:sent (str @(.send producer msg))}}))
