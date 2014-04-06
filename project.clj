(defproject otto "1.0.0"
  :aot :all
  :license "Mozilla Public License Version 2.0"
  :dependencies [[org.clojure/clojure       "1.6.0"]
                 [org.clojure/core.async    "0.1.267.0-0d7780-alpha"]
                 [org.clojure/core.typed    "0.2.34"]
                 [org.clojure/data.json     "0.2.4"]
                 [org.clojure/tools.logging "0.2.6"]
                 [compojure                 "1.1.3"]
                 [hiccup                    "1.0.5"]
                 [http-kit                  "2.1.16"]
                 [log4j                     "1.2.15" :exclusions [javax.mail/mail
                                                                  javax.jms/jms
                                                                  com.sun.jdmk/jmxtools
                                                                  com.sun.jmx/jmxri]]
                 [org.slf4j/slf4j-log4j12   "1.6.6"]
                 [ring/ring-core            "1.2.1"]
                 [ring/ring-jetty-adapter   "1.2.1"]]
  :plugins [[lein-kibit "0.0.8"]
            [lein-typed "0.3.1"]]
  :core.typed {:check [otto.config
                       otto.organization
                       otto.user]}
  :jar-name "otto.jar"
  :uberjar-name "otto-standalone.jar"
  :main otto.app)
