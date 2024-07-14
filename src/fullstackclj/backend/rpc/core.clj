(ns fullstackclj.backend.rpc.core)

(defn response
  [http_vers status_code status_info headers protobuf]
  (let [space " "
        crlf "\r\n"
        bytes (.toByteArray protobuf)
        headers (assoc headers "Content-type" "application/octet-stream"
                       "Content-Length" (alength bytes))]
    (str http_vers space status_code space status_info crlf
         (apply str (map #(str (key %) ": " (val %) crlf) headers))
         crlf
         (slurp bytes :encoding "UTF-8"))))