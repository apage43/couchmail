(ns couchmail
  (:require [clojure.contrib.io :as io])
  (:require [clj-http.client :as http])
  (:use clojure.contrib.json)
  (:import vimclojure.nailgun.NGContext)
  (:import org.apache.commons.codec.binary.Base64)
  (:import javax.mail.internet.MimeMessage)
  (:import javax.mail.internet.MimeMultipart)
  (:import javax.mail.Message$RecipientType)
  (:import javax.mail.Session))
; Should really use nailgun that doesn't come from vimclojure, but hey, I'm already running this copy for development.
; TL;DR: Clean this up later.
(gen-class 
  :name couchmail.Store
  :methods [#^{:static true} [nailMain [vimclojure.nailgun.NGContext] void]]
  :prefix Store-)

; I like this order for select-keys since its nicer with map/partial.
; is there a better way to do this?
(defn only [keyseq data] (select-keys data keyseq))

(def TO Message$RecipientType/TO)
(def CC Message$RecipientType/CC)
(def BCC Message$RecipientType/BCC)

(defn multipart? [p] (= MimeMultipart (.getClass (:content p))))
(defn attachment? [p] (= "attachment" (:disposition p)))

(defn parts [multipart]
  (map #(bean (.getBodyPart multipart %)) (range (.getCount multipart))))

(defn headers-map [en]
  (reduce conj {} (map (fn [h] [(.getName h) (.getValue h)])
                       (enumeration-seq en))))

(defn addresses-seq [adarray]
  (map (partial only '(:address :personal))
       (map bean (seq adarray))))

(defn simplify-part [part]
  (merge (only '(:size :contentType :disposition) part)
         {:content (if (= (.getClass (:content part)) String) (:content part)
                     (String. (io/to-byte-array (:inputStream part))))
          :headers (headers-map (:allHeaders part))}))

(defn encode-attachment [part]
  [(:fileName part) {:content_type (:contentType part)
                     :data (String. (Base64/encodeBase64 (io/to-byte-array (:inputStream part))))}])

(defn simplify-message [msg]
  (let [bmsg (bean msg)
        content (if (multipart? bmsg) (parts (:content bmsg)) (list bmsg))
        mailparts (filter (fn [p] (and (not (attachment? p)) (not (multipart? p)))) content)
        subparts (mapcat #(parts (:content %)) (filter multipart? content))
        attachments (filter #(attachment? %) content)]
    (merge (only '(:subject :messageID) bmsg)
      {:headers (headers-map (:allHeaders bmsg))
       :_attachments (reduce conj {} (map encode-attachment attachments))
       :parts (map simplify-part (concat mailparts subparts))
       :to (addresses-seq (.getRecipients msg TO))
       :cc (addresses-seq (.getRecipients msg CC))
       :bcc (addresses-seq (.getRecipients msg BCC))
       :from  (addresses-seq (:from bmsg))})))

(defn create-message [instream]
  (MimeMessage. (Session/getInstance (java.util.Properties.)) instream))

(defn Store-nailMain [ctx]
  (let [args (seq (.getArgs ctx))
        dburl (if (= 1 (count args)) (first args) "http://127.0.0.1:5984/mail")]
    (http/post "http://127.0.0.1:5984/mail" {:body (json-str (simplify-message (create-message (.in ctx)))) :content-type :json})
    (println "OK")))
