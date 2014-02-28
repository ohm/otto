(ns repos2.config
  (:require [clojure.string      :as s]
            [clojure.core.typed  :as t]
            [repos2.organization :as o]
            [repos2.user         :as u]))

(t/ann env [String -> String])
(defn- env
  [k]
  (let [v (System/getenv k)]
    (assert v)
    v))

(t/ann port [-> Integer])
(defn port
  []
  (Integer/parseInt (env "PORT")))

(t/ann orgs [-> (clojure.lang.IPersistentVector repos2.organization.Organization)])
(defn orgs
  []
  (let [v (env "ORGANIZATIONS")
        o (s/split v #"\;")]
    (into [] (map o/parse-organization o))))

(t/ann user [-> repos2.user.User])
(defn user
  []
  (u/parse-user (env "USER")))
