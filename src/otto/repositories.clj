(ns otto.repositories)

(defprotocol ARepositoryList
  (items  [this])
  (update [this organization repository]))

(deftype RepositoryList
  [a]
  ARepositoryList
  (items
    [this]
    @a)
  (update
    [this organization repository-data]
    (let [k (str (:name organization) ":" (get repository-data "name"))]
      (dosync (alter a assoc-in [k] repository-data)))
      true))

(defn make-repositories
  []
  (let [repos (ref {})]
    (->RepositoryList repos)))
