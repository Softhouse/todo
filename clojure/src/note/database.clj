(ns note.database
  (:require (vault (core :as vault_))))

(def ^:private state (vault_/make-standard))

(defn- check-start!
  []
  (when-not (vault_/started?)
    (vault_/start! ".neo4j")))

(defn perform
  [command & args]
  (check-start!)
  (apply vault_/perform state command args))

(defn create-user
  [username password]
  (perform :make-node
           :name username
           :password password
           :vault/index {:type :user
                         :value username}))

(defn get-user
  [username]
  (perform :get-node
           :vault/index {:type :user
                         :value username}))

(defn get-tree
  [username]
  (let [user (get-user username)
        id (:id user)]
    (perform :get-tree :id id)))

(defn add-note
  [parent-id name text]
  (perform :make-node
           :head parent-id
           :name name
           :text text))

(defn get-node
  [id]
  (perform :get-node :id id :data [:head :tail]))

(defn toggle-note
  [id]
  (let [node (perform :get-node :id id)]
    (perform :set-node
             :id id
             :done (or (not (:done node))
                       nil))))
