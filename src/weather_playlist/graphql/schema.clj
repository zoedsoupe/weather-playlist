(ns weather-playlist.graphql.schema
  (:require [com.walmartlabs.lacinia.schema :as schema]
            [com.walmartlabs.lacinia.parser.schema :as parser.schema]
            [clojure.java.io :as io]))

(defn generate-playlist [_ctx _args _parent]
  {:name "WIP" :genres [] :artists [] :songs []})

(defn get-temperatures [_ctx _args _parent]
  [{:value "balmy" :genre "party"}
   {:value "hot" :genre "pop"}
   {:value "cold" :genre "rock"}
   {:value "freezing" :genre "classic"}])

(def resolvers {:resolvers {:Mutation
                            {:generate_playlist generate-playlist}
                            :Query
                            {:temperatures get-temperatures}}})

(defn load-schema []
  (-> (io/resource "schema.graphql")
      (slurp)
      (parser.schema/parse-schema resolvers)
      (schema/compile)))
