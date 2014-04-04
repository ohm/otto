(ns otto.repositories)

(defprotocol ARepositoryList
  (items  [this organization])
  (update [this organization repository]))

(deftype RepositoryList
  [a]
  ARepositoryList
  (items [this organization]
    (get @a organization))
  (update [this organization repository]
    (let [k (:name repository)
          t (System/currentTimeMillis)
          v (assoc repository :_timestamp t)]
      (dosync (alter a assoc-in [organization k] v)))))

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
  [organizations]
  (let [repos (repeatedly (count organizations) sorted-map)
        state (ref (zipmap organizations repos))]
    (->RepositoryList state)))
