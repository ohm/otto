(ns otto.app
  (:require [otto.config        :as config]
            [otto.repositories  :as repos]
            [otto.user          :as user]
            [otto.web           :as web]
            [ring.adapter.jetty :as jetty :refer :all])
  (:gen-class :main true))

(defn -main
  []
  (let [o (config/orgs)
        p (config/port)
        _ (config/user)
        r (repos/make-repositories)]
    (run-jetty (web/make-handler-fn o r) {:port p})))
