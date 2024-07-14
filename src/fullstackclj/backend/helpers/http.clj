(ns fullstackclj.backend.helpers.http
  (:require [clojure.string :as str]
            [fullstackclj.backend.rest.core :as rest]
            [fullstackclj.backend.rest.handlers.test-handler :refer [test-handler]]
            [fullstackclj.backend.rpc.procedures.test-procedure :refer [test-procedure]])
  (:import [com.fullstackclj.proto Request])
  (:import [com.google.protobuf InvalidProtocolBufferException]))

(def method_regx #"(?i)(?:GET|POST|PUT|DELETE|PATCH|HEAD|OPTIONS|TRACE|CONNECT)")
(def http_vers_regx #"(?i)(?:HTTP)\/\d\.\d")

(defn parse
  "Parses an http request
   
   request: a vector consisting of http request parameters
   returns: a map of http request elements"
  [request]
  (let [req_line (first request)
        method (re-find method_regx req_line)
        ;;TODO? exract url using regex
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
  (test-handler request))

(defn handle_rpc
  "Handles a rpc request"
  [procedure byte_str]
  (try
    (case procedure
      "test-procedure" (let [proto (Request/parseFrom (.getBytes byte_str))]
                         (test-procedure proto)))
    (catch InvalidProtocolBufferException pb_exc
      (println "Invalid rpc request: " byte_str "\n cause: " (.getMessage pb_exc))
      (rest/response {:http_vers "HTTP/1.1"
                      :status_code 400
                      :status_info "BAD REQUEST"
                      :headers {"Content-Encoding" "UTF-8"
                                "Content-Type" "Application/text"}
                      :body (str "Invalid RPC request for /" procedure " procedure")}))))

(defn handle
  "Wrapper for handling http requests"
  [{:keys [method url headers body]}]
  (println method url headers body)
  ;; TODO: Verify request -> rpc: content-type:application/octet-stream, method:post...rest?
  (if (nil? (re-find #"^(?:/rpc/.*)" url))
    (handle_rest {:method method :url url :headers headers :body body})
    (handle_rpc (peek (str/split url #"^(?:/rpc/)")) body)))