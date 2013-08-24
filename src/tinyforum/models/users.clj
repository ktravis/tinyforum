(ns tinyforum.models.users
     (:require [noir.util.crypt :as crypt])
     (:use tinyforum.util.utils)
     (:use tinyforum.models.client)
     (:use tinyforum.models.keys)
     (:use [tinyforum.models.topics :only [topic-get]])
     (:use [tinyforum.models.comments :only [comment-get]]))


(def admin #{ "test@test.com" })

(def r @client)

(defn is-admin? [u] (or (contains? admin u)
                        (not= 0 (r [:sismember "admins" u])))) 

(defn user-get [email]
  (let [user (apply hash-map (r [:hgetall (key-user email)]))]
    (when (not (empty? user))
      (merge {:email (user "email") :pass (user "pass")}
             {:topics (sort-maps-by (map topic-get
                                         (r [:smembers (key-user-topics email)]))
                                    :title)
              :comments (sort-maps-by (map comment-get
                                           (r [:smembers (key-user-comments email)]))
                                      :body)}))))

(defn user-set-email! [email new-email]
  (r [:lrem "emails" 0 email])
  (r [:lpush "emails" new-email])
  (r [:hset (key-user email) "email" new-email]))

(defn users-get-all []
  (r [:lrange "emails" 0 (dec (r [:llen "emails"]))])
  )

(defn user-set-pass! [email new-pass]
  (r [:hset (key-user email) "pass" (crypt/encrypt new-pass)]))

(defn user-add-topic! [email topic-id]
  (r [:sadd (key-user-topics email) topic-id]))

(defn user-add-comment! [email comment-id]
  (r [:sadd (key-user-comments email) comment-id]))

(defn user-remove-topic! [email topic-id]
  (r [:srem (key-user-topics email) topic-id]))

(defn user-remove-comment! [email comment-id]
  (r [:srem (key-user-comments email) comment-id]))

(defn user-promote! [email]
  (r [:sadd "admins" email]))

(defn user-demote! [email]
  (r [:srem "admins" email]))

