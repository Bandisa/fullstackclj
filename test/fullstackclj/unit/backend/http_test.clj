(ns fullstackclj.unit.backend.http-test
  (:require [clojure.test :refer [deftest is testing]]
            [fullstackclj.backend.helpers.http :refer [handle parse]]
            [fullstackclj.unit.backend.mock-data :refer [parser_test_data]]
            [fullstackclj.helpers.http :refer [contains_http_params?
                                               has-key-values?]])
  (:import [com.fullstackclj.proto Request Request$REQUEST_TYPE]))

(deftest http-test-parser
  (testing "Parser, Should return a map of the http request as: {:method :url :http_vers :headers :body}")
  (is (= true (map? (parse ["GET / "]))))
  (is (= true (contains_http_params? (parse ["GET / " {:a "s", :b "q"} "body"]))))
  (is (= true (has-key-values? (parse (get-in parser_test_data [:case1 :req]))
                               (get-in parser_test_data [:case1 :res])))))

(deftest http-test-handler
  (testing "Handler, Should return a valid rpc or http response, given an http request")
  (is (= "HTTP/1.1 200 SUCCESS\r\nContent-type: text/plain\r\nContent-Encoding: UTF-8\r\nContent-Length: 13\r\n\r\nhttp response\r\n"
         (handle {:method "GET"
                  :url "/"
                  :http_vers "http/1.1"
                  :headers {"a: s", "b: q"}
                  :body "http response"})))
  (is (= (str "HTTP/1.1 200 SUCCESS\r\nContent-Encoding: UTF-8\r\nContent-type: application/octet-stream\r\n\r\n"
              (slurp (.toByteArray (.build (.setData (.setType (Request/newBuilder)
                                                               Request$REQUEST_TYPE/TYPE_TEST)
                                                     "rpc response")))
                     :encoding "UTF-8"))
         (handle {:method "POST"
                  :url "/rpc/test-procedure"
                  :http_vers "http/1.1"
                  :headers {"Content-type" " application/octet-stream"
                            "Content-Encoding" "UTF-8"}
                  :body (slurp (.toByteArray (.build (.setData (.setType (Request/newBuilder)
                                                                         Request$REQUEST_TYPE/TYPE_TEST)
                                                               "rpc response")))
                               :encoding "UTF-8")})))
  (is (= (str "HTTP/1.1 400 BAD REQUEST\r\nContent-Encoding: UTF-8\r\nContent-Type: Application/text\r\nContent-Length: 49\r\n\r\n"
              "Invalid RPC request for /test-procedure procedure\r\n")
         (handle {:method "POST"
                  :url "/rpc/test-procedure"
                  :http_vers "http/1.1"
                  :headers {"Content-type" " application/octet-stream"
                            "Content-Encoding" "UTF-8"}
                  :body "invalid rpc body"}))))

;; TODO: Integration test