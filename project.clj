(defproject kafka-playground "0.1.0-SNAPSHOT"
  :description "For learning about kafka"
  :license {:name "GPL v3"
            :url "http://www.gnu.org/licenses/gpl.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.apache.kafka/kafka-streams "1.0.0"]
                 [ring/ring-core "1.6.3"]
                 [ring/ring-json "0.4.0"]
                 [ring/ring-jetty-adapter "1.6.3"]
                 [compojure "1.6.0"]
                 [org.clojure/tools.logging "0.4.0"]
                 [ch.qos.logback/logback-classic "1.2.3"]]
  :plugins [[lein-ring "0.8.10"]]
  :ring {:handler kafka-playground.core/main-handler}
  :main ^:skip-aot kafka-playground.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev {:dependencies [[proto-repl "0.3.1"]
                                  [org.clojure/tools.namespace "0.2.11"]]}})
