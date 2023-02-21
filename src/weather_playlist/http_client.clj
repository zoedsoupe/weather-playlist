(ns weather-playlist.http-client
  (:require [clojure.data.json :as json]
            [clj-http.client :as client]
            [clj-http.headers :as headers])
  (:refer-clojure :exclude [get]))

(def ^:private basic-config {:accept :json :as :json :throw-exceptions false})

(defn- match-resp [{:keys [status body]}]
  (let [json-body (json/read-str body)]
    (case status
      400 {:error :bad-request :body json-body}
      401 {:error :unauthorized :body json-body}
      404 {:error :not-found :body json-body}
      201 {:created json-body}
      200 {:ok json-body}
      {:status status :body json-body})))

(defn- request [req]
  (client/with-middleware [headers/wrap-header-map
                           client/wrap-query-params
                           client/wrap-url
                           client/wrap-output-coercion
                           client/wrap-method]
    (client/request (merge basic-config req))))

(defn get [url & {:keys [_headers _query-params] :as req}]
  (-> {:method :get :url url}
      (merge req)
      request
      match-resp))

(defn post [url & {:keys [_body _query-params _headers] :as req}]
  (-> {:method :post :url url}
      (merge req)
      request
      match-resp))
