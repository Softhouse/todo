(ns vault.test.message.make-node
  (:use clojure.test)
  (:require (vault.test (test :as test))))

(use-fixtures :once test/database-fixture)

(deftest make-node-basic
  (test/message :make-node
                {:name "Qwerty"}
                {:id :test/any}))

(deftest make-node-head
  (let [{parent-id :id} (test/message :make-node
                                      nil
                                      {:id :test/any})
        {child-id :id} (test/message :make-node
                                     {:head parent-id}
                                     {:id :test/any})]
    (test/message :get-node
                  {:id child-id :data [:head]}
                  {:id child-id :head parent-id})))
