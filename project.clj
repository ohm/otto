(defproject otto "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure     "1.5.1"]
                 [org.clojure/core.async  "0.1.267.0-0d7780-alpha"]
                 [org.clojure/core.typed  "0.2.34"]
                 [org.clojure/data.json   "0.2.4"]
                 [compojure               "1.1.3"]
                 [hiccup                  "1.0.5"]
                 [http-kit                "2.1.16"]
                 [ring/ring-core          "1.2.1"]
                 [ring/ring-jetty-adapter "1.2.1"]]
  :plugins [[lein-kibit "0.0.8"]
            [lein-typed "0.3.1"]]
  :core.typed {:check [otto.config
                       otto.organization
                       otto.user]}
  :main otto.app)
