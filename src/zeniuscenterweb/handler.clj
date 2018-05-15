(ns zeniuscenterweb.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [zeniuscenterweb.routes.home :refer :all]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(def app
  (wrap-defaults app-routes site-defaults))
