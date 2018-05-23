(ns zeniuscenterweb.routes.home
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [net.cgrand.enlive-html :refer [deftemplate defsnippet] :as html]
            [zeniuscenterweb.db :as db]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [ring.util.response :as resp]
            [noir.session :as session]
            [hickory.core :as hk]))

;;function
(defn cont [content]
  (->> 
    (db/scomp content)
    (apply :content)
    (str)))

(defn log [acc]
  (session/put! :username acc))

(defn validate [succ fail]
  (if (= (session/get :username) "admin")
    succ
    fail))

;;dbase

(defn c-landing-page-large-text []
	(cont "landing-page-large-text"))

(defn c-program-section [] 
  (cont "program-section"))

(defn c-testimonial-section []
  (cont "testimonial-section"))

(defn c-about-section []
  (cont "about-section"))

(defn c-contact-section []
  (cont "contact-section"))

(defn c-map-section []
  (cont "map-section"))

(defn c-footer-section []
  (cont "footer-section"))

(defn c-header-section []
  (cont "header-section"))

(defn c-css-section []
  (cont "css-section"))

(defn c-js-section []
  (cont "js-section"))

;;template

(deftemplate login "public/login.html"
  [& msg]
  [:form#loginform] (html/append (html/html-snippet (anti-forgery-field)))
  [:div#msg] (html/content (first msg)))

(deftemplate home "public/index.html"
  [landing-page-large-text program-section testimonial-section about-section contact-section map-section footer-section header-section css-section js-section]
  [:section#fh5co-home] (html/html-content landing-page-large-text)
  [:section#fh5co-work] (html/html-content program-section)
  [:section#fh5co-testimonials] (html/html-content testimonial-section)
  [:section#fh5co-about] (html/html-content about-section)
  [:section#fh5co-contact] (html/html-content contact-section)
  [:div#map-section] (html/html-content map-section)
  [:footer#footer] (html/html-content footer-section)
  [:header#fh5co-header] (html/html-content header-section)
  [:style#css-section] (html/content css-section)
  [:script#js-section] (html/content js-section)) 

(deftemplate cms "public/cms.html"
	[landing-page-large-text program-section testimonial-section about-section contact-section map-section footer-section header-section css-section js-section]
  [:form#cmsform] (html/append (html/html-snippet (anti-forgery-field)))
  [:textarea#i-landing-page-large-text] (html/content landing-page-large-text)
  [:textarea#i-program-section] (html/content program-section)
  [:textarea#i-testimonial-section] (html/content testimonial-section)
  [:textarea#i-about-section] (html/content about-section)
  [:textarea#i-contact-section] (html/content contact-section)
  [:textarea#i-map-section] (html/content map-section)
  [:textarea#i-footer-section] (html/content footer-section)
  [:textarea#i-header-section] (html/content header-section)
  [:textarea#i-css-section] (html/content css-section)
  [:textarea#i-js-section] (html/content js-section))

(defroutes app-routes
  (GET "/" [] 
    (home (c-landing-page-large-text) (c-program-section) (c-testimonial-section) (c-about-section) (c-contact-section) (c-map-section) (c-footer-section)( c-header-section) (c-css-section) (c-js-section)))
  (GET "/admin" []
  	(validate 
      (cms (c-landing-page-large-text) (c-program-section) (c-testimonial-section) (c-about-section) (c-contact-section) (c-map-section) (c-footer-section)( c-header-section) (c-css-section) (c-js-section)) 
      (resp/redirect "/login")))
  (POST "/cms-action" {params :params}
  	(do
      (doseq [k (keys params)]
        (db/c-update (apply str (rest (str k))) (k params)))
      (resp/redirect "/")))
  (GET "/login" []
    (validate (resp/redirect "/admin") (login)))
  (POST "/login-action" [password]
    (if (= password (apply :password (db/acc "admin")))
      (do 
        (log "admin")
        (resp/redirect "/admin"))
      (login "Wrong Password")))
  (GET "/logout" []
    (do 
      (session/clear!)
      (resp/redirect "/login")))
  (GET "/parse" []
    (apply str (map hk/as-hiccup (hk/parse-fragment (c-landing-page-large-text)))))
  (route/not-found "Not Found"))

