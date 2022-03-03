(ns finance.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [cheshire.core :as json]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defn balance-as-json [] {:headers {"Content-Type" "application/json; charset=utf-8"}
                          :body    (json/generate-string {:balance 0})})

(defroutes app-routes
           (GET "/" [] "Hello Clojure!")
           (GET "/balance" [] (balance-as-json))
           (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
