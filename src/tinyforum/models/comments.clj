(ns tinyforum.models.comments
     (:use [tinyforum.util.timing :only (parse-int get-time)])
     (:use tinyforum.models.keys)
     (:use [aleph.redis :only (redis-client)]))

(def r (redis-client {:host "localhost" :password "owlcity"}))

; Schema 

(defn comment-get [id]
  (let [comm (apply hash-map @(r [:hgetall (key-comment id)]))]
    (when (not (empty? comm))
      {:post-time (comm "post-time")
       :author (comm "author")
       :id (comm "id")
       :parent (comm "parent")
       :body (comm "body")
       })))

(defn comment-set-id! [id new-id]
  @(r [:hset (key-comment id) "id" new-id]))

(defn comment-set-parent! [id new-parent]
  @(r [:hset (key-comment id) "parent" new-parent]))

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
    (comment-set-parent! id (comm :parent))
    (comment-set-post-time! id (get-time))
    (comment-set-author! id (comm :author))
    (comment-set-body! id (comm :body))))

(defn store-raw-comments [comments]
  (dorun (map store-raw-comment comments)))

(defn comments-length []
  @(r [:llen "cids"]))

(defn is-comment-author? [cid u]
  (if (= (:author (comment-get cid)) u) true nil))
