(defproject couchmail "0.0.1-SNAPSHOT"
  :description "dump email into couchdb"
  :dev-dependencies [[vimclojure/server "2.2.0-SNAPSHOT"]]
  :repositories { "java.net" "http://download.java.net/maven/2" }
  :dependencies [[org.clojure/clojure "1.2.0"]
                 [clj-http "0.1.1"]
                 [commons-codec "1.4"]
                 [javax.mail/mail "1.4.3"]
                 [org.clojure/clojure-contrib "1.2.0"]])
