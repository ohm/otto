(ns otto.web
  (:require ;[clojure.core.typed :as t :refer :all]
            [compojure.core     :as c]
            [ring.util.response :as r]
            [otto.html          :as html]))

;; (def-alias OrganizationList (clojure.lang.IPersistentVector otto.organization.Organization))

;; (def-alias State (HMap :complete? true
;;                        :mandatory {:organizations OrganizationList}))

;; (ann make-state [OrganizationList -> State])
(defn- make-state
  [o r]
  {:organizations o
   :repositories  r})

;; (ann show-organization [State -> String])
(defn- show-organization
  [s]
  (let [r (:repositories s)]
    (html/organization-view (.items r))))

;; (ann make-handler-fn [OrganizationList -> [Any -> String]])
(defn make-handler-fn
  [o r]
  (let [s (make-state o r)]
    (c/defroutes web-routes
      (c/GET "/" [] (show-organization s)))))
