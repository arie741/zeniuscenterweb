(ns zeniuscenterweb.db
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.java.jdbc :as jdbc]
            [clj-postgresql.core :as pg]
            [clj-time.local :as tl]
            [clj-time.format :as tf]))

(def dbase (pg/pool :host "localhost:5432"
                  :user "zeniuscenterweb"
                  :dbname "zeniuscenterweb"
                  :password "admin2018"))

(defn scomp [compo]
      (jdbc/query dbase [(str "select * from components where component = '" compo "'")]))

(defn c-update [compo content]
      (jdbc/update! dbase :components {:content content} ["component = ?" compo]))