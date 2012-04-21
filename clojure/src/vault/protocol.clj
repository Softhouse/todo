(ns vault.protocol)

(defn of-class
  [classes]
  {:function (fn [value]
               (some #(instance? % value) classes))
   :text (reduce str
                 (concat ["Wrong class type (Must be one of: "]
                         (interpose ", " (map .getName classes))
                         [")"]))})

(defn string-with-content
  []
  {:function #(-> % .trim count zero? not)
   :text "Must contain at least one non-whitespace character."})

;; --------------------------------------------------

(def node-id
  {:id :node-id
   :validate [(of-class [Integer Long])]})

(def node-name
  {:id :node-name
   :validate [(of-class [String])
              (string-with-content)]})

(def make-node-request
  {:id :node-data
   :type :map
   :values {:id :node-id
            :name :node-name}})
