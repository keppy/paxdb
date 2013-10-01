(ns paxdb.graph
  (:use paxdb.persist))

(def label-regex
  "regex to get the labels from a group of edges"
  (re-pattern "[1-9]|[1-9][0-9]|[1-9][0-9][0-9]"))

(def weight-regex
  "regex to get the weights from a group of edges"
  (re-pattern "[a-z]"))

(defn deref-edges
  "dereference the key-value edge pairs"
  [edges]
  (let [labels (remove empty? (clojure.string/split edges label-regex))
        weights (remove empty? (clojure.string/split edges weight-regex))]
    (doseq [[x y] (map list labels weights)]
      (prn x y))))
