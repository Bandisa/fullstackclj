(ns fullstackclj.backend.helpers.http
  (:require [clojure.string :as str]))

(def method_regx #"(?i)(?:GET|POST|PUT|DELETE|PATCH|HEAD|OPTIONS|TRACE|CONNECT)")
(def http_vers_regx #"(?i)(?:HTTP)\/\d\.\d")
(def body_regx #"\r\n\r\n")
(def crlf_regx #"\r\n")

(defn parse
  "Parses an http request
   
   returns: a map of http request elements"
  [request]
  (let [lines (str/split request body_regx)
        raw_req_header (str/split (first lines) crlf_regx)
        req_line (first raw_req_header)

        method (re-find method_regx req_line)
        url (second (str/split req_line #"\s"))
        http_vers (re-find http_vers_regx req_line)
        headers (rest raw_req_header)
        body (second lines)]
    {:method method :url url :http_vers http_vers
     :headers headers
     :body body}))

(defn response
  "Returns an HTTP response as a string"
  [{:keys [http_vers status_code status_info headers body]}]
  (let [space " "
        crlf "\r\n"
        headers (assoc headers "Content-Length" (count (or body "")))]
    (str http_vers space status_code space status_info crlf
         (apply str (map #(str (key %) ": " (val %) crlf) headers)) crlf
         (if (nil? body) "" (str body crlf)))))