(ns fullstackclj.unit.backend.mock-data)
(def parser_test_data
  {:case1 {:req ["GET / http/1.1" {"a: s", "b: q"} "body"]
           :res {:method "GET"
                 :url "/"
                 :http_vers "http/1.1"
                 :headers {"a: s", "b: q"}
                 :body "body"}}})