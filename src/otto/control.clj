(ns otto.control
  (:require [clojure.core.async :as async :refer [<! alts!! chan close! go timeout]]
            [otto.github :as github]))

(defn- update-repository
  [repos v k]
  (println v k)
  (not (nil? v)))

(defn- update-repositories
  [orgs user update-fn]
  (let [r (chan)
        t (timeout 1000)]
    (doseq [o orgs]
      (github/fetch-repositories o user r))
    (loop []
      (if (update-fn (alts!! [r t]))
        (recur)
        (do (close! r)
            (close! t))))))

(defn periodically-update
  [orgs user interval repos]
  (let [update-fn #(apply update-repository repos %)]
    (go (loop []
          (<! (timeout interval))
          (update-repositories orgs user update-fn)
          (recur)))))
