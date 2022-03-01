(ns acceptance.finance-api.balance-acceptance-test
  (:require [midje.sweet :refer :all])
  (:require [finance-api.handler :refer [app]])
  (:require [ring.adapter.jetty :refer [run-jetty]])
  (:require [clj-http.client :as http]))

(def server (atom nil))

(defn start-server [port]
  (swap! server
         (fn [_] (run-jetty app {:port port :join? false}))))

(defn stop-server []
  (.stop @server))

(fact "Starts and stops server" :acceptance
      (start-server 3001)
      (:body (http/get "http://localhost:3001/balance")) => "0"
      (stop-server))