(ns paxdb.graph
  (:use paxdb.persist))

(def weighted-graph
  {:s {:a 3 :d 4}
   :a {:s 3 :d 5 :b 4}
   :b {:a 4 :e 5 :c 4}
   :c {:b 4}
   :d {:s 4 :a 5 :e 2}
   :e {:d 2 :b 5 :f 4}
   :f {:e 4 :g 1}})

(def label-regex
  "regex to get the labels from a group of edges"
  (re-pattern "[1-9]|[1-9][0-9]|[1-9][0-9][0-9]"))

(def weight-regex
  "regex to get the weights from a group of edges"
  (re-pattern "[a-z]"))

(defn deref-edges
  "dereference the encoded edge string into k-v pairs"
  [edges]
  (let [labels (remove empty? (clojure.string/split edges label-regex))
        weights (remove empty? (clojure.string/split edges weight-regex))]
    (map vector labels weights)))

(defn memorize-nodes
  "store one or more node-edges constructs in memory under a namespace"
  [namespace node-edges]
  (doseq [x node-edges]
    (let [edge-keys (map name (keys (last x)))
          edge-vals (map str (vals (last x)))]
        (prn
         (vector
          (name (first x))
          (clojure.string/join
           (interleave edge-keys edge-vals)))))))

