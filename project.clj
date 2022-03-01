(defproject finance-api "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [compojure "1.6.2"]
                 [ring/ring-defaults "0.3.3"]
                 [clj-http "3.12.3"]]
  :plugins [[lein-ring "0.12.6"]]
  :ring {:handler finance-api.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.4.0"]
                        [midje "1.10.5"]
                        [ring/ring-core "1.9.5"]
                        [ring/ring-jetty-adapter "1.9.5"]]
         :plugins      [[lein-midje "3.2.2"]
                        [lein-cloverage "1.0.13"]]}})
