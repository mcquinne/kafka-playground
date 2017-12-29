(ns kafka-playground.api
  (:require [kafka-playground.kafka :refer [topics publish]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [compojure.core :refer :all]
            [compojure.route :as route])
  (:gen-class))

(defn publish-record
  [type record]
  (if-let [key (str (:id record))]
    (let [topic (get topics type)
          msg   (str (assoc record :type (name type)))]
      {:status 201
       :body {:created (str (publish topic key msg))}})
    {:status 400
     :body {:failure "invalid record... did you provide an id?"}}))

(defn collection-event
  [request]
  (publish-record :collection (:body request)))

(defn granule-event
  [request]
  (publish-record :granule (:body request)))

(defroutes app-routes
  (PUT "/collection" request collection-event)
  (PUT "/granule" request granule-event)
  (route/not-found {:status 404 :body {:message "404, fool."}}))

(def main-handler
  (-> app-routes
      (wrap-json-body {:keywords? true})
      (wrap-json-response)))
