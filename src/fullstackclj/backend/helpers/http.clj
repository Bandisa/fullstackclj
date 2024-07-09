(ns fullstackclj.backend.helpers.http
  (:require [clojure.string :as str]))

(def method_regx #"(?:GET|POST|PUT|DELETE|PATCH|HEAD|OPTIONS|TRACE|CONNECT)")
(def http_vers_regx #"(?:http|HTTP)\/\d\.\d")
(def body_regx #"\r\n\r\n")
(def crlf_regx #"\r\n")

(defn parser
  "Parses an http request"
  [request]
  (let [lines (str/split request body_regx)
        raw_req_header (str/split (first lines) crlf_regx)
        req_line (first raw_req_header)

        method (re-find method_regx req_line)
        url (second (str/split req_line #"\s"))
        http_vers (re-find http_vers_regx req_line)
        headers (rest raw_req_header)
        body (second lines)]
    (println headers (type [1 2]))
    {:method method :url url :http_vers http_vers
     :headers headers
     :body body}))