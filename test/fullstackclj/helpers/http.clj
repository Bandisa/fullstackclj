(ns fullstackclj.helpers.http)

(defn contains_http_params?
  "returns true if a given map contains the keys: method, url, http_vers, headers and body "
  [map_]
  (and
   (and (contains? map_ :method) (contains? map_ :url))
   (and (and (contains? map_ :http_vers)
             (contains? map_ :headers)) (contains? map_ :body))))

(defn has-key-values?
  "returns true if a given map contains the key-value pairs in items"
  [map_ items]
  (every? (fn [[k v]]
            (= (get map_ k) v))
          items))