(ns fullstackclj.backend.helpers.http
  (:require [clojure.string :as str]))

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

(defn response
  "Returns an HTTP response as a string"
  [{:keys [http_vers status_code status_info headers body]}]
  (let [space " "
        crlf "\r\n"
        headers (assoc headers "Content-Length" (count (or body "")))]
    (str http_vers space status_code space status_info crlf
         (apply str (map #(str (key %) ": " (val %) crlf) headers))
         crlf
         (if (nil? body) "" (str body crlf)))))