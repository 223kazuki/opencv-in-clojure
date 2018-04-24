(ns open-cv.core
  (:require [clojure.java.io :as io]
            [clojure.string :as str])
  (:import [org.opencv.highgui Highgui]
           [org.opencv.core Core Mat CvType Point Scalar]
           [org.opencv.imgproc Imgproc])
  (:gen-class))

(clojure.lang.RT/loadLibrary Core/NATIVE_LIBRARY_NAME)

(defn template-match [img tmpl]
  (let [result (Mat. (inc (- (.rows img) (.rows tmpl)))
                     (inc (- (.cols img) (.cols tmpl)))
                     CvType/CV_32FC1)]
    (Imgproc/matchTemplate img tmpl result Imgproc/TM_CCOEFF_NORMED)
    (Imgproc/threshold result result 0.8 1.0 Imgproc/THRESH_TOZERO)
    (let [[x y width height score]
          (->> (for [x (range (.cols result)) y (range (.rows result))]
                 (when (pos? (first (.get result y x)))
                   [x y (.cols tmpl) (.rows tmpl) (first (.get result y x))]))
               (filter identity)
               (sort-by last)
               last)]
      (println "[x y width height score] =" [x y width height score])
      (Core/rectangle img
                      (Point. x y)
                      (Point. (+ x width) (+ y height))
                      (Scalar. 0 0 255))
      (Highgui/imwrite "out.jpg" img))))

(defn -main
  [& args]
  (if-not (== 2 (count args))
    (do (println "Usage: $0 [image name] [template name]")
        (System/exit 1))
    (let [img-name (first args)
          tmpl-name (second args)
          img-file (io/file img-name)
          tmpl-file (io/file tmpl-name)]
      (cond
        (not (.exists img-file))
        (do (println "Image file does not exist.") (System/exit 1))

        (not (.exists tmpl-file))
        (do (println "Template image file does not exist.") (System/exit 1))

        :else
        (let [img (Highgui/imread (.getAbsolutePath img-file) Highgui/CV_LOAD_IMAGE_COLOR)
              tmpl (Highgui/imread (.getAbsolutePath tmpl-file) Highgui/CV_LOAD_IMAGE_COLOR)]
          (template-match img tmpl))))))
