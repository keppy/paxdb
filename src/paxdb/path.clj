(ns paxdb.path)

(defn distance 
  "Euclidean distance between 2 points"
  [[x1 y1] [x2 y2]]                     
  (Math/sqrt
   (+ (Math/pow (- x1 x2) 2)
      (Math/pow (- y1 y2) 2))))
