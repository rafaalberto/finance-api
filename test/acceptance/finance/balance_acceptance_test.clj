(ns finance.balance-acceptance-test
  (:require [midje.sweet :refer :all])
  (:require [cheshire.core :as json])
  (:require [clj-http.client :as http])
  (:require [finance.acceptance-template-test :refer :all]))

(against-background
  [(before :facts (start-server default-port))
   (after :facts (stop-server))]
  (fact "The initial balance is 0" :acceptance
        (json/parse-string (get-content "/balance") true) => {:balance 0})
  (fact "The balance must be 10 when put 'Deposit' transaction with 10" :acceptance
        (http/post (get-url "/transactions")
                   {:content-type :json
                    :body         (json/generate-string {:amount 10 :type "Deposit"})})
        (json/parse-string (get-content "/balance") true) => {:balance 10}))