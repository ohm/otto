(ns otto.github
  (:require [clojure.core.async :as async :refer [<! >! go timeout]]))

(defn- api-base-url
  [path]
  (format "https://api.github.com/%s" path))

(defn- organization-repositories-url
  [organization]
  (api-base-url (format "orgs/%s/repos" (:name organization))))

(defn fetch-repositories
  [organization user ch]
  (let [url (organization-repositories-url organization)]
    (go
      (<! (timeout (rand-int 100)))
      (>! ch url))))
