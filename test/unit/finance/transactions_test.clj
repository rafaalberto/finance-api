(ns finance.transactions-test
  (:require [midje.sweet :refer :all]
            [finance.transaction :refer :all]))

(fact "Transaction without amount is invalid"
      (is-valid? {:type "Deposit"}) => false)

(fact "Transaction with negative amount is invalid"
      (is-valid? {:amount -10 :type "Deposit"}) => false)

(fact "Transaction with amount no number type is invalid"
      (is-valid? {:amount "a" :type "Deposit"}) => false)

(fact "Transaction without type is invalid"
      (is-valid? {:amount 1000}) => false)

(fact "Transaction with unknown type is invalid"
      (is-valid? {:amount 50 :type "Transfer"}) => false)

(fact "Transaction with amount positive numeric and known type is valid"
      (is-valid? {:amount 500 :type "Deposit"}) => true)