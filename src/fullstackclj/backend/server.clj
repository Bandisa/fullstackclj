(ns fullstackclj.backend.server
  (:require [clojure.string :as str]
            [fullstackclj.backend.config :as config]
            [fullstackclj.backend.helpers.http :as http])
  (:import (java.io BufferedReader InputStreamReader PrintWriter)
           (java.net InetAddress ServerSocket)))

(defn handle-client
  "Handle socket connection"
  [client-socket]
  (with-open [in (BufferedReader. (InputStreamReader. (.getInputStream client-socket)))
              out (PrintWriter. (.getOutputStream client-socket) true)]
    (let [req_line (.readLine in)
          headers (loop [lines (.readLine in) header {}]
                    (let [header_kv (str/split lines #":\s")]
                      (if (or (nil? lines) (empty? lines)) header
                          (recur (.readLine in)
                                 (assoc header (str/lower-case (first header_kv)) (last header_kv))))))
          content_len (Integer/parseInt (get headers "content-length" "0"))
          body (char-array (when (> content_len 0) content_len))
          request (http/parse [req_line headers ""])]
      (.read in body 0 content_len)
      ;;TODO: Auth
      (.println out (http/handle (assoc request :body (String. body)))))
    (.flush out)))

(defn start-server
  "Start the server"
  []
  (let [server-socket (ServerSocket. config/PORT config/BACKLOG (InetAddress/getByName config/ADDRESS))]
    (println (str "Server started on port " config/PORT))
    (while true
      (let [client-socket (.accept server-socket)]
        (println "Client connected. Address: " (str (.getRemoteSocketAddress client-socket)))
        ;;TODO: add MAX_THREADS limit
        (.start (Thread. #(handle-client client-socket)))))))