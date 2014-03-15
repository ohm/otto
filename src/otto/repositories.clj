(ns otto.repositories)

(defn- repository-key
  [organization repository-data]
  (let [o (:name organization)
        n (get repository-data "name")]
    [o n]))

(defprotocol ARepositoryList
  (items  [this organization])
  (update [this organization repository]))

(deftype RepositoryList
  [a]
  ARepositoryList
  (items
    [this organization]
    (filter (fn [[k v]]
              (let [[o n] k]
                (= (:name organization) o))) @a))
  (update
    [this organization repository-data]
    (let [k (repository-key organization repository-data)]
      (dosync (alter a assoc-in [k] repository-data)))
      true))

(defn make-repositories
  []
  (let [repos (ref {})]
    (->RepositoryList repos)))
