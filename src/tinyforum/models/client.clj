(ns tinyforum.models.client
     (:use [aleph.redis :only (redis-client)]))

(def client (delay (redis-client 
                     {:host "pub-redis-10331.us-east-1-4.3.ec2.garantiadata.com" 
                      :port 10331 :password "VRSidx9WYg7QYvUe"})))
