(ns fullstackclj.backend.mock-data)
(def parser_test_data
  {:case1 {:req "GET / http/1.1\r\na: s\r\nb: q\r\n\r\nbody"
           :res {:method "GET"
                 :url "/"
                 :http_vers "http/1.1"
                 :headers ["a: s" "b: q"]
                 :body "body"}}})