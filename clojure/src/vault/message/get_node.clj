(ns vault.message.get-node
  (:require (borneo (core :as neo))
            (vault (database :as db))
            vault.message.common))

(defn- get-node
  [arg-map]
  (if (:vault/index arg-map)
    (let [{{:keys [type value]} :vault/index} arg-map]
      (db/index-get type :key value))
    (db/index-get :vault/node :key (:id arg-map))))

(deftype GetNodeHandler []
  vault.message.common.MessageHandler
  (process [this command arg-map]
    (when-let [node (get-node arg-map)]
      (let [data (neo/props node)
            relations (neo/rels node)
            data (if ((set (:data arg-map)) :tail)
                   (assoc data :tail (map (comp :id
                                                neo/props
                                                #(neo/other-node % node))
                                          relations))
                   data)
            data (if ((set (:data arg-map)) :head)
                   (assoc data :head (first
                                      (map (comp :id
                                                 neo/props
                                                 #(neo/other-node % node))
                                           relations)))
                   data)]
        data))))

(defn make
  []
  (GetNodeHandler.))
