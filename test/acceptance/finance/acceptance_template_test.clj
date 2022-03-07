(ns finance.acceptance-template-test
  (:require [finance.handler :refer [app]])
  (:require [ring.adapter.jetty :refer [run-jetty]])
  (:require [clj-http.client :as http]
            [cheshire.core :as json]))

(def server (atom nil))

(defn start-server [port]
  (swap! server
         (fn [_] (run-jetty app {:port port :join? false}))))

(defn stop-server []
  (.stop @server))

(def default-port 3001)

(defn create-url [route]
  (str "http://localhost:" default-port route))

(def request-to (comp http/get create-url))

(defn get-content [route] (:body (request-to route)))

(defn content-as-json [transaction]
  {:content-type     :json
   :body             (json/generate-string transaction)
   :throw-exceptions false})

(defn deposit [amount]
  (content-as-json {:amount amount :type "Deposit"}))

(defn withdraw [amount]
  (content-as-json {:amount amount :type "Withdraw"}))