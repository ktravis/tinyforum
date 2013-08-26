(ns tinyforum.models.client
     (:use [aleph.redis :only (redis-client)]))

(def redis-host "scat.redistogo.com")
(def redis-password "db52dcc762988a408ecd823ddaec2395")
(def redis-port 10034)

(def r (delay (redis-client 
  {:host redis-host
   :port redis-port
   :password redis-password})))
