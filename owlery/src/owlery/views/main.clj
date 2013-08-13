(ns owlery.views.main
  (:require [owlery.views.common :as common]
            [noir.content.getting-started])
  (:use [noir.core :only [defpage]]
        [hiccup.form :only [form-to label text-field label text-area submit-button]])
  )


(defpage "/main" {:keys [topic]} 
         (common/site-layout
           [:div.jumbotron
            [:h1 "Oregon Wafer Lab"]
            [:p.lead "I was bored so go nuts. I can add your email to the admin list if you ask me."]]
           [:table.table.table-hover
            [:thead
             [:tr [:th "User"] [:th "Topic"]]]
            [:tbody
              (if topic
                (let [[t b] topic]
                  [:tr.topics
                   [:td [:a {:href "/"} t]]
                   [:td (str b)]])
                [:tr.topics
                 [:td [:a "user"]]
                 [:td t]])
             (for [x (range 1 5)]
               [:tr.topics
                [:td [:a {:href "/"} "- "]][:td (str "This is topic " x)]])]]
           (form-to [:post "/main"]
             [:fieldset
               [:legend "Create new Topic"]
               (label "title" "Topic Title")
               (text-field "h")
               (label "topic" "Topic Text")
               (text-area {:value "testing" :style "width:100%;min-height:150px;"} "b")
               (submit-button "Submit")])))

(defpage [:post "/main"] {:keys [h b]} 
         (noir.core/render "/main"
                           {:topic [h b]}))
