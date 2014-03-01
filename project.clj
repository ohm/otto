(defproject otto "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure     "1.5.1"]
                 [org.clojure/core.typed  "0.2.34"]
                 [compojure               "1.1.3"]
                 [ring/ring-core          "1.2.1"]
                 [ring/ring-jetty-adapter "1.2.1"]]

  :plugins [[lein-typed "0.3.1"]]
  :core.typed {:check [otto.config
                       otto.organization
                       otto.user]}
  :main otto.app)
