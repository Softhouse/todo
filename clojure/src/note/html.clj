(ns note.html
  (:use (hiccup (core :only (html)))))

(defn transform
  [body]
  (html body))

;;--------------------------------------------------
;;
;;  Base HTML
;;
(defn base
  [{:keys [body messages]}]
  [:html
   [:head
    [:title "Note"]
    [:link {:rel "stylesheet" :type "text/css" :href "note.css"}]]
   [:body
    [:div {:class :main}
     [:div#head [:h1 "Note"]]
     messages
     body]]])

(defn input
  [text parameter]
  [:div.labelField
   [:label {:for parameter} text]
   [:input {:id parameter :type "text" :name parameter :autocomplete "off"}]])

(defn password
  [text parameter]
  [:div.labelField
   [:label {:for parameter} text]
   [:input {:id parameter :type "password" :name parameter :autocomplete "off"}]])

(defn textfield
  [text parameter]
  [:div.labelField
   [:label {:for parameter} text]
   [:textarea {:id parameter :type "textfield" :name parameter}]])

(defn button
  [text]
  [:input {:type "submit" :value text}])

(defn link
  [text path]
  [:a {:href path} text])

(defn hidden
  [key value]
  [:input {:type "hidden" :name key :value value}])

;;--------------------------------------------------
;;
;;  Other helpers
;;
(defn message-list
  [errors]
  [:ul
   (map (fn [{:keys [text type]}]
          [:li (if (= :error type)
                 [:strong text]
                 text)])
        errors)])
