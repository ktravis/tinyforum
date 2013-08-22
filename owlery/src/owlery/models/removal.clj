(ns owlery.models.removal
     (:use owlery.models.keys)
     (:use [owlery.models.users :only (user-remove-topic! user-remove-comment!)])
     (:use [owlery.models.comments :only (comment-get)])
     (:use [owlery.models.topics :only (topic-get-comments topic-get)])
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
