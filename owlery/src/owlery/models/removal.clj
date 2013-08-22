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
    @(r [:srem (key-topic-comment-ids (:parent comm)) cid])
    )
  )

(defn remove-topic! [id]
  ;(for [comm (topic-get-comments id)]
  ;(remove-comment! (:id comm))) 
  ;(map #(remove-comment! (:id %)) (topic-get-comments id))
  ;(for [comm (topic-get-comments id)]
  ;(let [cid (:id comm)] 
  ;(user-remove-comment! (:author comm) (:id comm))
  ;@(r [:lrem "cids" 0 cid])
  ;@(r [:del (key-comment cid)])
  ;@(r [:srem (key-topic-comment-ids id cid)])))
  (let [topic (topic-get id) comments (topic-get-comments id)]
    ;(map (fn [comm] (remove-comment! (:id comm))) (topic-get-comments id))
    (map #(remove-comment! (:id %)) comments)
    @(r [:lrem "ids" 0 id])
    @(r [:del (key-topic id)])
    (user-remove-topic! (:author topic) id)
    ))
