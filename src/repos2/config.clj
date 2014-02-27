(ns repos2.config
  (:require [clojure.string     :as s]
            [clojure.core.typed :as t]))

(t/ann env [String -> String])
(defn- env
  [k]
  (if-let [v (System/getenv k)]
    v
    (throw (Exception. (format "Missing environment variable %s." k)))))

(t/ann port [-> Integer])
(defn port
  []
  (Integer/parseInt (env "PORT")))

(t/ann orgs [-> (clojure.lang.IPersistentVector String)])
(defn orgs
  []
  (let [v (env "ORGANIZATIONS")]
    (s/split v #";")))

(t/ann user [-> String])
(defn user
  []
  (env "USER"))
