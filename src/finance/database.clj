(ns finance.database)

(def transactions (atom []))

(defn get-transactions []
  @transactions)

(defn insert [transaction]
  (let [transaction-inserted (swap! transactions conj transaction)]
    (merge transaction {:id (count transaction-inserted)})))

(defn clear []
  (reset! transactions []))