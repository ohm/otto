(ns otto.repositories)

(defn- repository-key
  [organization repository-data]
  (let [o (:name organization)
        n (:name repository-data)]
    [o n]))

(defprotocol ARepositoryList
  (items  [this organization])
  (update [this organization repository]))

(deftype RepositoryList
  [a]
  ARepositoryList
  (items [this organization]
    (filter (fn [[k v]]
              (let [[o n] k]
                (= (:name organization) o))) (:repositories @a)))
  (update [this organization repository-data]
    (let [k (repository-key organization repository-data)
          t (System/currentTimeMillis)
          v (assoc repository-data :_timestamp t)]
      (dosync (alter a assoc-in [:repositories k] v)))))

(defn make-repository
  [attributes]
  (select-keys attributes [:description
                           :fork
                           :html_url
                           :language
                           :name
                           :private
                           :pushed_at]))

(defn make-repositories
  []
  (let [repos (ref {:repositories (sorted-map)})]
    (->RepositoryList repos)))
