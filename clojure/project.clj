(defproject note "1.0.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [ring "1.0.2"]
                 [borneo "0.3.0"]]
  :main note.core
  :plugins [[lein-ring "0.6.3"]]
  :ring {:handler note.core/handler}
  :jvm-opts ["-Xdebug"
             "-Xrunjdwp:transport=dt_socket,address=4000,server=y,suspend=n"])
