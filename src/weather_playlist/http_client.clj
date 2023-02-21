(ns weather-playlist.http-client
  (:require [clj-http.client :as client]
            [clj-http.headers :as headers])
  (:refer-clojure :exclude [get]))

(def ^:private basic-config {:accept :json :as :json :throw-exceptions false})

(defn- match-resp [{:keys [status body]}]
  (case status
      400 {:error :bad-request :body body}
      401 {:error :unauthorized :body body}
      404 {:error :not-found :body body}
      201 {:created body}
      200 {:ok body}
      {:status status :body body}))

(defn- request [req]
  (client/with-middleware [client/wrap-basic-auth
                           client/wrap-form-params
                           client/wrap-content-type
                           client/wrap-query-params
                           client/wrap-url
                           client/wrap-output-coercion
                           client/wrap-method
                           headers/wrap-header-map]
    (client/request (merge basic-config req))))

(defn get [url & [req]]
  (-> {:method :get :url url}
      (merge req)
      request
      match-resp))

(defn post [url & [req]]
  (-> {:method :post :url url}
      (merge req)
      request
      match-resp))
