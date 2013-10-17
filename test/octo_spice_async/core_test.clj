(ns octo-spice-async.core-test
  (:use clojure.test
        octo-spice-async.core))

(def urls
  ["http://www.textfiles.com/etext/FICTION/2000010.txt"])

(deftest fetch-url-test
  (testing "Fetching a url..."
    (println (fetch-url (first urls))) 
    (is  (not= nil
               (fetch-url (first urls))))))

(deftest run-test
  (let [result (run "/home/mark/projects/octo-spice-async/resources/urls.txt")]
    (println result)
    (is (= nil
           result))))
