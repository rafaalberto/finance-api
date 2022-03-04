(ns finance.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [cheshire.core :as json]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :refer [wrap-json-body]]
            [finance.database :as database]))

(defn content-as-json [content & [status]]
  {:status  (or status 200)
   :headers {"Content-Type" "application/json; charset=utf-8"}
   :body    (json/generate-string content)})

(defroutes app-routes
           (GET "/balance" [] (content-as-json {:balance 0}))
           (POST "/transactions" request (-> (database/insert (:body request))
                                             (content-as-json 201)))
           (route/not-found "Not Found"))

(def app
  (-> (wrap-defaults app-routes api-defaults)
      (wrap-json-body {:keywords? true :bigdecimals? true})))