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

(def string-keyword-graph
  {"james" {"jerry" 3}
   "jerry" {"james" 3}})

(defn dfs
  "http://codereview.stackexchange.com/questions/15961/
  depth-first-search-algorithm-in-clojure"
  [graph goal]
  (fn search
    [path visited]
    (let [current (peek path)]
      (if (= goal current)
        [path]
        (->> current graph keys
             (remove visited)
             (mapcat #(search (conj path %) (conj visited %))))))))

(defn findpath
  "Returns a lazy sequence of all directed paths from start to goal
  within graph."
  [graph start goal]
  ((dfs graph goal) [start] #{start}))

(def graph (atom {}))

(def label-regex
  "regex to get the labels from a group of edges"
  (re-pattern "[1-9]|[1-9][0-9]|[1-9][0-9][0-9]"))

(def weight-regex
  "regex to get the weights from a group of edges"
  (re-pattern "[a-z]"))

(defn decode-edges
  "get k-v pairs from encoded edges string"
  [edges]
  (let [labels (remove empty? (clojure.string/split edges label-regex))
        weights (remove empty? (clojure.string/split edges weight-regex))]
    (zipmap labels weights)))

(defn decode-node
  "get node map with node name as key and an edges hash as the val"
  [node]
  (let [k (strip-namespace (first node))
        v (decode-edges (last node))]
    `(~@k ~@v)))

(defn memorize-graph
  "store one or more node-edges constructs in memory under a namespace"
  [namespace node-edges]
  (doseq [x node-edges]
    (let [edge-keys (map name (keys (last x)))
          edge-vals (map str (vals (last x)))]
        (store-pair namespace
         (vector
          (name (first x))
          (clojure.string/join
           (interleave edge-keys edge-vals)))))))

(defn recall-graph
  "recall a graph from memory"
  [namespace]
  (let [pairs (get-namespaced-pairs namespace)]
    (doseq [pair pairs]
      (swap! graph assoc
        (strip-namespace (first pair))
        (decode-edges (last pair)))))
  graph)

(defn recall-node
  "get a single node from memory and de-serialize it"
  [namespace node]
  (let [edges (get-pair namespace node)]
    {node (decode-edges edges)}))
