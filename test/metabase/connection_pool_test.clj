(ns metabase.connection-pool-test
  (:require [clojure.test :as t]
            [metabase.connection-pool :as connection-pool]))

(def ^:private spec
  {:classname "org.h2.Driver", :subprotocol "h2", :subname "mem:db"})

(t/deftest properties-test
  (t/is (= "acquireIncrement -> 1"
           (->> (connection-pool/connection-pool-spec spec {"acquireIncrement" 1})
                :datasource
                str
                (re-find #"acquireIncrement -> \d")))
        "Options passed in to `connection-pool-spec` should get parsed correctly"))
