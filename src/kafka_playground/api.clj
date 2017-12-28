(ns kafka-playground.api
  (:require [kafka-playground.kafka :refer [topics publish]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [compojure.core :refer :all]
            [compojure.route :as route])
  (:gen-class))

(defn upsert-collection-handler
  "Publishes request body with collection info to kafka"
  [request]
  (let [topic (:collection topics)
        key   "abc"
        value (str (:body request))]
    {:status 200
     :body {:sent (str (publish topic key value))}}))

(defn upsert-granule-handler
  "Publishes request body with granule info to kafka"
  [request]
  (let [topic (:granule topics)
        key   "abc"
        value (str (:body request))]
    {:status 200
     :body {:sent (str (publish topic key value))}}))

(defroutes app-routes
  (PUT "/collection" request upsert-collection-handler)
  (PUT "/granule" request upsert-granule-handler)
  (route/not-found {:status 404 :body {:message "404, fool."}}))

(def main-handler
  (-> app-routes
      (wrap-json-body {:keywords? true})
      (wrap-json-response)))
