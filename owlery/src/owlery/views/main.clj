(ns owlery.views.main
  (:require [owlery.views.common :as common]
            [owlery.models.topics :as topics]
            [noir.session :as sess]
            [noir.response :as resp])
  (:use [noir.core :only [defpage defpartial]]
        [owlery.models.users :only [user-add-topic!]]
        [hiccup.form :only [form-to label text-field label text-area submit-button]]))

(defpage "/" [] 
         (common/site-layout
           [:div.jumbotron
            [:h1 "Oregon Wafer Lab"]
            [:p.lead "I was bored so go nuts. I can add your email to the admin list if you ask me."]]
           [:table.table.table-hover
            [:thead
             [:tr [:th "User"] [:th "Topic"]]]
            [:tbody
             (for [topic (topics/topics-get-all)]
              [:tr.topics
               [:td (:author topic)]
               [:td [:a {:href (str "/topic/" (:id topic))} (:title topic)]]])]]
           (if (common/logged-in?)
             (form-to [:post "/add"]
               [:fieldset
                 [:legend "Create new Topic"]
                 (label "title" "Topic Title")
                 (text-field "h")
                 (label "topic" "Topic Text")
                 (text-area {:value "testing" :style "width:98%;min-height:140px;"} "b")
                 (submit-button "Submit")]) (common/please-login))))

(defpage [:post "/add"] {:keys [h b]}
  (let [id (inc (topics/ids-get-latest)) user (sess/get :email)]
    (user-add-topic! user id)
    (topics/store-raw-topic {:title h :body b :author user :id id}))
  (do (resp/redirect "/")))
