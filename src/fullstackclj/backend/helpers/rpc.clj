(ns fullstackclj.backend.helpers.rpc
  (:import [com.fullstackclj.clj_proto Request Request$REQUEST_TYPE]))

;; TODO: add rpc handler
(defn t
  "Tests the construction of a protobuf"
  []
  (let [request-builder (Request/newBuilder)]
    (println (type request-builder))
    (.setData (.setType request-builder Request$REQUEST_TYPE/TYPE_TEST) "some data")
    (println (.toString (.build request-builder)))))
(t)