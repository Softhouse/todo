(ns note.dispatch.path
  (:refer-clojure :exclude (get)))

(defn get
  [id]
  ({
    :create-note "/create_note"
    :create-user "/create_user"
    :login       "/login"
    :root        "/"
    :task-list   "/task_list"
    :toggle-note "/toggle_note"
    } id))
