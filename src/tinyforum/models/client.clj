(ns tinyforum.models.client
     (:use [aleph.redis :only (redis-client)]))

(def redis-host "localhost")
(def redis-password "")
(def redis-port 6379)

(def r (delay (redis-client 
  {:host redis-host
   :port redis-port
   :password redis-password})))
