(ns finance.acceptance-template-test
  (:require [finance.handler :refer [app]])
  (:require [ring.adapter.jetty :refer [run-jetty]])
  (:require [clj-http.client :as http]))

(def server (atom nil))

(defn start-server [port]
  (swap! server
         (fn [_] (run-jetty app {:port port :join? false}))))

(defn stop-server []
  (.stop @server))

(def default-port 3001)

(defn get-url [route]
  (str "http://localhost:" default-port route))

(def request-to (comp http/get get-url))

(defn get-content [route] (:body (request-to route)))