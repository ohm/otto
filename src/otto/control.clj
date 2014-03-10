(ns otto.control
  (:require [clojure.core.async :as async :refer [<! alts!! chan close! go timeout]]
            [otto.github :as github]))

(defn- update-repository
  [repos r]
  (if (nil? r)
    false
    (do (println r)
        true)))

(defn- update-repositories
  [org user repo-fn]
  (let [r (chan)
        t (timeout 1000)]
    (github/fetch-repositories org user r)
    (loop []
      (if (apply repo-fn (alts!! [r t]))
        (recur)))))

(defn periodically-update
  [orgs user interval repos]
  (doseq [o orgs]
    (go (loop []
          (<! (timeout interval))
          (update-repositories o user (fn [r _]
                                        (update-repository repos r)))
          (recur)))))
