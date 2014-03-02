(ns otto.web
  (:require [clojure.core.typed :as t :refer :all]
            [compojure.core     :as c]
            [ring.util.response :as r]))

(def-alias State (HMap :complete? true :mandatory {:organizations (clojure.lang.IPersistentVector otto.organization.Organization)}))

(ann make-state [(clojure.lang.IPersistentVector otto.organization.Organization) -> State])
(defn- make-state
  [o]
  {:organizations o})

(ann show-organization [State -> String])
(defn- show-organization
  [s]
  (let [o (first (:organizations s))]
    (format "%s" (:name o))))

(defn make-handler-fn
  [o]
  (let [s (make-state o)]
    (c/defroutes web-routes
      (c/GET "/" [] (show-organization s)))))
