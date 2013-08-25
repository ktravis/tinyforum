(ns tinyforum.models.client
     (:use [aleph.redis :only (redis-client)]))

(def r (redis-client 
  {:host "scat.redistogo.com" 
   :port 10023 :password "c5de129266f4e611d5a6e4d6b07504b2"}))
