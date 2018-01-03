(ns kafka-playground.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [kafka-playground.api :refer [main-handler]]
            [kafka-playground.kafka :refer [topics]]
            [kafka-playground.stream :as stream])
  (:gen-class))

(defn -main
  "Start the streams application, then start a ring handler to receive posts"
  [& args]
  (doseq [topic (vals topics)]
    (.start (stream/create-printer-stream topic)))
  (.start stream/app)
  (run-jetty main-handler {:port 3000}))
