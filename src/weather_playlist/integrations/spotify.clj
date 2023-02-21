(ns weather-playlist.integrations.spotify
  (:require [weather-playlist.http-client :as http-client]))

(def ^:private endpoint (System/getenv "SPOTIFY_ENDPOINT"))
(def ^:private client-id (System/getenv "SPOTIFY_CLIENT_ID"))
(def ^:private client-secret (System/getenv "SPOTIFY_CLIENT_SECRET"))

(def ^:private token-endpoint "https://accounts.spotify.com/api/token")

(def access-token (atom nil))

(defn- parse-token [resp]
  (if (contains? resp :ok)
    (let [token (get-in resp [:ok :access_token])]
      (reset! access-token token)
      token)
    resp))

(defn authenticate []
  (let [auth {:basic-auth (str client-id ":" client-secret)}
        content-type :application/x-www-form-urlencoded
        params {:grant_type "client_credentials"}
        req (-> auth
                (assoc :form-params params)
                (assoc :content-type content-type))]
    (-> token-endpoint
        (http-client/post req)
        parse-token)))
