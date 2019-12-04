(ns metabase.connection-pool-test
  (:require [clojure.test :refer :all]
            [metabase.connection-pool :as connection-pool]))

(def ^:private spec
  {:classname "org.h2.Driver", :subprotocol "h2", :subname "mem:db"})

(deftest properties-test
  (testing "Options passed in to `connection-pool-spec` should get parsed correctly"
    (let [description (-> (connection-pool/connection-pool-spec spec {"acquireIncrement"        1
                                                                      "testConnectionOnCheckin" true})
                          :datasource
                          str)]
      (is (= "acquireIncrement -> 1"
             (re-find #"acquireIncrement -> \d" description))
          "numeric options should get converted correctly")
      (is (= "testConnectionOnCheckin -> true"
             (re-find #"testConnectionOnCheckin -> \w+" description))
          "boolean options should get converted correctly"))))

(deftest map->properties-test
  (testing "Properties should be converted to strings"
    ;; Properties are equality-comparable to maps
    (is (= {"A" "true", "B" "false", "C" "100"}
           (connection-pool/map->properties {:A "true", "B" false, "C" 100})))))

(defrecord ^:private FakeConnection [url props]
  java.sql.Connection)

(defrecord ^:private FakeDriver []
  java.sql.Driver
  (connect [_ url props]
    (FakeConnection. url props)))

(defn- proxy-data-source ^javax.sql.DataSource [& args]
 (apply #'connection-pool/proxy-data-source args))

(deftest proxy-data-source-test
  (testing "Make sure we can create a data source with an explicit driver instance"
    (is (= (FakeConnection. "jdbc:my-fake-db:localhost" nil)
           (.getConnection (proxy-data-source (FakeDriver.) "jdbc:my-fake-db:localhost" nil)))))

  (testing "Make sure username/password are set when using the 3-arg getConnection method"
    (doseq [props [(java.util.Properties.) nil (doto (java.util.Properties.)
                                                 (.setProperty "password" "abc")
                                                 (.setProperty "user" "cam-2"))]]
      (testing (format "with initial properties = %s" (pr-str props))
        (is (= (FakeConnection. "jdbc:my-fake-db:localhost" {"password" "passw0rd", "user" "cam"})
               (.getConnection (proxy-data-source (FakeDriver.) "jdbc:my-fake-db:localhost" props)
                               "cam"
                               "passw0rd"))))))

  (testing "passing nil username/password to 3-arg getConnection method should existing props"
    (let [props (doto (java.util.Properties.)
                  (.setProperty "password" "abc")
                  (.setProperty "user" "cam-2"))]
      (is (= (FakeConnection. "jdbc:my-fake-db:localhost" {})
             (.getConnection (proxy-data-source (FakeDriver.) "jdbc:my-fake-db:localhost" props)
                             nil
                             nil))))))
