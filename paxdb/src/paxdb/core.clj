(ns paxdb.core
  (:use paxdb.graph)
  (:use paxdb.persist))

(defn push-message
  "pushes messages onto a broadcaster, using a perscribed scale for ordering"
  [messages broadcaster scale]
  (-> (scale messages) (broadcaster)))

(defn scale
  "scales messages based on weight. message is a bunch of map vals :name weight"
  [messages]
  (sorted-map messages))

(defmacro broadcaster
  [messages]
  "creates or finds the correct broadcast channel for these messages")

(defn choose
  [messages meta]
  "chose the right broadcaster to send the message to")
