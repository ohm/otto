(ns otto.config
  (:require [clojure.string     :as s]
            [clojure.core.typed :as t]
            [otto.organization  :as o]
            [otto.user          :as u]))

(t/def-alias OrganizationList (clojure.lang.IPersistentVector otto.organization.Organization))

(t/ann env [String -> String])
(defn- env
  [k]
  (let [v (System/getenv k)]
    (assert v)
    v))

(t/ann env-integer [String -> Integer])
(defn- env-integer
  [k]
  (Integer/parseInt (env k)))

(t/ann port [-> Integer])
(defn port
  []
  (env-integer "PORT"))

(t/ann orgs [-> OrganizationList])
(defn orgs
  []
  (let [v (env "ORGANIZATIONS")
        o (s/split v #"\;")]
    (vec (map o/parse-organization o))))

(t/ann user [-> otto.user.User])
(defn user
  []
  (u/parse-user (env "USER")))

(t/ann interval [-> Integer])
(defn interval
  []
  (env-integer "INTERVAL"))
