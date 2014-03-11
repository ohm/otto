(ns otto.app
  (:require [otto.config        :as config]
            [otto.control       :as control]
            [otto.repositories  :as repos]
            [otto.user          :as user]
            [otto.web           :as web]
            [ring.adapter.jetty :as jetty :refer :all])
  (:gen-class :main true))

(defn -main
  []
  (let [o (config/orgs)
        p (config/port)
        u (config/user)
        i (config/interval)
        r (repos/make-repositories)]
    (control/periodically-update o u r i)
    (run-jetty (web/make-handler-fn o r) {:port p})))
