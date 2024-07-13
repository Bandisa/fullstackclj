(ns fullstackclj.backend.rpc.core)

(defn response
  [http_vers status_code status_info headers protobuf]
  (let [space " "
        crlf "\r\n"
        headers (assoc headers "Content-type" "application/octet-stream")]
    (str http_vers space status_code space status_info crlf
         (apply str (map #(str (key %) ": " (val %) crlf) headers))
         crlf
         (slurp (.toByteArray protobuf) :encoding "UTF-8"))))