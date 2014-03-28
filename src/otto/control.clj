(ns otto.control
  (:require [clojure.core.async :as async :refer [<!! alts!! chan thread timeout]]
            [otto.github :as github]))

(def update-repositories-timeout 10000)

(defn- make-update-repository-fn
  [repositories]
  println)

(defn- update-repositories
  [organization user repo-fn]
  (let [r (github/fetch-organization-repositories organization user repo-fn)
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
