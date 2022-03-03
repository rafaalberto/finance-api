(ns finance.balance-acceptance-test
  (:require [midje.sweet :refer :all])
  (:require [cheshire.core :as json])
  (:require [finance.acceptance-template-test :refer :all]))

(against-background
  [(before :facts (start-server default-port))
   (after :facts (stop-server))]
  (fact "The initial balance is 0" :acceptance
        (json/parse-string (get-content "/balance") true) => {:balance 0}))