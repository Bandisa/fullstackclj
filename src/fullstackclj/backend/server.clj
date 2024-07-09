(ns fullstackclj.backend.server
  (:import (java.net ServerSocket Socket)
           (java.io BufferedReader InputStreamReader PrintWriter))
  (:require [fullstackclj.backend.config :as config]))

(defn handle-client
  "Handle socket connection"
  [client-socket]
  (with-open [in (BufferedReader. (InputStreamReader. (.getInputStream client-socket)))
              out (PrintWriter. (.getOutputStream client-socket) true)]
    (loop [line (.readLine in)]
      (when line
        (println line)
        (.println out (str "Echo: " line))
        (recur (.readLine in))))))

(defn start-server
  "Start the server"
  []
  (let [server-socket (ServerSocket. config/port)]
    (println (str "Server started on port " config/port))
    (while true
      (let [client-socket (.accept server-socket)]
        (println "Client connected")
        (.start (Thread. #(handle-client client-socket)))))))