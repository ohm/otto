(ns otto.github
  (:require [clojure.core.async    :as async :refer [>! <! chan go]]
            [clojure.data.json     :as json]
            [clojure.tools.logging :as logger]
            [org.httpkit.client    :as http]
            [otto.repositories     :as repositories :refer [make-repository]]))

(defn- api-base-url
  [path]
  (format "https://api.github.com/%s" path))

(defn- organization-repositories-url
  [organization]
  (api-base-url (format "orgs/%s/repos?per_page=%d" (:name organization) 100)))

(defn- make-http-get-fn
  [user]
  (fn [url response-fn]
    (logger/debug (format "Fetching %s" url))
    (http/get url {:basic-auth [(:name user) (:token user)]} response-fn)))

(defn- parse-next-url
  [link]
  (->> (str link)
       (re-find #"<([^\s]+)>;\s?rel=\"next\"")
       vec
       last))

(defn- parse-json-date
  [date]
  (if-not (clojure.string/blank? date)
    (.parse (java.text.SimpleDateFormat. "yyyy-MM-dd'T'HH:mm:ss'Z'") date)))

(defn- parse-json-val
  [k v]
  (if (= k :pushed_at) (parse-json-date v) v))

(defn- make-response-fn
  [success-fn]
  (fn [{:keys [body error headers status]}]
    (if (and (nil? error) (= 200 status))
      (let [next-url (parse-next-url (:link headers))]
        (success-fn body next-url))))) ;; TODO error

(defn- make-json-response-fn
  [success-fn]
  (make-response-fn (fn [body next-url]
                      (let [json (json/read-str body :key-fn   keyword
                                                     :value-fn parse-json-val)]
                        (success-fn json next-url)))))

(defn- make-json-collection-fn
  [request-chan item-fn]
  (make-json-response-fn (fn [items next-url]
                           (go (if-not (nil? next-url)
                                 (>! request-chan next-url))
                               (doseq [i items] (item-fn i))))))

(defn- make-repositories-fn
  [request-chan item-fn]
  (make-json-collection-fn request-chan (fn [item]
                                          (let [r (make-repository item)]
                                            (item-fn r)))))

(defn fetch-repositories
  [organization user repository-fn]
  (let [http-get-fn  (make-http-get-fn user)
        request-chan (chan 1)
        response-fn  (make-repositories-fn request-chan repository-fn)]
    (go (>! request-chan (organization-repositories-url organization))
        (loop []
          (if-let [url (<! request-chan)]
            (do (http-get-fn url response-fn)
                (recur)))))))
