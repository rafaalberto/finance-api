(ns finance.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [cheshire.core :as json]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :refer [wrap-json-body]]
            [finance.database :as database]
            [finance.transaction :as transaction]))

(defn content-as-json [content & [status]]
  {:status  (or status 200)
   :headers {"Content-Type" "application/json; charset=utf-8"}
   :body    (json/generate-string content)})

(defroutes app-routes
           (GET "/balance" [] (content-as-json {:balance (database/get-balance)}))
           (POST "/transactions" request
             (if (transaction/is-valid? (:body request))
               (-> (database/insert (:body request))
                   (content-as-json 201))
               (content-as-json {:message "Invalid request"} 422)))
           (GET "/transactions" {filters :params}
             (content-as-json {:transactions
                               (if (empty? filters)
                                 (database/get-transactions)
                                 (database/transactions-by-filter filters))}))
           (GET "/deposits" [] (content-as-json {:transactions (database/transactions-by-type "Deposit")}))
           (GET "/withdraws" [] (content-as-json {:transactions (database/transactions-by-type "Withdraw")}))
           (route/not-found "Not Found"))

(def app
  (-> (wrap-defaults app-routes api-defaults)
      (wrap-json-body {:keywords? true :bigdecimals? true})))