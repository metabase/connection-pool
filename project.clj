(defproject metabase/connection-pool "1.0.3"
  :description "Connection pools for JDBC databases. Simple wrapper around C3P0."
  :url "https://github.com/metabase/connection-pool"
  :min-lein-version "2.5.0"

  :license {:name "Eclipse Public License"
            :url "https://raw.githubusercontent.com/metabase/connection-pool/master/LICENSE"}

  :aliases
  {"bikeshed"                  ["with-profile" "+bikeshed" "bikeshed" "--max-line-length" "120"]
   "check-namespace-decls"     ["with-profile" "+check-namespace-decls" "check-namespace-decls"]
   "eastwood"                  ["with-profile" "+eastwood" "eastwood"]
   "docstring-checker"         ["with-profile" "+docstring-checker" "docstring-checker"]
   ;; `lein lint` will run all linters
   "lint"                      ["do" ["eastwood"] ["bikeshed"] ["check-namespace-decls"] ["docstring-checker"]]}

  :dependencies
  [[com.mchange/c3p0 "0.9.5.4"]]

  :profiles
  {:dev
   {:dependencies
    [[org.clojure/clojure "1.10.1"]
     [com.h2database/h2 "1.4.197"]
     [pjstadig/humane-test-output "0.9.0"]]

    :jvm-opts
    ["-Xverify:none"]}

   :eastwood
   {:plugins
    [[jonase/eastwood "0.3.6" :exclusions [org.clojure/clojure]]]

    :add-linters
    [:unused-private-vars
     :unused-namespaces
     :unused-fn-args
     :unused-locals]

    :exclude-linters
    [:deprecations]}

   :docstring-checker
   {:plugins
    [[docstring-checker "1.0.3"]]

    :docstring-checker
    {:exclude [#"test"]}}

   :bikeshed
   {:plugins
    [[lein-bikeshed "0.5.2"
      :exclusions [org.clojure/tools.namespace]]]}

   :check-namespace-decls
   {:plugins               [[lein-check-namespace-decls "1.0.2"]]
    :source-paths          ["test"]
    :check-namespace-decls {:prefix-rewriting true}}}

  :deploy-repositories
  [["clojars"
    {:url           "https://clojars.org/repo"
     :username      :env/clojars_username
     :password      :env/clojars_password
     :sign-releases false}]])
