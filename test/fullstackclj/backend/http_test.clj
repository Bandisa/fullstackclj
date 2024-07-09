(ns fullstackclj.backend.http-test
  (:require [clojure.test :refer :all]
            [fullstackclj.backend.helpers.http :refer [parser]]
            [fullstackclj.backend.mock-data :refer [parser_test_data]]
            [fullstackclj.core :refer :all]
            [fullstackclj.helpers.http :refer [contains_http_params?
                                                           has-key-values?]]))


(deftest http-test
  (testing "Parser, Should return a map of the request as: {:method :url :http_vers :headers :body}")
  (is (= true (map? (parser "GET / "))))
  (is (= true (contains_http_params? (parser "GET / \r\na:s\r\nb:q\r\n\r\nbody"))))
  (is (= true (has-key-values? (parser (get-in parser_test_data [:case1 :req]))
                               (get-in parser_test_data [:case1 :res])))))