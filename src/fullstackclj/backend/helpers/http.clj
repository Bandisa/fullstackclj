(ns fullstackclj.backend.helpers.http
  (:require [clojure.string :as str]
            [fullstackclj.backend.rest.handlers.test-handler :refer [test-handler]]
            [fullstackclj.backend.rpc.procedures.test-procedure :refer [test-procedure]])
  (:import [com.fullstackclj.proto Request Request$REQUEST_TYPE]))

(def method_regx #"(?i)(?:GET|POST|PUT|DELETE|PATCH|HEAD|OPTIONS|TRACE|CONNECT)")
(def http_vers_regx #"(?i)(?:HTTP)\/\d\.\d")

(defn parse
  "Parses an http request
   
   request: a vector consisting of http request parameters
   returns: a map of http request elements"
  [request]
  (let [req_line (first request)
        method (re-find method_regx req_line)
        url (second (str/split req_line #"\s"))
        http_vers (re-find http_vers_regx req_line)
        headers (second request)
        body (peek request)]
    {:method method :url url :http_vers http_vers
     :headers headers
     :body body}))

(defn handle_rest
  "Handles a rest request"
  [request]
  (println "http request: " request)
  (test-handler request))

(defn handle_rpc
  "Handles a rpc request"
  [procedure bit_string]
  (let [proto (.build
               (.setData
                (.setType (Request/newBuilder)
                          Request$REQUEST_TYPE/TYPE_TEST) bit_string))]
    (println "rpc request: " (.toString proto))
    (case procedure
      "test-procedure" (test-procedure proto))))

(defn handle
  "Wrapper for handling http requests"
  [{:keys [method url headers body]}]
  ;; TODO: Verify request -> rpc: content-type:application/octet-stream, method:post...rest?
  (if (nil? (re-find #"^(?:/rpc/.*)" url))
    (handle_rest {:method method :url url :headers headers :body body})
    (handle_rpc (peek (str/split url #"^(?:/rpc/)")) body)))