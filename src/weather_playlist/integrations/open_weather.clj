(ns weather-playlist.integrations.open-weather
  (:require [weather-playlist.http-client :as http-client]))

(def ^:private endpoint (System/getenv "OPEN_WEATHER_ENDPOINT"))
(def ^:private api-key (System/getenv "OPEN_WEATHER_API_KEY"))
(def ^:private geocode-path "/geo/1.0/direct")
(def ^:private weather-path "/data/2.5/weather")

(defn- parse-geocode [resp]
  (if (contains? resp :ok)
    (let [body (first (:ok resp))
          lat (get body :lat)
          lon (get body :lon)]
    (println body)
      {:lat lat :lon lon})
    resp))

(defn get-geocoding [city]
  (let [params {"q" city "limit" 1 "appid" api-key}]
    (-> endpoint
        (str geocode-path)
        (http-client/get {:query-params params})
        parse-geocode)))

(defn- parse-current-weather [resp]
  (if (contains? resp :ok)
    (let [body (:ok resp)
          temp (get-in body [:main :temp])
          feels-like (get-in body [:main :feels_like])
          weather (-> body 
                      (get :weather)
                      first
                      (get :main))]
      {:temp temp :feels-like feels-like :weather weather})
    resp))

(defn get-current-weather [lat lon]
  (let [params {"lat" lat "lon" lon "appid" api-key "units" "metric"}]
    (-> endpoint
        (str weather-path)
        (http-client/get {:query-params params})
        parse-current-weather)))
