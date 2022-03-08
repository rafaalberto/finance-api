(ns finance.balance-acceptance-test
  (:require [midje.sweet :refer :all])
  (:require [cheshire.core :as json])
  (:require [clj-http.client :as http])
  (:require [finance.database :as database])
  (:require [finance.acceptance-template-test :refer :all]))

(against-background
  [(before :facts [(start-server default-port)
                   (database/clear)])
   (after :facts (stop-server))]
  (fact "The initial balance is 0" :acceptance
        (json/parse-string (get-content "/balance") true) => {:balance 0})
  (fact "The balance must be 10 when put 'Deposit' transaction with 10" :acceptance
        (http/post (create-url "/transactions")
                   {:content-type :json
                    :body         (json/generate-string {:amount 10 :type "Deposit"})})
        (json/parse-string (get-content "/balance") true) => {:balance 10})
  (fact "The balance is 1000 when put 'Deposit' with 2000 and 'Withdraw'" :acceptance
        (http/post (create-url "/transactions") (deposit 2000))
        (http/post (create-url "/transactions") (deposit 2000))
        (http/post (create-url "/transactions") (withdraw 3000))
        (json/parse-string (get-content "/balance") true) => {:balance 1000})
  (fact "Deny transaction without amount" :acceptance
        (let [response (http/post (create-url "/transactions")
                                  (content-as-json {:type "Deposit"}))]
          (:status response) => 422))
  (fact "Deny transaction without negative amount" :acceptance
        (let [response (http/post (create-url "/transactions")
                                  (deposit -100))]
          (:status response) => 422))
  (fact "Deny transaction with no number type" :acceptance
        (let [response (http/post (create-url "/transactions")
                                  (deposit "a"))]
          (:status response) => 422))
  (fact "Deny transaction without type" :acceptance
        (let [response (http/post (create-url "/transactions")
                                  (content-as-json {:amount 1000}))]
          (:status response) => 422))
  (fact "Deny transaction with unknown type" :acceptance
        (let [response (http/post (create-url "/transactions")
                                  (content-as-json {:amount 1000 :type "Transfer"}))]
          (:status response) => 422)))