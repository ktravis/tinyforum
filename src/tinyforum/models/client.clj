(ns tinyforum.models.client
     (:use [aleph.redis :only (redis-client)]))

(def client (delay (redis-client 
                     {:host "pub-redis-12925.us-east-1-4.3.ec2.garantiadata.com" 
                      :port 12925 :password "8PqiNd6uoNsVDYRH"})))
