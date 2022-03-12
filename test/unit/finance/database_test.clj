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

(facts "Filter transactions by type"
       (def random-transactions '({:amount 2 :type "Withdraw"}
                                  {:amount 10 :type "Deposit"}
                                  {:amount 200 :type "Withdraw"}
                                  {:amount 1000 :type "Deposit"}))
       (against-background
         [(before :facts
                  [(clear)
                   (doseq [transaction random-transactions]
                     (insert transaction))])]
         (fact "Find only Deposits"
               (transactions-by-type "Deposit") => '({:amount 10 :type "Deposit"}
                                                     {:amount 1000 :type "Deposit"}))
         (fact "Find only Withdraws"
               (transactions-by-type "Withdraw") => '({:amount 2 :type "Withdraw"}
                                                      {:amount 200 :type "Withdraw"}))))

(facts "Filter transactions by label"
       (def random-transactions '({:amount 7.0M :type "Withdraw" :labels ["ice cream" "entertainment"]}
                                  {:amount 88.0M :type "Withdraw" :labels ["book" "education"]}
                                  {:amount 106.0M :type "Withdraw" :labels ["course" "education"]}
                                  {:amount 8000.0M :type "Deposit" :labels ["salary"]}))
       (against-background
         [(before :facts
                  [(clear)
                   (doseq [transaction random-transactions]
                     (insert transaction))])]
         (fact "Find transaction with label 'salary'"
               (transactions-by-filter {:labels "salary"}) => '({:amount 8000.0M :type "Deposit" :labels ["salary"]}))
         (fact "Find transactions with label 'education'"
               (transactions-by-filter {:labels "education"}) => '({:amount 88.0M :type "Withdraw" :labels ["book" "education"]}
                                                                   {:amount 106.0M :type "Withdraw" :labels ["course" "education"]}))
         (fact "Find transactions with label 'book' or 'course'"
               (transactions-by-filter {:labels ["book" "course"]}) => '({:amount 88.0M :type "Withdraw" :labels ["book" "education"]}
                                                                         {:amount 106.0M :type "Withdraw" :labels ["course" "education"]}))))