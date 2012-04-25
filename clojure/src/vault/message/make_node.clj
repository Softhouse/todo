(ns vault.message.make-node
  (:require (borneo (core :as neo))
            (vault (database :as db))
            vault.message.common)
  (:import java.util.Random))

(defn- fixup-node-data
  [node-data id]
  (-> node-data
      (assoc :id id)
      (dissoc :head :vault/index)))

(defn- set-node-values
  [node node-data]
  (doseq [key [:head :tail]]
    (when-let [value (get node-data key)]
      (db/set-node-value! node key value))))

(defn- add-additional-index
  [node index-data]
  (db/index-add! node (:type index-data) :key (:value index-data)))

(deftype MakeNodeHandler [rand-long]
  vault.message.common.MessageHandler
  (process [this command arg-map]
    (neo/with-tx
      (let [id (rand-long)
            index-data (:vault/index arg-map)
            node-data (fixup-node-data arg-map id)
            node (neo/create-node! node-data)]
        (set-node-values node arg-map)
        (db/index-add! node :vault/node :key id)
        (when index-data
          (add-additional-index node index-data))
        {:id id}))))

(defn make
  []
  (let [rnd (java.util.Random.)]
    (MakeNodeHandler. (fn [] (.nextLong rnd)))))
