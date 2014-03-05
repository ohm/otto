(ns otto.control
  (:require [clojure.core.async :as async :refer [go <! timeout]]))

(defn update-repositories
  [orgs user _repos]
  (doseq [o orgs]
    (println (format "Updating repositories for %s using user %s" o user))))

(defn periodically-update
  [orgs user interval repos]
  (go (loop []
        (<! (timeout interval))
        (update-repositories orgs user repos)
        (recur))))
