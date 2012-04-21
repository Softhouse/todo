(ns vault.test.test
  (:use clojure.test)
  (:require (borneo (core :as neo))
            (vault (core :as core))))

;;--------------------------------------------------
;;
;;  Const
;;
(def default-state (core/make-standard))

(defn database-fixture
  [f]
  (neo/with-db! ".test-database"
    (neo/purge!)
    (f)))

;;--------------------------------------------------
;;
;;  Helpers
;;
(defmulti validate-value (fn [actual expect]
                           (cond
                            (= expect :test/any)          :any-value
                            (every? nil? [actual expect]) :nothing
                            (map? expect)                 :map
                            (vector? expect)              :vector
                            :else                         :default)))

(defmethod validate-value :default   [actual expect] (= actual expect))
(defmethod validate-value :any-value [actual expect] (not (nil? actual)))
(defmethod validate-value :nothing   [& _] true)

(defmethod validate-value :map
  [actual expect]
  (let [all-keys (set (mapcat keys [actual expect]))]
    (every? identity
            (map (fn [k a e]
                   (validate-value a e))
                 all-keys
                 (map #(get actual %) all-keys)
                 (map #(get expect %) all-keys)))))

(defmethod validate-value :vector
  [actual expect]
  (every? identity
          (map validate-value
               actual
               expect)))

;;--------------------------------------------------
;;
;;  Interface
;;
(defn message
  "Takes a request and passes it to perform. The result will be
  verified with the given response-map. Returns the result.

  If state is not given default-state will be used.

  Response:
  {<key> <value>   ;; match value with result value
   <key> :test/any ;; accept any value but nil
   ...}"
  ([command parameters response]
     (message nil command parameters response))
  ([state command parameters response]
     (let [result (apply core/perform
                         (or state default-state)
                         command
                         (mapcat identity parameters))]
       (if (validate-value result response)
         (is true)
         (testing "Validation failed:"
           (is (= result response))))
       result)))

(defn make-node
  ([]
     (make-node nil nil))
  ([parameters]
     (make-node nil parameters))
  ([state parameters]
     (let [result (message state :make-node parameters {:id :test/any})]
       (if (:id result)
         (:id result)
         (testing "Message result does not contain :id"
           (is (= {:id 0} result)))))))
