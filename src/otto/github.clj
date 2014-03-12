(ns otto.github
  (:require [clojure.core.async :as async :refer [<! >! chan go timeout]]
            [org.httpkit.client :as http]))

(defn- api-base-url
  [path]
  (format "https://api.github.com/%s" path))

(defn- organization-repositories-url
  [organization]
  (api-base-url (format "orgs/%s/repos" (:name organization))))

(defn fetch-repositories
  [organization user]
  (let [results (chan)
        url     (organization-repositories-url organization)]
    (http/get url #(go (>! results %)))
    results))
