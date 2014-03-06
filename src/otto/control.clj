(ns otto.control
  (:require [clojure.core.async :as async :refer [<! alts!! chan go timeout]]))

(defn update-repositories
  [orgs user _repos]
  (let [r (chan)
        t (timeout 1000)]
    (println (alts!! [r t]))))

(defn periodically-update
  [orgs user interval repos]
  (go (loop []
        (<! (timeout interval))
        (update-repositories orgs user repos)
        (recur))))
