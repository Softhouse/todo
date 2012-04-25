(ns note.dispatch.toggle-note
  (:require (note (database :as db_))
            (note.dispatch (common :as common_))))

(defn get-dispatch
  [request]
  (let [{:keys [id]} (:params request)]
    (db_/toggle-note id)
    (common_/redirect :task-list)))
