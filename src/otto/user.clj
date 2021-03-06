(ns otto.user
  (:require [clojure.string     :as s]
            [clojure.core.typed :as t]))

(t/ann-record User [resource :- String
                    name     :- String
                    token    :- String])
(defrecord User [resource name token])

(t/ann parse-user [String -> User])
(defn parse-user
  [s]
  (let [[r n t] (s/split s #"\:")]
    (assert r)
    (assert n)
    (assert t)
    (->User r n t)))
