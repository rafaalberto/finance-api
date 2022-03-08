(ns finance.database)

(def transactions (atom []))

(defn get-transactions []
  @transactions)

(defn insert [transaction]
  (let [transaction-inserted (swap! transactions conj transaction)]
    (merge transaction {:id (count transaction-inserted)})))

(defn clear []
  (reset! transactions []))

(defn- deposit? [transaction]
  (= (:type transaction) "Deposit"))

(defn- calculate [total transaction]
  (let [amount (:amount transaction)]
    (if (deposit? transaction)
      (+ total amount)
      (- total amount))))

(defn get-balance []
  (reduce calculate 0 @transactions))

(defn transactions-by-type [type]
  (filter #(= type (:type %)) (get-transactions)))