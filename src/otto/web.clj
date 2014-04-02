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
     (if-let [organization (->> (:organizations state)
                                (filter #(= (:name %) organization-name))
                                first)]
       (html/organization-view (.items (:repositories state) organization))))) ;; TODO else

(defn make-handler-fn
  [o r]
  (let [s (make-state o r)]
    (c/defroutes web-routes
      (c/GET "/:organization" [organization]
             (show-organization s organization))
      (c/GET "/" []
             (show-organization s)))))
