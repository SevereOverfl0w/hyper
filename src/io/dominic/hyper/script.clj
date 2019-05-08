(ns io.dominic.hyper.script
  (:require cljs.core
            cljs.tagged-literals))

(defmacro read-js*
  [x]
  (cond
    (map? x)
    (cljs.tagged-literals/->JSValue
      (into {}
            (map (fn [[k v]]
                   (when-not (cljs.tagged-literals/valid-js-literal-key? k)
                     (throw
                       (RuntimeException.
                         "JavaScript literal keys must be strings or unqualified keywords")))
                   [k `(read-js* ~v)])
                 x)))

    (vector? x)
    (cljs.tagged-literals/->JSValue
      (mapv (fn [v] `(read-js* ~v)) x))

    :else
    x))

(defn read-js*-fn
  [x]
  (cond
    (map? x)
    (cljs.tagged-literals/->JSValue
      (into {}
            (map (fn [[k v]]
                   (when-not (cljs.tagged-literals/valid-js-literal-key? k)
                     (throw
                       (RuntimeException.
                         "JavaScript literal keys must be strings or unqualified keywords")))
                   [k (read-js*-fn v)])
                 x)))

    (vector? x)
    (cljs.tagged-literals/->JSValue
      (mapv (fn [v] (read-js*-fn v)) x))

    :else
    x))

(defmacro h
  [tag child-or-props & children]
  (let [props (if (map? child-or-props)
                (read-js*-fn child-or-props)
                nil)
        children
        (if (not props)
          (cons child-or-props children)
          children)]

    `(apply
       io.dominic.hyper.script/create-element
       ~tag
       ~props
       ~@children)))

(comment
  (js-obj* {:some-schema
            {:type 100
             :foo {:bar 10}
             :baz (fn [] (+ 10 10))}}))
