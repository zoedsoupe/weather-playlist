(ns weather-playlist.service
  (:require [io.pedestal.http :as http]
            [com.walmartlabs.lacinia.pedestal2 :as p2]))

(defn service-map [opts]
  (let [{:keys [schema env graphiql]} opts
        graphql-interceptors (p2/default-interceptors schema nil nil)
        graphiql-route (into #{["/graphiql" :get (p2/graphiql-ide-handler opts) :route-name ::graphiql-id]}
                             (p2/graphiql-asset-routes "/assets/graphiql"))
        graphql-route #{["/api" :post graphql-interceptors :route-name ::graphql-api]}
        base-map {::http/routes graphql-route ::http/type :immutant ::http/port 4000}]
    (println (update base-map ::http/routes conj graphiql-route))
    (if (and (= env :dev) graphiql)
      (-> base-map
          #_(http/dev-interceptors)
          (assoc ::http/secure-headers nil)
          (update ::http/routes conj graphiql-route))
      (http/default-interceptors base-map))))
