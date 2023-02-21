(ns weather-playlist.core
  (:require [io.pedestal.http :as http]
            [weather-playlist.service :as service]
            [weather-playlist.graphql.schema :as graphql-schema])
  (:gen-class))

(defonce server (atom nil))

(def graphql-schema (graphql-schema/load-schema))

(defn run-dev []
  (swap! server (fn [srv]
                  (when srv
                    (http/stop srv))
                  (-> (service/service-map {:env :dev
                                            :graphiql true
                                            :schema graphql-schema})
                      (http/create-server)
                      (http/start)))))

(defn -main [& args]
  (swap! server (fn [srv]
                  (when srv
                    (http/stop srv))
                  (-> (service/service-map {:env :prod
                                            :graphiql false
                                            :schema graphql-schema})
                      (http/create-server)
                      (http/start)))))
