(ns tinyforum.models.client
     (:use [aleph.redis :only (redis-client)]))

(def client (delay (redis-client 
                     {:host "pub-redis-13618.us-east-1-4.3.ec2.garantiadata.com" 
                      :port 13618 :password "NLwnDlCIziUk3H0U"})))
