(ns repos2.app
  (:require [repos2.config      :as config]
            [repos2.user        :as user]
            [ring.adapter.jetty :as jetty :refer :all])
  (:gen-class :main true))

(defn make-handler-fn
  [o u]
  (fn [request]
    {:status  200
     :headers {"Content-Type" "text/html"}
     :body    (format "User: %s - Organizations: %s" o u)}))

(defn -main
  []
  (let [o (config/orgs)
        p (config/port)
        u (config/user)]
    (run-jetty (make-handler-fn o u) {:port p})))