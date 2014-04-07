(ns otto.app
  (:require [clojure.tools.logging :as logger]
            [otto.config           :as config]
            [otto.github           :as github]
            [otto.repositories     :as repos]
            [otto.web              :as web]
            [clojure.core.async    :as async :refer [<! go timeout]]
            [ring.adapter.jetty    :as jetty])
  (:gen-class :main true))

(defn- make-repository-update-fn
  [repositories]
  (fn [organization repository]
    (.update repositories organization repository)))

(defn periodically-update
  [organizations user interval update-fn]
  (doseq [o organizations]
    (go (loop []
          (logger/info (format "Updating organization %s" (:name o)))
          (github/fetch-repositories o user #(update-fn o %))
          (<! (timeout interval))
          (recur)))))

(defn -main
  []
  (let [o (config/orgs)
        p (config/port)
        u (config/user)
        i (config/interval)
        r (repos/make-repositories o)]
    (periodically-update o u i (make-repository-update-fn r))
    (jetty/run-jetty (web/make-handler-fn o r) {:port p})))
