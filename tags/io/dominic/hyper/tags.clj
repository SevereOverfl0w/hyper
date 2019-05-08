(ns io.dominic.hyper.tags
  (:require
    [clojure.java.io :as io]
    [clojure.string :as string]
    [cheshire.core :as json]))

(def svg-tags (io/resource "svg-tags/index.json"))
(def html-tags (io/resource "html-tags/html-tags.json"))
(def html-tags-void (io/resource "html-tags/html-tags-void.json"))
(def mathml-tags (io/resource "mathml-tags/index.json"))

(defn- output-deftag
  [tag]
  (list 'deftag (case tag
                  "true" (symbol "true'")
                  "false" (symbol "false'")
                  (symbol tag))))

(defn- all-tags
  []
  (mapcat (comp json/parse-stream io/reader)
          [svg-tags html-tags html-tags-void mathml-tags]))

(defn- update-template
  [file contents]
  (spit file
        (string/replace
          (slurp file)
          #"(?m)(;TagStart\n).*(;TagEnd\n)"
          (format "$1%s\n$2" contents))))

(defn i
  []
  (update-template
    "src/io/dominic/hyper/axe.cljs"
    (string/join "\n" (map output-deftag (distinct (all-tags))))))

(defn -main
  [& args]
  (i))
