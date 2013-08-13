(ns owlery.views.topic
  (:require [owlery.views.common :as common]
            [noir.content.getting-started])
  (:use [noir.core :only [defpage]])
  )


(defpage "/topic" []
         (common/site-layout
            [:h3 "Yo it's a Topic Title"]
            [:p "I was bored so go nuts. I can add your email to the admin list if you ask me."]
            [:hr]
            [:div#comments
             (apply concat
              (for [x (range 1 5)]
               [[:div.clearfix.content-heading.comment (str "This is comment " x)]
                [:hr]]))]))
