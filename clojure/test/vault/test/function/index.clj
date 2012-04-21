(ns vault.test.function.index
  (:use clojure.test)
  (:require (vault.test (test :as test))))

(use-fixtures :once test/database-fixture)

(deftest index-basic
  (let [name "Scrying Sphere"
        {id :id} (test/message :make-node
                               {:name name
                                :vault/index {:type "name"
                                              :value name}}
                               {:id :test/any})]
    (test/message :get-node
                  {:vault/index {:type "name"
                                 :value name}}
                  {:id id :name name})))

(deftest index-get-non-existing
  (let [node (test/message :get-node
                           {:vault/index {:type "qwerty"
                                          :value "none"}}
                           nil)]
    (is (nil? node))))

(deftest index-get-nil-value
  (let [node (test/message :get-node
                           {:vault/index {:type "qwerty"
                                          :value nil}}
                           nil)]
    (is (nil? node))))
