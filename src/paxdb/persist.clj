(ns paxdb.persist
  (:require 
   [clj-leveldb :as l]
   [byte-streams :as bs]))

(def hard-pairs
  [["1" "b"]
   ["200" "d"]
   ["5" "f"]])

(def big-pairs
  [["2409" "standard"]
   ["1034" "whoopy cush"]
   ["2245" "cowboy"]])

(def uni-start (char 0))

(def delim-regex
  "produce the proper delimiter regex from uni-start"
  (re-pattern (str uni-start)))

(def namespace-regex
  "regex for matching the namespace and delimiter"
  (re-pattern (str "^[A-Za-z0-9]+" uni-start)))

(def db
  (l/create-db
   (str "/tmp/clojurecharacters")
   {:key-decoder bs/to-string
    :val-decoder bs/to-string}))

(def namespaces-db
  (l/create-db
   (str "/tmp/clojurenames")
   {:key-decoder bs/to-string
    :val-decoder bs/to-string}))

(defn namespaced-key
  "creates a namespaced key out of a namespace and a string x"
  [namespace k]
  (list namespace nil k))

(defn strip-namespace
  "strips the namespace data from a key"
  [k]
  (clojure.string/replace k namespace-regex ""))

(defn first-key
  "the first possible key within a namespace"
  [namespace]
  (str namespace uni-start))

(defn store-pair
  "store a single k/v pair in a given namespace"
  [namespace p]
  (l/put db
         (namespaced-key namespace (first p))
         (last p)))

(defn store-pairs
  "store the k/v pairs in a given namespace"
  [namespace pairs]
  (doseq [x pairs]
    (l/put db
           (namespaced-key namespace (first x))
           (last x))))

(defn get-namespaced-pairs
  "get all the pairs of a given namespace"
  [namespace]
  (let [possible-pairs (l/iterator db namespace nil)]
    (filter #(= (first (clojure.string/split (first %) delim-regex)) 
                namespace)
            possible-pairs)))
