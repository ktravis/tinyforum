(ns owlery.views.faq
  (:require [owlery.views.common :as common]
            [noir.content.getting-started])
  (:use [noir.core :only [defpage]])
  )


(defpage "/faq" []
         (common/site-layout
           [:div.jumbotron
            [:h1 "FAQ"]
            [:p.lead 
             [:ol {:style "text-align:left;padding-top:5px"}
              [:li "First question? Answer."]
              [:li "Question number 2? No answer."]
              [:li "This is the last question? Yes."]]]]))
