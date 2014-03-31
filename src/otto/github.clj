(ns otto.github
  (:require [clojure.core.async :as async :refer [>! <! chan go]]
            [clojure.data.json  :as json]
            [org.httpkit.client :as http]
            [otto.repositories  :as repositories :refer [make-repository]]))

(defn- api-base-url
  [path]
  (format "https://api.github.com/%s" path))

(defn- organization-repositories-url
  [organization]
  (api-base-url (format "orgs/%s/repos" (:name organization))))

(defn- make-http-get-fn
  [_user]
  (fn [url response-fn]
    (http/get url {} response-fn)))

(defn- make-response-fn
  [success-fn]
  (fn [{:keys [body error headers status]}]
    (if (and (nil? error) (= 200 status))
      (let [next-url (->> (:link headers)
                          str
                          (re-find #"<([^\s]+)>;\s?rel=\"next\"")
                          vec
                          last)]
        (success-fn body next-url))))) ;; TODO error

(defn- make-json-response-fn
  [success-fn]
  (make-response-fn (fn [body next-url]
                      (let [json (json/read-str body :key-fn keyword)]
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
