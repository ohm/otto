(ns otto.web
  (:require [ring.util.response :as r]))

(defn- make-state
  [o]
  {})

(defn make-handler-fn
  [o]
  (let [s (make-state o)]
    (fn [request]
      (-> (r/response (format "Organizations: %s" o))
          (r/content-type "text/plain")))))
