(ns otto.repositories)

(defprotocol ARepositoryList
  (items [this]))

(deftype RepositoryList
  [a]
  ARepositoryList
  (items [this] a))

(defn make-repositories
  []
  (->RepositoryList {}))
