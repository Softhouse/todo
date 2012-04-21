(ns vault.core
  (:require (borneo (core :as neo_))
            (vault.message (common :as msg_)
                           (get-tree :as get-tree)
                           (get-node :as get-node)
                           (make-node :as make-node)
                           (set-node :as set-node))))

(defn make
  [])

(defn add-handler
  [state command handler]
  (assoc-in state [command] handler))

(defn perform
  [state command & args]
  (msg_/process (get-in state [command])
               command
               (apply hash-map args)))

(defmacro with-db!
  [path & forms]
  `(neo_/with-db! path ~@forms))

(def start! neo_/start!)
(def stop! neo_/stop!)

(defn started?
  []
  (and (bound? #'neo_/*neo-db*)
       neo_/*neo-db*))

(defn make-standard
  []
  (-> (make)
      (add-handler :get-tree  (get-tree/make))
      (add-handler :get-node  (get-node/make))
      (add-handler :make-node (make-node/make))
      (add-handler :set-node  (set-node/make))))
