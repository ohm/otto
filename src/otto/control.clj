(ns otto.control
  (:require [clojure.core.async :as async :refer [<!! alts!! chan thread timeout]]
            [otto.github :as github]))

(def update-repositories-timeout 10000)

(defn- make-update-repository-fn
  [repositories]
  (fn [o r]
    (if (nil? r)
      false
      (.update repositories o r))))

(defn- update-repositories
  [organization user repo-fn]
  (let [r (github/fetch-repositories organization user)
        t (timeout update-repositories-timeout)]
    (loop []
      (if (repo-fn organization (first (alts!! [r t])))
        (recur)))))

(defn periodically-update
  [organizations user repositories interval]
  (let [repo-fn (make-update-repository-fn repositories)]
    (doseq [o organizations]
      (thread (loop []
                (update-repositories o user repo-fn)
                (<!! (timeout interval))
                (recur))))))
