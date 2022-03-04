(ns finance.balance-acceptance-test
  (:require [midje.sweet :refer :all])
  (:require [cheshire.core :as json])
  (:require [clj-http.client :as http])
  (:require [finance.database :as database])
  (:require [finance.acceptance-template-test :refer :all]))

(against-background
  [(before :facts [(start-server default-port) (database/clear)])
   (after :facts (stop-server))]
  (fact "The initial balance is 0" :acceptance
        (json/parse-string (get-content "/balance") true) => {:balance 0})
  (fact "The balance must be 10 when put 'Deposit' transaction with 10" :acceptance
        (http/post (create-url "/transactions")
                   {:content-type :json
                    :body         (json/generate-string {:amount 10 :type "Deposit"})})
        (json/parse-string (get-content "/balance") true) => {:balance 10})
  (fact "The balance is 1000 when put 'Deposit' with 2000 and 'Withdraw'" :acceptance
        (http/post (create-url "/transactions")
                   {:content-type :json
                    :body         (json/generate-string {:amount 2000 :type "Deposit"})})
        (http/post (create-url "/transactions")
                   {:content-type :json
                    :body         (json/generate-string {:amount 2000 :type "Deposit"})})
        (http/post (create-url "/transactions")
                   {:content-type :json
                    :body         (json/generate-string {:amount 3000 :type "Withdraw"})})
        (json/parse-string (get-content "/balance") true) => {:balance 1000}))