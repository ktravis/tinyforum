(ns tinyforum.models.topics
     (:use [tinyforum.util.timing :only (parse-int get-time)])
     (:use tinyforum.models.keys)
     (:use [tinyforum.models.comments :only (store-raw-comment comment-get)])
     (:use [aleph.redis :only (redis-client)]))

(def r (redis-client {:host "pub-redis-10331.us-east-1-4.3.ec2.garantiadata.com" :port 10331 :password "VRSidx9WYg7QYvUe"}))

; Schema 
;
; topics:<topic id> = {
;   id:         id
;   title:      topic title
;   post-time:  duh
;   author:     again
;   body:       
; }
;
; topics = {<topic id>, ...}

(defn topic-get [id]
  (let [topic (apply hash-map @(r [:hgetall (key-topic id)]))]
    (when (not (empty? topic))
      {:id (topic "id")
       :title (topic "title")
       :post-time (topic "post-time")
       :author (topic "author")
       :body (topic "body")
       :edit-time (topic "edit-time")
       })))


(defn topic-set-id! [id new-id]
  @(r [:hset (key-topic id) "id" new-id]))

(defn topic-set-title! [id new-title]
  @(r [:hset (key-topic id) "title" new-title]))

(defn topic-set-post-time! [id new-post-time]
  @(r [:hset (key-topic id) "post-time" new-post-time]))

(defn topic-set-author! [id new-author]
  @(r [:hset (key-topic id) "author" new-author]))

(defn topic-set-body! [id new-body]
  @(r [:hset (key-topic id) "body" new-body]))

(defn topic-edit-body! [id new-body]
  (topic-set-body! id new-body)
  @(r [:hset (key-topic id) "edit-time" (get-time)]))


(defn topic-get-comments [id]
  (map comment-get @(r [:smembers (key-topic-comment-ids id)])))

(defn topic-add-comment [id comm]
  @(r [:sadd (key-topic-comment-ids id) (:id comm)])
  (store-raw-comment comm))


(defn store-raw-topic [topic]
  (let [id (topic :id)]
    @(r [:lpush "ids" id])
    (topic-set-id! id id)
    (topic-set-title! id (topic :title))
    (topic-set-post-time! id (get-time))
    (topic-set-author! id (topic :author))
    (topic-set-body! id (topic :body))))

(defn store-raw-topics [topics]
  (dorun (map store-raw-topic topics)))


(defn topics-length []
  @(r [:llen "ids"]))

(defn ids-get-latest []
  (parse-int @(r [:lindex "ids" 0])))

(defn topic-get-nth-latest [n]
  (topic-get
    (parse-int @(r [:lindex "ids" n]))))

(defn topic-get-n-latest [n]
  (let [topic-ids @(r [:lrange "ids" 0 (dec n)])]
       (for [i topic-ids
         :let [topic (topic-get i)]
         :when (not (empty? topic))]
           topic)))

(defn topics-get-all []
  (topic-get-n-latest (topics-length)))

(defn is-topic-author? [id u] 
  (if (= (:author (topic-get id)) u) true nil))
