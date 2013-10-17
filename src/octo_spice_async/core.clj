(ns octo-spice-async.core
  (:import [java.util.concurrent CountDownLatch])
  (:require [clj-http.client :as http]
            [clojure.string :refer [split-lines join split]])
  (:gen-class :main true))

(defn get-urls-from-file
  [file-name]
  (->> file-name
       slurp
       split-lines
       vec))

(defn add-content
  [words content]
  (swap! content concat words))

(defn fetch-and-parse-url
  [latch content url]
  (let [words (-> url
                  slurp
                  (split #"\s+"))]
    (add-content words content)  
    (.countDown latch)))

(defn retrieve-content-of-url
  [latch content url]
  (.start (Thread. 
           (fn [] (fetch-and-parse-url latch
                                       content
                                       url)))))

(defn retrieve-content-of-urls
  [latch content urls]
  (doall (pmap #(retrieve-content-of-url latch content %) urls)))

(defn done
  [latch content]
  (.await latch)
  (spit "count.txt" (doall (frequencies @content))))

(defn run
  [file-name]
  (let [urls (get-urls-from-file file-name)
        latch (CountDownLatch. (count urls))
        content (atom [])]
    (retrieve-content-of-urls latch content urls)
    (done latch content)))

(defn -main
  "The application's main function"
  [& args]
  (time (run (first args))))
