(ns fullstackclj.backend.http-test
  (:require [clojure.test :refer [deftest is testing]]
            [fullstackclj.backend.helpers.http :refer [handle parse]]
            [fullstackclj.backend.mock-data :refer [parser_test_data]]
            [fullstackclj.helpers.http :refer [contains_http_params?
                                               has-key-values?]]))

(deftest http-test-parser
  (testing "Parser, Should return a map of the http request as: {:method :url :http_vers :headers :body}")
  (is (= true (map? (parse ["GET / "]))))
  (is (= true (contains_http_params? (parse ["GET / " {:a "s", :b "q"} "body"]))))
  (is (= true (has-key-values? (parse (get-in parser_test_data [:case1 :req]))
                               (get-in parser_test_data [:case1 :res])))))

(deftest http-test-handler
  (testing "Handler, Should return a valid rpc or http response, given an http request")
  (is (= "HTTP/1.1 200 SUCCESS\r\nContent-type: text/plain\r\nContent-Length: 13\r\n\r\nhttp response\r\n"
         (handle {:method "GET"
                  :url "/"
                  :http_vers "http/1.1"
                  :headers {"a: s", "b: q"}
                  :body "http response"})))
  (is (= "HTTP/1.1 200 SUCCESS\r\nContent-type: application/octet-stream\r\n\r\nType: TYPE_TEST\nData: \"rpc response\"\n"
         (handle {:method "POST"
                  :url "/rpc/test-procedure"
                  :http_vers "http/1.1"
                  :headers {"Content-type" " application/octet-stream"}
                  :body "rpc response"}))))

;; TODO: Integration test