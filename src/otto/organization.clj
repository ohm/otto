(ns otto.organization
  (:require [clojure.string     :as s]
            [clojure.core.typed :as t]))

(t/ann-record Organization [resource :- String
                            name     :- String])
(defrecord Organization [resource name])

(t/ann parse-organization [String -> Organization])
(defn parse-organization
  [o]
  (let [[r n] (s/split o #"\:")]
    (assert r)
    (assert n)
    (->Organization r n)))
