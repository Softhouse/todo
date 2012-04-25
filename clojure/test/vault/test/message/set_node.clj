(ns vault.test.message.set-node
  (:use clojure.test)
  (:require (vault.test (test :as test))))

(use-fixtures :once test/database-fixture)

(deftest set-node-name
  (let [id (test/make-node {:name "Alpha"})]
    (test/message :set-node
                  {:id id :name "Omega"}
                  {:result :ok})
    (test/message :get-node
                  {:id id}
                  {:id id :name "Omega"})))

(deftest set-node-tail
  (let [id (test/make-node {:name "Alpha"})
        tail1 (test/make-node {:name "Beta"})
        tail2 (test/make-node {:name "Gamma"})]
    (test/message :set-node
                  {:id id :tail [tail1 tail2]}
                  {:result :ok})
    (test/message :get-node
                  {:id id}
                  {:id id :name "Alpha" :tail [tail1 tail2]})))
