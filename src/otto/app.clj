(ns otto.app
  (:require [otto.config        :as config]
            [otto.github        :as github]
            [otto.repositories  :as repos]
            [otto.user          :as user]
            [otto.web           :as web]
            [clojure.core.async :as async :refer [<!! thread timeout]]
            [ring.adapter.jetty :as jetty :refer :all])
  (:gen-class :main true))

(defn periodically-update
  [organizations user repositories interval]
  (doseq [o organizations]
    (thread (loop []
              (github/fetch-repositories o user #(.update repositories o %))
              (<!! (timeout interval))
              (recur)))))

(defn -main
  []
  (let [o (config/orgs)
        p (config/port)
        u (config/user)
        i (config/interval)
        r (repos/make-repositories)]
    (periodically-update o u r i)
    (run-jetty (web/make-handler-fn o r) {:port p})))
