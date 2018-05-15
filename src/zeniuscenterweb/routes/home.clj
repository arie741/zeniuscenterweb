(ns zeniuscenterweb.routes.home
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [net.cgrand.enlive-html :refer [deftemplate defsnippet] :as html]
            [zeniuscenterweb.db :as db]
            [ring.util.anti-forgery :refer [anti-forgery-field]]))

;;dbase
(def c-landingtext1 
	(str (apply :content (db/scomp "landingtext1"))))

(def c-landingtext2
	(str (apply :content (db/scomp "landingtext2"))))

;;template

(deftemplate home "public/index.html"
  [landingtext1 landingtext2]
  [:h1#landingtext1] (html/content landingtext1)
  [:h2#landingtext2] (html/content landingtext2))

(deftemplate cms "public/cms.html"
	[]
  [:form#cmsform] (html/append (html/html-snippet (anti-forgery-field))))

(defroutes app-routes
  (GET "/" [] (home c-landingtext1 c-landingtext2))
  (GET "/cms" []
  	(cms))
  (POST "/cms-action" {params :params}
  	(str params))
  (route/not-found "Not Found"))

