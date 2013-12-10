(ns mapy.core
  (:require [ring.adapter.jetty :only [run-jetty]]
            [ring.util.response :as resp]
            [compojure.core :refer [defroutes GET POST]]
            [compojure.route :refer [resources not-found]]
            [compojure.handler :refer [site]]
            [paxdb.graph :refer [memorize-graph recall-graph weighted-graph]]))

(def db-namespace "mapy")

(defroutes main-routes
    (GET "/" []
         (resp/redirect "/index.html"))

    (GET "/route/:id" [id]
         (str [id 50.2233 50.33]))

    (POST "/nodes" [coord weight node]
          (memorize-graph db-namespace 
              {node
               {coord weight}})
          "okay got it")

    (GET "/nodes" []          
         (str (recall-graph db-namespace)))

    (GET "/graph" []
         (str weighted-graph))


    (resources "/")
    (not-found "Page not found"))

(def handler
  (site main-routes))
