(ns fullstackclj.backend.server
  (:require [fullstackclj.backend.config :as config]
            [fullstackclj.backend.helpers.http :as http]
            [clojure.string :as str])
  (:import (java.io BufferedReader InputStreamReader PrintWriter)
           (java.net ServerSocket InetAddress)))

(defn handle-client
  "Handle socket connection"
  [client-socket]
  (with-open [in (BufferedReader. (InputStreamReader. (.getInputStream client-socket)))
              out (PrintWriter. (.getOutputStream client-socket) true)]
    (let [req_line (.readLine in)
          headers (loop [lines (.readLine in) header {}]
                    (let [header_kv (str/split lines #":\s")]
                      (if (or (nil? lines) (empty? lines)) header
                          (recur (.readLine in) (assoc header (str/lower-case (first header_kv)) (last header_kv))))))
          content_len (Integer/parseInt (get headers "content-length"))
          body (char-array content_len)]
      (.read in body 0 content_len)
      (println (http/parse [req_line headers (String. body)])))
    (.println out (http/response {:http_vers "HTTP/1.1"
                                  :status_code 200
                                  :status_info "SUCCESS"
                                  :headers {"Content-Length" 0}
                                  :body "some data"}))
    (.flush out)))

(defn start-server
  "Start the server"
  []
  (let [server-socket (ServerSocket. config/PORT config/BACKLOG (InetAddress/getByName config/ADDRESS))]
    (println (str "Server started on port " config/PORT))
    (while true
      (let [client-socket (.accept server-socket)]
        (println "Client connected. Address: " (str (.getRemoteSocketAddress client-socket)))
        (.start (Thread. #(handle-client client-socket)))))))