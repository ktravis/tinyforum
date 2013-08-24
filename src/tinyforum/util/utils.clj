(ns tinyforum.util.utils)

(defn sort-maps-by [coll k]
  (sort #(compare (k %1) (k %2)) coll))

(def email-regex #"[a-zA-Z0-9._+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}")

