(defproject open-cv "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [opencv/opencv "2.4.13"]
                 [opencv/opencv-native "2.4.13"]]
  :main ^:skip-aot open-cv.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
