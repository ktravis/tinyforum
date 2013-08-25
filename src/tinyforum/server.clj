(ns tinyforum.server
  (:require [noir.server :as server])
  (:require [tinyforum.models.client :as client]))

(server/load-views-ns 'tinyforum.views)

(defn -main [& m]
  (let [mode (keyword (or (first m) :dev))
        port (Integer. (get (System/getenv) "PORT" "8080"))]
    (server/start port {:mode mode
                        :ns 'tinyforum})))

