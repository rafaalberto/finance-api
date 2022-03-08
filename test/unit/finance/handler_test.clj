(ns finance.handler-test
  (:require [midje.sweet :refer :all]
            [ring.mock.request :as mock]
            [cheshire.core :as json]
            [finance.handler :refer :all]
            [finance.database :as database]))

(facts "Invalid route does not exists"
       (let [response (app (mock/request :get "invalid"))]
         (fact "Error code is 404"
               (:status response) => 404)
         (fact "Response body is 'Not Found'"
               (:body response) => "Not Found")))

(facts "Initial balance is 0"
       (against-background [(json/generate-string {:balance 0}) => "{\"balance\":0}"
                            (database/get-balance) => 0])
       (let [response (app (mock/request :get "/balance"))]
         (fact "The format is 'application/json'"
               (get-in response [:headers "Content-Type"]) => "application/json; charset=utf-8")
         (fact "Status is 200"
               (:status response) => 200)
         (fact "Response body is '0'"
               (:body response) => "{\"balance\":0}")))

(facts "Insert 'Deposit' transaction with amount 10"
       (against-background (database/insert {:amount 10 :type "Deposit"})
                           => {:id 1 :amount 10 :type "Deposit"})
       (let [response (app (-> (mock/request :post "/transactions")
                               (mock/json-body {:amount 10 :type "Deposit"})))]
         (fact "Status is 201"
               (:status response) => 201)
         (fact "Response body is a JSON"
               (:body response) => "{\"id\":1,\"amount\":10,\"type\":\"Deposit\"}")))

(facts "There are routes to handle transaction filter by type"
       (against-background [(database/transactions-by-type "Deposit") => '({:id 1 :amount 2000 :type "Deposit"})
                            (database/transactions-by-type "Withdraw") => '({:id 2 :amount 89 :type "Withdraw"})
                            (database/get-transactions) => '({:id 1 :amount 2000 :type "Deposit"}
                                                             {:id 2 :amount 89 :type "Withdraw"})]
                           (fact "Filter by Deposit"
                                 (let [response (app (mock/request :get "/deposits"))]
                                   (:status response) => 200
                                   (:body response) => (json/generate-string {:transactions '({:id 1 :amount 2000 :type "Deposit"})})))
                           (fact "Filter by Withdraw"
                                 (let [response (app (mock/request :get "/withdraws"))]
                                   (:status response) => 200
                                   (:body response) => (json/generate-string {:transactions '({:id 2 :amount 89 :type "Withdraw"})})))
                           (fact "No filter"
                                 (let [response (app (mock/request :get "/transactions"))]
                                   (:status response) => 200
                                   (:body response) => (json/generate-string {:transactions '({:id 1 :amount 2000 :type "Deposit"}
                                                                                              {:id 2 :amount 89 :type "Withdraw"})})))))