(ns fullstackclj.backend.rest.core)

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