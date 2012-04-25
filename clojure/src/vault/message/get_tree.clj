(ns vault.message.get-tree
  (:require (borneo (core :as neo))
            (vault (database :as db))
            (vault.message (common :as common))))

(defn- get-head-id
  [node-id]
  (->> (neo/traverse (db/index-get :vault/node :key node-id)
                     :1
                     :all-but-start
                     {:tail :in})
       (map neo/props)
       (map :id)
       (filter identity)
       first))

(defn- get-tail-ids
  [node-id]
  (->> (neo/traverse (db/index-get :vault/node :key node-id)
                     :1
                     :all-but-start
                     {:tail :out})
       (map neo/props)
       (map :id)
       (filter identity)))

(defn build-tree
  [root-id prop-map]
  (when-let [node (get prop-map root-id)]
    (let [tail (->> (get-tail-ids (:id node))
                    (map #(build-tree % (dissoc prop-map root-id)))
                    (filter identity))]
      (if (empty? tail)
        node
        (assoc node :tail (sort-by :name tail))))))

(deftype GetTreeHandler []
  vault.message.common.MessageHandler
  (process [this command arg-map]
    (->> (neo/traverse (db/index-get :vault/node :key (:id arg-map))
                       :all
                       {:tail :out})
         (map neo/props)
         (map (fn [props]
                (assoc props :head (get-head-id (:id props)))))
         (reduce #(assoc %1 (:id %2) %2) nil)
         (build-tree (:id arg-map)))))

(defn make
  []
  (GetTreeHandler.))
