(defproject weather-playlist "0.1.0-SNAPSHOT"
  :description "Generates a playlist based on a city temperature"
  :url "https://weather-playlist.fly.dev"
  :license {:name "BSD 3-Clause"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [io.pedestal/pedestal.service "0.5.7"]
                 [io.pedestal/pedestal.route "0.5.7"]
                 [io.pedestal/pedestal.jetty "0.5.7"]
                 [org.clojure/data.json "0.2.6"]
                 [org.slf4j/slf4j-simple "1.7.28"]]
  :main ^:skip-aot weather-playlist.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
