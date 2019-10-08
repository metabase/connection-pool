(ns metabase.connection-pool-test
  (:require [clojure.test :as t]
            [metabase.connection-pool :as connection-pool]))

(def ^:private spec
  {:classname "org.h2.Driver", :subprotocol "h2", :subname "mem:db"})

(t/deftest properties-test
  (t/testing "Options passed in to `connection-pool-spec` should get parsed correctly"
    (let [description (-> (connection-pool/connection-pool-spec spec {"acquireIncrement"        1
                                                                      "testConnectionOnCheckin" true})
                          :datasource
                          str)]
      (t/is (= "acquireIncrement -> 1"
               (re-find #"acquireIncrement -> \d" description))
            "numeric options should get converted correctly")
      (t/is (= "testConnectionOnCheckin -> true"
               (re-find #"testConnectionOnCheckin -> \w+" description))
            "boolean options should get converted correctly"))))
