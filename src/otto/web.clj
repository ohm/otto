(ns otto.web
  (:require [compojure.core     :as c]
            [ring.util.response :as r]
            [otto.html          :as html]))

(defn- make-state
  [organizations repositories]
  {:organizations organizations
   :repositories  repositories})

(defn- show-organization
  ([state]
   (let [organization-name (:name (first (:organizations state)))]
     (show-organization state organization-name)))
  ([state organization-name]
   (let [organizations (:organizations state)
         repositories  (:repositories  state)]
     (if-let [organization (->> organizations
                                (filter #(= (:name %) organization-name))
                                first)]
       (html/organization-view organizations
                               organization
                               (.items repositories organization))))))

(defn make-handler-fn
  [o r]
  (let [s (make-state o r)]
    (c/defroutes web-routes
      (c/GET "/:organization" [organization]
             (show-organization s organization))
      (c/GET "/" []
             (show-organization s)))))
