(ns fullstackclj.backend.rpc.procedures.test-procedure
  (:require [fullstackclj.backend.rpc.core :as rpc]))

(defn test-procedure
  "Test procedure"
  [request]
  (rpc/response "HTTP/1.1" 200 "SUCCESS" {} request))