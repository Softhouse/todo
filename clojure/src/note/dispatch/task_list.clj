(ns note.dispatch.task-list
  (:require (note (database :as db_)
                  (html :as html_))
            (note.dispatch (path :as path_))))

(defn- create-link
  [parent-id]
  (str (path_/get :create-note) "?parent-id=" parent-id))

(defn- toggle-link
  [id]
  (str (path_/get :toggle-note) "?id=" id))

(defn- item-tree->html
  [node-tree indent]
  [:div {:class (str "indent_" indent)}
   [:table {:width "100%"}
    [:tr
     (if (:done node-tree)
       [:td#done "Done"]
       [:td#todo "Todo"])
     [:td (:name node-tree)]
     [:td (html_/link "Take note" (create-link (:id node-tree)))]
     [:td (html_/link "Toggle" (toggle-link (:id node-tree)))]]
    [:tr {:col-span 4}
     [:td (:text node-tree)]]]
   [:div
    (map #(item-tree->html % (inc indent)) (:tail node-tree))]])

;; TODO debug
(use 'clojure.pprint)

(defn- make-body
  [node-tree]
  [:div
   [:h1 "This is body"]
   [:pre
    #_(.replaceAll (with-out-str (pprint node-tree))
                   "\n"
                   "<br />")]
   [:hr]
   (item-tree->html node-tree 0)])

(defn get-dispatch
  [request]
  (let [node-tree (db_/get-tree (-> request :session :username))]
    {:body (make-body node-tree)}))

(defn post-dispatch
  [request]
  {:body [:div "TASK LIST - POST"]})
