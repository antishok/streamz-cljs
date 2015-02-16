(defproject streamz "0.1.0-SNAPSHOT"
  :description ""

  :source-paths ["src"]

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2814"]
                 [figwheel "0.2.3-SNAPSHOT"]
                 [reagent "0.5.0-alpha3"]
                 [reagent/reagent-cursor "0.1.2"]
                 [reagent-forms "0.4.3"]]

  :plugins [[lein-cljsbuild "1.0.4"]
            [lein-figwheel "0.2.3-SNAPSHOT"]]

  :hooks [leiningen.cljsbuild]

  :clean-targets ^{:protect false} ["resources/public/js"]

  :cljsbuild {:builds {:app {:source-paths ["src"]
                             :compiler {:main "streamz.core"
                                        :output-to  "resources/public/js/app.js"
                                        :output-dir "resources/public/js/out"
                                        :asset-path "js/out"
                                        :source-map true}}}}

  :profiles {:dev {:cljsbuild
                   {:builds {:app {:compiler {:optimizations :none
                                              :pretty-print true}}}}}

             :prod {:cljsbuild
                    {:builds {:app {:compiler {:optimizations :advanced
                                               :elide-asserts true
                                               :pretty-print false}}}}}})
