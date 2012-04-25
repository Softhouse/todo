(ns vault.message.set-node
  (:require (borneo (core :as neo))
            (vault (database :as db))
            (vault.message (common :as common))))

(deftype SetNodeHandler []
  vault.message.common.MessageHandler
  (process [this command arg-map]
    (let [node (db/index-get :vault/node :key (:id arg-map))]
      (neo/with-tx
        (doseq [key [:name :tail :done]]
          (when-let [value (get arg-map key)]
            (db/set-node-value! node key value)))))
    {:result :ok}))

(defn make
  []
  (SetNodeHandler.))
