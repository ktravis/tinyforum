(ns owlery.util.timing)

(defn parse-int [s]
  (if s 
    (Integer. (re-find #"\d+" s))
    0))

(defn parse-long [s]
  (if s 
    (Long. (re-find #"\d+" s))
    0))

(defn now [] (new java.util.Date))

(defn get-time [] (str (.getTime (now))))

(defn format-time [t] 
  (let [t (if (string? t)
            (parse-long t)
            t)
        df (new java.text.SimpleDateFormat "'at' h:mm a 'on' EEEE, MMMM d")]
    (.format df (new java.util.Date t))))
