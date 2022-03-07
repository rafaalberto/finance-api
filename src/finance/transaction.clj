(ns finance.transaction)

(defn is-valid? [transaction]
  (and (contains? transaction :amount)
       (number? (:amount transaction))
       (pos? (:amount transaction))
       (contains? transaction :type)
       (or (= "Deposit" (:type transaction))
           (= "Withdraw" (:type transaction)))))