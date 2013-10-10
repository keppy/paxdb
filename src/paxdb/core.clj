(ns paxdb.core
  (:use paxdb.graph)
  (:use paxdb.persist))

(defn scale
  "scales messages based on weight. messages is a map {:name weight}"
  [messages]
  (sorted-map messages))

(defn broadcast
  "creates or finds the correct broadcast channel for these messages"
  [messages]
  (prn messages))

(defn push-message
  "pushes message onto a broadcast function"
  [message scale]
  (-> (broadcast (scale messages))))
