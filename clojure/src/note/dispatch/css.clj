(ns note.dispatch.css)

(def color-bg "#362f26")
(def color-fg "#e0c39e")
(def color-light "#8a7862")

(defmulti to-str type)
(defmethod to-str java.lang.String [s] s)
(defmethod to-str clojure.lang.Keyword [kw] (name kw))
(defmethod to-str java.util.Map
  [m]
  (->> m
       (sort-by first)
       (map (fn [[k v]]
              (format "    %s: %s;\n"
                      (to-str k)
                      (to-str v))))
       (reduce str)))

(defn- sel
  [css-selector & {:as opt}]
  (format "%s {\n%s\n}\n"
          (to-str css-selector)
          (to-str opt)))

(defn get-dispatch
  [request]
  {:headers {"Content-Type" "text/css"}
   :status 200
   :body (str
          (sel :body
               :margin :auto
               :background color-bg
               :color color-fg)
          (sel ".main"
               :margin :auto
               :width "600px")

          ;; links
          (sel "a"
               :color "#ebb13d")

          (sel "indent_0, .indent_1, .indent_2"
               :margin "1em"
               :border "1px solid black")
          
          ;; what?
          (sel "fieldset"
               :border "none")
          (sel ".box"
               :border (str "0.1em solid " color-light)
               :box-shadow (str "0.4em 0.4em 1em " color-light)
               :margin "0.2em"
               :padding "0.2em")

          ;; labelfield
          (sel ".labelField"
               :margin :auto)
          (sel ".labelField label"
               :display :inline-block
               :margin "0.2em")
          (sel ".labelField input"
               :display :inline
               :margin "0.2em"
               :height "1.8em"))})
