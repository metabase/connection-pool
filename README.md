[![Downloads](https://versions.deps.co/metabase/connection-pool/downloads.svg)](https://versions.deps.co/metabase/connection-pool)
[![Dependencies Status](https://versions.deps.co/metabase/connection-pool/status.svg)](https://versions.deps.co/metabase/connection-pool)
[![Circle CI](https://circleci.com/gh/metabase/connection-pool.svg?style=svg)](https://circleci.com/gh/metabase/connection-pool)
[![License](https://img.shields.io/badge/license-Eclipse%20Public%20License-blue.svg)](https://raw.githubusercontent.com/metabase/connection-pool/master/LICENSE)
[![cljdoc badge](https://cljdoc.org/badge/metabase/connection-pool)](https://cljdoc.org/d/metabase/connection-pool/CURRENT)

[![Clojars Project](https://clojars.org/metabase/connection-pool/latest-version.svg)](http://clojars.org/metabase/connection-pool)

### Creating a Connection Pool

You can create a C3P0 connection pool with any `clojure.java.jdbc` connection spec map. (Currently, only maps with `:subname` and `:subprotocol` are supported.) `connection-pool-spec` will return a `clojure.java.jdbc` connection spec you can use directly with JDBC:

```clj
(require '[clojure.java.jdbc :as jdbc]
         '[metabase.connection-pool :as connection-pool])

;;; Create a C3P0 connection pool

(let [pool-spec (connection-pool/connection-pool-spec my-jdbc-spec)]
  (jdbc/query pool-spec ["SELECT *"]))
  ```

(You will almost certainly want to store your pool somewhere, such as in an atom).

### Configuring the connection pool

You can set connection pool options such as size in a `c3p0.properties` file, or by passing them as a map to `connection-pool-spec`:

```clj
(def ^:private connection-pool-properties
  {"maxIdleTime"     (* 3 60 60)
   "minPoolSize"     1
   "initialPoolSize" 1
   "maxPoolSize"     15})
   
(def my-pool-spec 
  (connection-pool/connection-pool-spec my-jdbc-spec connection-pool-properties))
```

See [https://www.mchange.com/projects/c3p0/#configuration_properties](https://www.mchange.com/projects/c3p0/#configuration_properties) for a list of all options.

### Destroying connection pools

`destroy-connection-pool!` will destroy the connection pool you created:

```clj
(connection-pool/destroy-connection-pool! (:datasource pool-spec))
```

Note that due to me making bad decisions that I haven't fixed yet you currently have to pull the DataSource out of the pool spec yourself. (I plan to fix this in the future)

### Legal Stuff

Copyright © 2019 [Metabase, Inc.](https://metabase.com/). This project is licensed under the Eclipse Public License,  same as Clojure.
