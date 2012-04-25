(ns vault.message.common)

(defprotocol MessageHandler
  (process [this command arg-map]))
