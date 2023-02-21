(ns weather-playlist.integrations.spotify
  (:import java.util.Base64)
  (:require [weather-playlist.http-client :as http-client]))

(def ^:private endpoint (System/getenv "SPOTIFY_ENDPOINT"))
(def ^:private client-id (System/getenv "SPOTIFY_CLIENT_ID"))
(def ^:private secret-id (System/getenv "SPOTIFY_SECRET_ID"))

(def ^:private token-endpoint "https://accounts.spotify.com/api/token")

(def access-token (atom nil))

(defn- encode-b64 [to]
  (.encodeToString (Base64/getEncoder) (.getBytes to)))

(defn authenticate []
  (let [auth (encode-b64 (str client-id ":" secret-id))
        headers {"Authorization" (str "Basic " auth)
                 "Content-Type" "application/x-www-form-urlencoded"}
        params {:grant-type "client_credentials"}]
    (http-client/post token-endpoint :query-params params :headers headers)))
