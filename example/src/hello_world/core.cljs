(ns ^:figwheel-hooks hello-world.core
  (:require
   [goog.dom :as gdom]
   [react :as react]
   [react-dom :as react-dom]
   [io.dominic.hyper.script :refer [h]]
   [io.dominic.hyper.axe :as 🪓️]))

(println "This text is printed from src/hello_world/core.cljs. Go ahead and edit it and see reloading in action.")

(defn get-app-element []
  (gdom/getElement "app"))

(defn mount [el]
  (react-dom/render
    (h "div"
       {:onClick (fn [e] (js/alert "Hi from h"))}
       "Hello, " "world"
       (for [i (range 10)]
         (🪓️/p {:key i} (str "Line: " i))))
    el))

(defn mount-app-element []
  (when-let [el (get-app-element)]
    (mount el)))

;; conditionally start your application based on the presence of an "app" element
;; this is particularly helpful for testing this ns without launching the app
(mount-app-element)

;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  (mount-app-element)
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
