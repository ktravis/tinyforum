(ns owlery.models.comments
     (:use [owlery.util.timing :only (parse-int get-time)])
     (:use owlery.models.keys)
     (:use [aleph.redis :only (redis-client)]))

(def r (redis-client {:host "localhost"}))

; Schema 

(defn comment-get [id]
  (let [comm (apply hash-map @(r [:hgetall (key-comment id)]))]
    (when (not (empty? comm))
      {:post-time (comm "post-time")
       :author (comm "author")
       :body (comm "body")
       })))

(defn comment-set-id! [id new-id]
  @(r [:hset (key-comment id) "id" new-id]))

(defn comment-set-post-time! [id new-post-time]
  @(r [:hset (key-comment id) "post-time" new-post-time]))

(defn comment-set-author! [id new-author]
  @(r [:hset (key-comment id) "author" new-author]))

(defn comment-set-body! [id new-body]
  @(r [:hset (key-comment id) "body" new-body]))

(defn cids-get-latest []
  (parse-int @(r [:lindex "cids" 0])))


(defn store-raw-comment [comm]
  (let [id (comm :id)]
    @(r [:lpush "cids" id])
    (comment-set-id! id id)
    (comment-set-post-time! id (get-time))
    (comment-set-author! id (comm :author))
    (comment-set-body! id (comm :body))))

(defn store-raw-comments [comments]
  (dorun (map store-raw-comment comments)))

(defn comments-length []
  @(r [:llen "cids"]))
