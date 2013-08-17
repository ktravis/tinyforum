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
  (let [topic (topic-get id)]
  (user-remove-topic! (:author topic) id)
    ;(for [cid (map (fn [comm] (:id comm)) (topic-get-comments id))]
      ;(remove-comment! cid)    
      ;)
    (map (fn [comm] (remove-comment! (:id comm))) (topic-get-comments id))
    @(r [:lrem "ids" 0 id])
    @(r [:del (key-topic id)])
    )
  )
