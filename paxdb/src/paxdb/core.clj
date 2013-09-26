(ns paxdb.core)

(defn push-message
  "pushes messages onto a broadcaster, using a perscribed scale for ordering"
  [messages broadcaster scale]
  (-> (scale messages) (broadcaster)))

(defn scale
  "scales messages based on weight. message is a bunch of map vals :name weight"
  [messages]
  (sorted-map messages))

(defmacro broadcaster
  "creates or finds the correct broadcast channel for these messages")

(defn choose
  "chose the right broadcaster to send the message to")
