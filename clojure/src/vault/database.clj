(ns vault.database
  (:require (borneo (core :as neo))))

;;--------------------------------------------------
;;
;;  Index
;;
(defn index-add!
  [node index-type key value]
  (neo/with-tx
    (-> (.forNodes (neo/index) (str index-type))
        (.add node (str key) value))))

(defn index-get
  [index-type key value]
  (-> (.forNodes (neo/index) (str index-type))
      (.get (str key) (str value))
      .getSingle))

;;--------------------------------------------------
;;
;;  Node properties
;;
(defmulti set-node-value! (fn [node key value] key))

(defmethod set-node-value! :default
  [node key value]
  (neo/set-prop! node key (or value nil)))

(defmethod set-node-value! :name
  [node key value]
  (neo/set-prop! node key value))

(defmethod set-node-value! :head
  [node key head-id]
  (let [head-node (index-get :vault/node :key head-id)]
    (neo/create-rel! head-node :tail node)))

(defmethod set-node-value! :tail
  [node key id-coll]
  (doseq [rel (neo/rels node :tail :out)]
    (neo/delete! rel))
  (doseq [other (map #(index-get :vault/node :key %) id-coll)]
    (neo/create-rel! node :tail other)))
