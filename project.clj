(defproject couchmail "0.0.1-SNAPSHOT"
  :description "dump email into couchdb"
  :repositories { "java.net" "http://download.java.net/maven/2" }
  :aot [couchmail]
  :dependencies [[org.clojure/clojure "1.2.0"]
                 [clj-http "0.1.1"]
                 ;v Not a dev depend since we require nailgun to run. Should use standard nailgun but don't want
                 ;v to have to have code differences between dev and release.
                 [vimclojure/server "2.2.0-SNAPSHOT"]
                 [commons-codec "1.4"]
                 [javax.mail/mail "1.4.3"]
                 [org.clojure/clojure-contrib "1.2.0"]])
