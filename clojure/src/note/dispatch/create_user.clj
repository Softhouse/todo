(ns note.dispatch.create-user
  (:require (note (database :as db_)
                  (html :as html_))
            (note.dispatch (common :as common_)
                           (path :as path_))))

(defn make-body
  []
  [:div
   [:form {:method "POST"}
    [:fieldset
     (html_/input "Username" :username)
     (html_/password "Password" :password1)
     (html_/password "Confirm password" :password2)
     (html_/button "Create user")]]
   (html_/link "Already have an account?" (path_/get :login))])

(defn validate-input
  [username pw1 pw2]
  (cond
   (some nil? [username pw1 pw2]) ["Missing parameter, are you hax?"]
   (some empty? [username pw1 pw2]) ["Empty parameter, do it again!"]
   (not= pw1 pw2) ["Passwords do not match."]
   (db_/get-user username) ["User already exists."]
   :else nil))

(defn get-dispatch
  [request]
  {:body (make-body)})

(defn post-dispatch
  [request]
  (let [{:keys [username password1 password2]} (:params request)]
    (if-let [errors (validate-input username password1 password2)]
      {:body (make-body)
       :message (map #(hash-map :text % :type :error) errors)}
      (let [response (db_/create-user username password1)]
        (common_/redirect :login)))))
