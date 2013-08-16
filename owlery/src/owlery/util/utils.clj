(ns owlery.util.utils)

(defn sort-maps-by [coll k]
  (sort #(compare (%1 k) (%2 k)) coll))

(def email-regex #"[a-zA-Z0-9._+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}")

