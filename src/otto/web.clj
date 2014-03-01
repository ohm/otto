(ns otto.web
  (:require [compojure.core     :as c]
            [ring.util.response :as r]))

(defn- make-state
  [o]
  {:organizations o})

(defn- show-organization
  [s]
  (let [o (first (:organizations s))]
    (format "%s" (:name o))))

(defn make-handler-fn
  [o]
  (let [s (make-state o)]
    (c/defroutes main-routes
      (c/GET "/" [] (show-organization s)))))
