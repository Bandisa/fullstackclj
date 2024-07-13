(ns fullstackclj.backend.rest.handlers.test-handler
  (:require [fullstackclj.backend.rest.core :as rest]))

(defn test-handler
  [request]
  (rest/response {:http_vers "HTTP/1.1" :status_code 200 :status_info "SUCCESS"
                  :headers {"Content-type" "text/plain"
                            "Content-Encoding" "UTF-8"}
                  :body (get request :body "")}))