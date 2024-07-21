(ns fullstackclj.e2e.rest-test
  (:require [clj-http.client :as http_cl]
            [clojure.test :refer [deftest is testing]]
            [fullstackclj.helpers.http :refer [has-key-values?]]))

;; TODO: send http requests to server and eval responses

(deftest ^:e2e test_handler
  (testing "Should return a valid response")
  (let [response (http_cl/get (str "http://localhost:8888/test_handler")
                              {:body "this is a test"})]
    (is (= "this is a test"
           (get response :body)))
    (is (= true
           (has-key-values? response
                            ;; FIXME: has-key-values? only works for unnested maps
                            {;;  :protocol-version {:name "HTTP", :major 1, :minor 1}
                             :reason-phrase "SUCCESS",
                            ;;  :headers {"Content-type" "text/plain",
                            ;;            "Content-Encoding" "UTF-8",
                            ;;            "Content-Length" 14},
                             :orig-content-encoding "UTF-8", :status 200})))))