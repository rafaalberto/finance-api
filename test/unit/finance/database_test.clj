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

(facts "Get Balance after insert transactions"
       (against-background [(before :facts (clear))])
       (fact "Balance is positive when there is only 'Deposit'"
             (insert {:amount 1 :type "Deposit"})
             (insert {:amount 10 :type "Deposit"})
             (insert {:amount 100 :type "Deposit"})
             (insert {:amount 1000 :type "Deposit"})
             (get-balance) => 1111)
       (fact "Balance is negative when there is only 'Withdraw'"
             (insert {:amount 2 :type "Withdraw"})
             (insert {:amount 20 :type "Withdraw"})
             (insert {:amount 200 :type "Withdraw"})
             (insert {:amount 2000 :type "Withdraw"})
             (get-balance) => -2222)
       (fact "Balance is calculated sum 'Deposit' minus 'Withdraw'"
             (insert {:amount 2 :type "Withdraw"})
             (insert {:amount 10 :type "Deposit"})
             (insert {:amount 200 :type "Withdraw"})
             (insert {:amount 1000 :type "Deposit"})
             (get-balance) => 808))