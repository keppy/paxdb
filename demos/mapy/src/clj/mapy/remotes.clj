(ns mapy.remotes
  (:require 
   [mapy.core :refer [handler]]
   [compojure.handler :refer [site]]
   [shoreleave.middleware.rpc :refer [defremote wrap-rpc]])
  (:import java.io.File))

(def app (-> (var handler)
             (wrap-rpc)
             (site)))
