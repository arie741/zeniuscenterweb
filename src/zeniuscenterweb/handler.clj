(ns zeniuscenterweb.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [zeniuscenterweb.routes.home :refer :all]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [noir.util.middleware :as middleware]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]))

(def app (middleware/app-handler
       [app-routes]
       :middleware [wrap-params wrap-multipart-params]))
