(ns fullstackclj.integration.server-test
  (:require [clojure.test :refer [deftest is testing]]
            [fullstackclj.backend.server :refer [handle-client]]
            [fullstackclj.integration.data :refer [test_rpc_body]])
  (:import (java.io ByteArrayInputStream ByteArrayOutputStream)
           (java.net Socket)))

(defn mock-client-socket [byte_array]
  (proxy [Socket] []
    (getInputStream [] (ByteArrayInputStream. byte_array))
    (getOutputStream [] (let [byte_len (alength byte_array)
                              output_stream (ByteArrayOutputStream. byte_len)]
                          (.write output_stream byte_array 0 byte_len) output_stream))))

(deftest  ^:integration int-test
  (testing "Should return a valid http response"
    (is (= (str "HTTP/1.1 200 SUCCESS\r\n"
                "Content-type: text/plain\r\n"
                "Content-Encoding: UTF-8\r\n"
                "Content-Length: 13\r\n\r\n"
                "http response\r\n")
           (handle-client (mock-client-socket
                           (.getBytes
                            (str "GET / HTTP/1.1\r\n"
                                 "Content-type: text/plain\r\n"
                                 "Content-Length: 13\r\n\r\n"
                                 "http response\r\n")
                            "UTF-8"))))))
  (testing "Should return a valid rpc response"
    (is (= (str "HTTP/1.1 200 SUCCESS\r\nContent-Encoding: UTF-8\r\nContent-type: application/octet-stream\r\n"
                "Content-Length: " (alength test_rpc_body) "\r\n\r\n"
                (slurp test_rpc_body
                       :encoding "UTF-8"))
           (handle-client (mock-client-socket (.getBytes (str
                                                          "POST /rpc/test-procedure http/1.1\r\n"
                                                          "Content-Encoding: UTF-8\r\nContent-type: application/octet-stream\r\n"
                                                          "Content-Length: " (alength test_rpc_body) "\r\n\r\n"
                                                          (slurp test_rpc_body
                                                                 :encoding "UTF-8")))))))))
;; TODO: Add more test cases

