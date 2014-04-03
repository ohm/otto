(ns otto.web
  (:require [compojure.core               :as compojure]
            [otto.html                    :as html]
            [ring.middleware.content-type :as content-type]
            [ring.middleware.not-modified :as not-modified]
            [ring.middleware.resource     :as resource]))

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
(defn- make-routes-fn
  [state]
  (compojure/defroutes web-routes
    (compojure/GET "/:organization-name" [organization-name]
      (show-organization state organization-name))
    (compojure/GET "/" []
      (show-organization state))))

(defn make-handler-fn
  [organizations repositories]
  (let [state (make-state organizations repositories)]
    (-> (make-routes-fn state)
        (resource/wrap-resource "public")
        (content-type/wrap-content-type)
        (not-modified/wrap-not-modified))))
