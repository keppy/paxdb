(ns paxdb.persist
  (:require 
   [clj-leveldb :as l]
   [byte-streams :as bs]))

(def hard-pairs
  [["a" "b"]
   ["c" "d"]
   ["e" "f"]])

(def db
  (l/create-db
   (str "/tmp/testerlevel")
   {:key-decoder bs/to-string
    :val-decoder bs/to-string}))

(defn namespaced-key
  "creates a namespaced key out of a namespace and a string x"
  [namespace x]
  (clojure.string/join  [namespace (char 0) x]))

(defn first-key
  "the first possible key within a namespace"
  [namespace]
  (clojure.string/join [namespace (char 0)]))

(defn last-key
  "the last possible key within a namespace"
  [namespace]
  (clojure.string/join [namespace (char 0) (char 255)]))

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
  (l/iterator db (first-key namespace) 
              (last-key namespace)))
