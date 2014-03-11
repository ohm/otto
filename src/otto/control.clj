(ns otto.control
  (:require [clojure.core.async :as async :refer [<! alts!! chan close! go timeout]]
            [otto.github :as github]))

(defn- make-update-repository-fn
  [repositories]
  (fn [r]
    (if (nil? r)
      false
      (do (println r)
          true))))

(defn- update-repositories
  [organization user repo-fn]
  (let [r (chan)
        t (timeout 1000)]
    (github/fetch-repositories organization user r)
    (loop []
      (if (repo-fn (first (alts!! [r t])))
        (recur)))))

(defn periodically-update
  [organizations user repositories interval]
  (let [repo-fn (make-update-repository-fn repositories)]
    (doseq [o organizations]
      (go (loop []
            (<! (timeout interval))
            (update-repositories o user repo-fn)
            (recur))))))
