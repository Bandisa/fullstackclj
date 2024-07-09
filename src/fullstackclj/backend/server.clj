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
    (let [crlf "\r\n"
          request (loop [line (.readLine in) message []]
                    (if (or (nil? line) (empty? line)) message
                        (recur (.readLine in) (conj message (str line crlf)))))]
      (println (http/parse (str/join request))))
    (.println out (http/response {:http_vers "HTTP/1.1"
                                  :status_code 200
                                  :status_info "SUCCESS"
                                  :headers {"Content-Length" 0}
                                  :body "some data"}))))

(defn start-server
  "Start the server"
  []
  (let [server-socket (ServerSocket. config/PORT config/BACKLOG (InetAddress/getByName config/ADDRESS))]
    (println (str "Server started on port " config/PORT))
    (while true
      (let [client-socket (.accept server-socket)]
        (println "Client connected. Address: " (str (.getRemoteSocketAddress client-socket)))
        (.start (Thread. #(handle-client client-socket)))))))