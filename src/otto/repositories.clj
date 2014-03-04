(ns otto.repositories)

(defprotocol ARepositoryList
  (items [this]))

(deftype RepositoryList
  [a]
  ARepositoryList
  (items [this] @a))

(defn make-repositories
  []
  (let [repos (ref {})]
    (->RepositoryList repos)))
