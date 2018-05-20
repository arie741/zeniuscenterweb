(ns zeniuscenterweb.routes.home
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [net.cgrand.enlive-html :refer [deftemplate defsnippet] :as html]
            [zeniuscenterweb.db :as db]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [ring.util.response :as resp]))

;;dbase
(def c-landing-page-large-text 
	(str (apply :content (db/scomp "landing-page-large-text"))))

(def program-section
  (str (apply :content (db/scomp "program-section"))))

;;template

(deftemplate home "public/index.html"
  [landing-page-large-text program-section]
  [:div#landing-page-large-text] (html/html-content landing-page-large-text)
  [:div#program-section] (hmtl/html-content program-section))

(deftemplate cms "public/cms.html"
	[]
  [:form#cmsform] (html/append (html/html-snippet (anti-forgery-field))))

(defroutes app-routes
  (GET "/" [] (home c-landing-page-large-text program-section))
  (GET "/admin" []
  	(cms))
  (POST "/cms-action" {params :params}
  	(do
      (doseq [k (keys params)]
        (db/c-update (apply str (rest (str k))) (k params)))
      (resp/redirect "/")))
  (route/not-found "Not Found"))

