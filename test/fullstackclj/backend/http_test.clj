(ns fullstackclj.backend.http-test
  (:require [clojure.test :refer :all]
            [fullstackclj.backend.helpers.http :refer [parse response]]
            [fullstackclj.backend.mock-data :refer [parser_test_data response_test_data]]
            [fullstackclj.core :refer :all]
            [fullstackclj.helpers.http :refer [contains_http_params?
                                               has-key-values?]]))


(deftest http-test-parser
  (testing "Parser, Should return a map of the http request as: {:method :url :http_vers :headers :body}")
  (is (= true (map? (parse "GET / "))))
  (is (= true (contains_http_params? (parse "GET / \r\na:s\r\nb:q\r\n\r\nbody"))))
  (is (= true (has-key-values? (parse (get-in parser_test_data [:case1 :req]))
                               (get-in parser_test_data [:case1 :res])))))

(deftest http-test-response
  (testing "Response, Should return a string of the http request")
  (is (= true (string? (response (first response_test_data)))))
  (is (= "HTTP/1.1 200 SUCCESS\r\n\r\n" (response (first response_test_data)))))