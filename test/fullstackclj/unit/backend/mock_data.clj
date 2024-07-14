(ns fullstackclj.unit.backend.mock-data
  (:import [com.fullstackclj.proto Request Request$REQUEST_TYPE]))

(def parser_test_data
  {:case1 {:req ["GET / http/1.1" {"a: s", "b: q"} "body"]
           :res {:method "GET"
                 :url "/"
                 :http_vers "http/1.1"
                 :headers {"a: s", "b: q"}
                 :body "body"}}})

(def test_rpc_body
  (.toByteArray (.build (.setData (.setType (Request/newBuilder)
                                            Request$REQUEST_TYPE/TYPE_TEST)
                                  "rpc response"))))