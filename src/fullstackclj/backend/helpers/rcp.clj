(ns fullstackclj.backend.helpers.rcp
  (:import [com.fullstackclj.clj_proto Request Request$REQUEST_TYPE]))

(defn t []
  (let [request-builder (Request/newBuilder)]
    (println (type request-builder))
    (.setData (.setType request-builder Request$REQUEST_TYPE/TYPE_TEST) "some data")
    (println (.toString (.build request-builder)))))
(t)