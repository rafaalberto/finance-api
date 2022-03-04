(ns finance.database-test
  (:require [midje.sweet :refer :all])
  (:require [finance.database :refer :all]))

(facts "Insert transaction in atom"
       (against-background [(before :facts (clear))])
       (fact "collection starts empty"
             (count (get-transactions)) => 0)
       (fact "first transaction"
             (insert {:amount 7 :type "Deposit"})
             => {:id 1 :amount 7 :type "Deposit"}
             (count (get-transactions)) => 1))