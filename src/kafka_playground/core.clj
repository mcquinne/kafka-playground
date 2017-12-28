(ns kafka-playground.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [kafka-playground.api :refer [kafka-pub-handler]])
  (:gen-class))

(def main-handler (-> kafka-pub-handler
                      (wrap-json-body {:keywords? true})
                      (wrap-json-response)))

(defn -main
  "Do the things!"
  [& args]
  (run-jetty main-handler {:port 3000}))
