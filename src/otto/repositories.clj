(ns otto.repositories)

(defn- repository-key
  [organization repository-data]
  (let [o (:name organization)
        n (get repository-data "name")]
    [o n]))

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
    (let [k (repository-key organization repository-data)]
      (dosync (alter a assoc-in [k] repository-data)))
      true))

(defn make-repositories
  []
  (let [repos (ref {})]
    (->RepositoryList repos)))
