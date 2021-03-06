(ns finance.filters-acceptance-test
  (:require [midje.sweet :refer :all]
            [cheshire.core :as json]
            [finance.acceptance-template-test :refer :all]
            [finance.database :as database]))

(def random-transactions
  '({:amount 7.0M :type "Withdraw" :labels ["ice cream" "entertainment"]}
    {:amount 88.0M :type "Withdraw" :labels ["book" "education"]}
    {:amount 106.0M :type "Withdraw" :labels ["course" "education"]}
    {:amount 8000.0M :type "Deposit" :labels ["salary"]}))

(against-background
  [(before :facts
           [(start-server default-port)
            (database/clear)])
   (after :facts (stop-server))]
  (fact "There is no 'Deposit'" :acceptance
        (json/parse-string (get-content "/deposits") true) => {:transactions '()})
  (fact "There is no 'Withdraw'" :acceptance
        (json/parse-string (get-content "/withdraws") true) => {:transactions '()})
  (fact "There is no transactions" :acceptance
        (json/parse-string (get-content "/transactions") true) => {:transactions '()})

  (against-background
    [(before :facts
             (doseq [transaction random-transactions]
               (database/insert transaction)))
     (after :facts (database/clear))]
    (fact "There are 3 'Withdraw'" :acceptance
          (count (:transactions
                   (json/parse-string (get-content "/withdraws") true))) => 3)
    (fact "There are 1 'Deposit'" :acceptance
          (count (:transactions
                   (json/parse-string (get-content "/deposits") true))) => 1)
    (fact "There are 4 transactions" :acceptance
          (count (:transactions
                   (json/parse-string (get-content "/transactions") true))) => 4)
    (fact "There is 1 'Deposit' with 'salary' label" :acceptance
          (count (:transactions
                   (json/parse-string (get-content "/transactions?labels=salary") true))) => 1)
    (fact "There are 2 'Withdraw' with 'book' or 'course' label" :acceptance
          (count (:transactions
                   (json/parse-string (get-content "/transactions?labels=book&labels=course") true))) => 2)
    (fact "There are 2 'Withdraw' with 'education' label" :acceptance
          (count (:transactions
                   (json/parse-string (get-content "/transactions?labels=education") true))) => 2)))