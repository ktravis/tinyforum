(ns tinyforum.models.removal
     (:use tinyforum.models.keys)
     (:use [tinyforum.models.users :only (user-remove-topic! user-remove-comment!)])
     (:use [tinyforum.models.comments :only (comment-get)])
     (:use [tinyforum.models.topics :only (topic-get-comments topic-get)])
     (:use [aleph.redis :only (redis-client)]))

(def r (redis-client {:host "localhost"}))


(defn remove-comment! [cid]
  (let [comm (comment-get cid)]
    (user-remove-comment! (:author comm) cid)
    @(r [:lrem "cids" 0 cid])
    @(r [:del (key-comment cid)])
    @(r [:srem (key-topic-comment-ids (:parent comm)) cid])))

(defn remove-topic! [id]
  (loop [comments (topic-get-comments id)]
    (remove-comment! (:id (first comments)))
    (if-not (empty? (rest comments)) (recur (rest comments))))
  @(r [:lrem "ids" 0 id])
  @(r [:del (key-topic id)])
  (user-remove-topic! (:author (topic-get id)) id))

(defn user-delete! [email]
  @(r [:del (key-user email)])
  @(r [:lrem "emails" 0 email])
  @(r [:srem "admins" email]) 
  (map remove-topic! @(r [:smembers (key-user-topics email)]))
  (map remove-comment! @(r [:smembers (key-user-comments email)]))
  @(r [:del (key-user-topics email)])
  @(r [:del (key-user-comments email)]))
