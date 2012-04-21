(ns note.core
  (:use clojure.pprint)
  (:require (clojure (stacktrace :as stacktrace_)
                     (walk :as walk_))
            (note (html :as html_))
            (note.dispatch (dispatch :as dispatch_))
            (ring.middleware (params :as params_)
                             (reload :as reload_)
                             (session :as session_))
            (ring.util (response :as ring-response_)))
  (:import java.util.Date))

(defn- make-html
  [response]
  (let [errors (when-let [msgs (:message response)]
                 (html_/message-list msgs))]
    (assoc response
      :body (-> {:messages errors :body (:body response)}
                html_/base
                html_/transform))))

(defn- process-response
  [response]
  (-> response
      make-html
      (assoc :status 200
             :headers {"Content-Type" "text/html"})))

(defn request-dispatcher
  [request]
  (require 'note.dispatch.dispatch
           'note.database
           :reload-all)
  (println "--------------------------------------------------------------------------------" (Date.))
  (let [debug (-> request :uri #{"/favicon.ico" "/note.css"} not)
        request (update-in request [:params] walk_/keywordize-keys)]
    ;; before
    (when debug
      (pprint request)
      (println))
    ;; middle
    (let [response (dispatch_/process request)
          response (if (and (map? response)
                            (vector? (:body response)))
                     (process-response response)
                     response)]
      (pprint response)
      (println)
      response)))

(def handler
  (-> request-dispatcher
      params_/wrap-params
      session_/wrap-session))


(use 'ring.adapter.jetty)
(defn -main
  [& _]
  (run-jetty handler {:port 3000}))
