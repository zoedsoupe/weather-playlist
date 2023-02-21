(ns weather-playlist.http-client
  (:require [clojure.data.json :as json]
            [clj-http.client :as client])
  (:refer-clojure :exclude [get]))

(def ^:private basic-config {:accept :json :throw-exceptions false})

(defn- match-resp [{:keys [status body]}]
  (let [json-body (json/read-str body)]
    (case status
      401 {:error :unauthorized :body json-body}
      404 {:error :not-found :body json-body}
      201 {:created json-body}
      200 {:ok json-body}
      _ {:status status :body json-body})))

(defn get [url params]
  (let [with-params (assoc basic-config :query-params params)]
    (-> url
        (client/get with-params)
        match-resp)))

(defn post [url body]
  (let [json-body (json/write-str body)
        post-config (into basic-config {:body json-body
                                        :content-type :json})]
    (-> url
        (client/post post-config)
        match-resp)))
