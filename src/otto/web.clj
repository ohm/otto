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
  ([state]
   (let [organization-name (:name (first (:organizations state)))]
     (show-organization state organization-name)))
   ([state organization-name]
     (if-let [organization (->> (:organizations state)
                                (filter #(= (:name %) organization-name))
                                first)]
       (html/organization-view (.items (:repositories state) organization))))) ;; TODO else

;; (ann make-handler-fn [OrganizationList -> [Any -> String]])
(defn make-handler-fn
  [o r]
  (let [s (make-state o r)]
    (c/defroutes web-routes
      (c/GET "/:organization" [organization]
             (show-organization s organization))
      (c/GET "/" []
             (show-organization s)))))
