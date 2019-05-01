(ns io.dominic.hyper.script
  (:require ["react" :as react])
  (:require-macros io.dominic.hyper.script))

(defn create-element
  [tag props & children]
  (apply react/createElement tag props children))
