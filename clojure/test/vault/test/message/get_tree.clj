(ns vault.test.message.get-tree
  (:use clojure.test)
  (:require (vault.test (test :as test))))

(use-fixtures :once test/database-fixture)

(deftest get-tree-basic
  (let [id (test/make-node {:name "Alpha"
                            :tail [(test/make-node {:name "Beta"})
                                   (test/make-node {:name "Gamma"})]})]
    (test/message :get-tree
                  {:id id}
                  {:id id
                   :head nil
                   :name "Alpha"
                   :tail [{:id :test/any :head :test/any :name "Beta"}
                          {:id :test/any :head :test/any :name "Gamma"}]})))

(deftest get-tree-multi-level
  (let [id (test/make-node
            {:name "Alpha"
             :tail [(test/make-node
                     {:name "Beta"
                      :tail [(test/make-node
                              {:name "Gamma"
                               :tail [(test/make-node
                                       {:name "Delta"})]})]})]})]
    (test/message :get-tree
                  {:id id}
                  {:id id
                   :head nil
                   :name "Alpha"
                   :tail [{:id :test/any
                           :head :test/any
                           :name "Beta"
                           :tail [{:id :test/any
                                   :head :test/any
                                   :name "Gamma"
                                   :tail [{:id :test/any
                                           :head :test/any
                                           :name "Delta"}]}]}]})))

(deftest get-tree-reverse-creation
  (let [{parent-id :id} (test/message :make-node
                                      {:name "Parent"}
                                      {:id :test/any})
        {child-id :id} (test/message :make-node
                                     {:head parent-id :name "Child"}
                                     {:id :test/any})]
    (test/message :get-tree
                  {:id parent-id :data [:head]}
                  {:id parent-id
                   :head nil
                   :name "Parent"
                   :tail [{:id child-id
                           :head parent-id
                           :name "Child"}]})))
