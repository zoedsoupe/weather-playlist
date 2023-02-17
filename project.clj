(defproject weather-playlist "0.1.0-SNAPSHOT"
  :description "Generates a playlist based on a city temperature"
  :url "https://weather-playlist.fly.dev"
  :license {:name "BSD 3-Clause"}
  :dependencies [[org.clojure/clojure "1.11.1"]]
  :main ^:skip-aot weather-playlist.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
