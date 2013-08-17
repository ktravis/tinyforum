(ns owlery.models.users
     (:use owlery.util.utils)
     (:use owlery.models.keys)
     (:require [noir.util.crypt :as crypt])
     (:use [owlery.models.topics :only [topic-get]])
     (:use [owlery.models.comments :only [comment-get]])
     (:use [aleph.redis :only (redis-client)]))

(def r (redis-client {:host "localhost"}))

(def admin #{ "test@test.com" })

(defn is-admin? [u] (contains? admin u))

(defn user-get [email]
  (let [user (apply hash-map @(r [:hgetall (key-user email)]))]
    (when (not (empty? user))
      (merge {:email (user "email") :pass (user "pass")}
             {:topics (sort-maps-by (map topic-get
                                         @(r [:smembers (key-user-topics email)]))
                                    :title)
              :comments (sort-maps-by (map comment-get
                                           @(r [:smembers (key-user-comments email)]))
                                      :body)}))))

(defn user-set-email! [email new-email]
  @(r [:hset (key-user email) "email" new-email]))

(defn user-set-pass! [email new-pass]
  @(r [:hset (key-user email) "pass" (crypt/encrypt new-pass)]))

(defn user-add-topic! [email topic-id]
  @(r [:sadd (key-user-topics email) topic-id]))

(defn user-add-comment! [email comment-id]
  @(r [:sadd (key-user-comments email) comment-id]))

(defn user-remove-topic! [email topic-id]
  @(r [:srem (key-user-topics email) topic-id]))

(defn user-remove-comment! [email comment-id]
  @(r [:srem (key-user-comments email) comment-id]))

(defn user-delete! [email]
  @(r [:del (key-user email)])
  @(r [:del (key-user-topics email)])
  @(r [:del (key-user-comments email)]))
