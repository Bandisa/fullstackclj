(ns fullstackclj.core
  (:require [fullstackclj.backend.config :as config])
  (:require [fullstackclj.backend.server :as socket])
  (:gen-class))

(defn -main
  "Start server"
  [& args]
  (socket/start-server))