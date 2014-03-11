(ns otto.github
  (:require [clojure.core.async :as async :refer [<! >! go timeout]]))

(defn fetch-repositories
  [organization user ch]
  (go
    (<! (timeout (rand-int 100)))
    (>! ch organization)))
