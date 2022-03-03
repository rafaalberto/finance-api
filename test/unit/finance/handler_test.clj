(ns finance.handler-test
  (:require [midje.sweet :refer :all]
            [ring.mock.request :as mock]
            [finance.handler :refer :all]))

(facts "Display 'Hello World' at root route"
       (let [response (app (mock/request :get "/"))]
         (fact "Status is 200"
               (:status response) => 200)
         (fact "Response body is 'Hello Clojure!'"
               (:body response) => "Hello Clojure!")))

(facts "Initial balance is 0"
       (let [response (app (mock/request :get "/balance"))]
         (fact "Status is 200"
               (:status response) => 200)
         (fact "Response body is '0'"
               (:body response) => "0")))

(facts "Invalid route does not exists"
       (let [response (app (mock/request :get "invalid"))]
         (fact "Error code is 404"
               (:status response) => 404)
         (fact "Response body is 'Not Found'"
               (:body response) => "Not Found")))