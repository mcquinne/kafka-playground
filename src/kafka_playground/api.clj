(ns kafka-playground.api
  (:require [kafka-playground.kafka :as kafka]
            [kafka-playground.stream :as stream]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [compojure.core :refer :all]
            [compojure.route :as route])
  (:gen-class))

(defn publish-record
  [type record]
  (if-let [key (str (:id record))]
    (let [topic (get kafka/topics type)
          msg   (str (assoc record :type (name type)))]
      {:status 201
       :body {:created (str (kafka/publish topic key msg))}})
    {:status 400
     :body {:failure "invalid record... did you provide an id?"}}))

(defroutes app-routes
  (PUT "/collection" request (publish-record :collection (:body request)))
  (GET "/collection/:id" [id] (stream/get-by-key :collection id))
  (PUT "/granule" request (publish-record :granule (:body request)))
  (GET "/granule/:id" [id] (stream/get-by-key :granule id))
  (route/not-found {:status 404 :body {:message "404, fool."}}))

(def main-handler
  (-> app-routes
      (wrap-json-body {:keywords? true})
      (wrap-json-response)))
