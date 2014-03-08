(ns otto.control
  (:require [clojure.core.async :as async :refer [<! alts!! chan go timeout]]
            [otto.github :as github]))

(defn- update-repository
  [repos r]
  (println r)
  (not (nil? r)))

(defn- update-repositories
  [orgs user update-fn]
  (let [r (chan)
        t (timeout 1000)]
    (doseq [o orgs]
      (github/fetch-repositories o user r))
    (loop []
      (if (update-fn (first (alts!! [r t])))
        (recur)))))

(defn periodically-update
  [orgs user interval repos]
  (let [f (fn [r] (update-repository repos r))]
    (go (loop []
          (<! (timeout interval))
          (update-repositories orgs user f)
          (recur)))))
