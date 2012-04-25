(ns vault.test.message.get-node
  (:use clojure.test)
  (:require (vault.test (test :as test))))

(use-fixtures :once test/database-fixture)

(deftest get-node-basic
  (let [name "Loaf of bread"
        result (test/message :make-node
                             {:name name}
                             {:id :test/any})
        id (:id result)]
    (test/message :get-node
                  {:id id}
                  {:id id :name name})))

(deftest get-node-tail
  (let [{master-id :id} (test/message :make-node
                                      nil
                                      {:id :test/any})
        {slave-1 :id} (test/message :make-node
                                    {:head master-id}
                                    {:id :test/any})
        {slave-2 :id} (test/message :make-node
                                    {:head master-id}
                                    {:id :test/any})]
    (test/message :get-node
                  {:id master-id :data [:tail]}
                  {:id master-id :tail [slave-1 slave-2]})))

(deftest get-node-head
  (let [{master-id :id} (test/message :make-node
                                      nil
                                      {:id :test/any})
        {slave-1 :id} (test/message :make-node
                                    {:head master-id}
                                    {:id :test/any})
        {slave-2 :id} (test/message :make-node
                                    {:head master-id}
                                    {:id :test/any})]
    (test/message :get-node
                  {:id slave-1 :data [:head]}
                  {:id slave-1 :head master-id})))

(deftest get-non-existing-node
  (let [node (test/message :get-node
                           {:id 0}
                           nil)]
    (is (nil? node))))
