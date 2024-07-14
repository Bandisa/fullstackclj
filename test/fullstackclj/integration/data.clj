(ns fullstackclj.integration.data
  (:import [com.fullstackclj.proto Request Request$REQUEST_TYPE]))

(def test_rpc_body
  (.toByteArray (.build (.setData (.setType (Request/newBuilder)
                                            Request$REQUEST_TYPE/TYPE_TEST)
                                  "rpc response"))))