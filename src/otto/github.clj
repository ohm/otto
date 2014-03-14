(ns otto.github
  (:require [clojure.core.async :as async :refer [close! chan go onto-chan]]
            [clojure.data.json  :as json]
            [org.httpkit.client :as http]))

(defn- api-base-url
  [path]
  (format "https://api.github.com/%s" path))

(defn- organization-repositories-url
  [organization]
  (api-base-url (format "orgs/%s/repos" (:name organization))))

(def repository-attributes
  ["description"
   "fork"
   "html_url"
   "language"
   "name"
   "private"
   "pushed_at"])

(defn- parse-repositories
  [json]
  (let [rs (json/read-str json)]
    (map #(select-keys % repository-attributes) rs)))

(defn fetch-repositories
  [organization user]
  (let [ch  (chan)
        url (organization-repositories-url organization)]
    (http/get url (fn [{:keys [body error status]}]
                    (go (if (or error (not= 200 status))
                          (do (println body)
                              (close! ch))
                          (onto-chan ch (parse-repositories body))))))
     ch))
