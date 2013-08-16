(ns owlery.models.keys)

(defn key-topic [id]
  (str "topics:" id))

(defn key-user [email]
  (str "users:" email))

(defn key-user-topics [email]
  (str "users:" email ":topics"))

(defn key-user-comments [email]
  (str "users:" email ":comments"))

(defn key-topic-comment-ids [id]
  (str "topics:" id ":comments"))

(defn key-comment [id]
  (str "comments:" id))
