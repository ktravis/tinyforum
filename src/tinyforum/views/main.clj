(ns tinyforum.views.main
  (:require [tinyforum.views.common :as common]
            [tinyforum.models.topics :as topics]
            [tinyforum.models.static :as static]
            [noir.session :as sess]
            [noir.response :as resp])
  (:use [noir.core :only [defpage defpartial]]
        [markdown.core]
        [tinyforum.models.users :only [user-add-topic!]]
        [hiccup.form :only [form-to label text-field label text-area submit-button]]))

(defpage "/" [] 
         (sess/put! :referral "/")
         (common/site-layout
           [:div.jumbotron
            [:h1 (static/get-banner)]
            [:p.lead (md-to-html-string (static/get-lead))]]
           [:table.table.table-hover
            [:thead
             [:tr [:th "User"] [:th "Topic"]]]
            [:tbody
             (for [topic (topics/topics-get-all)]
              [:tr.topics
               [:td (:author topic)]
               [:td [:a {:href (str "/topic/" (:id topic))
                         :style (if (common/seen? (:id topic))
                                 "font-weight:regular" 
                                 "font-weight:bold")} (:title topic)]
                (when-let [cids (topics/topic-get-comment-ids (:id topic))]
                  (let [num (count (clojure.string/join #" " 
                              (filter #(not (common/comment-seen? %)) 
                                      cids)))]
                    (if (< 0 num) 
                      [:span.countpos [:a.commentcount 
                       {:href (str "/topic/" (:id topic))} 
                       (str num)]])))]])]]
           (if (common/logged-in?)
             (form-to [:post "/add"]
               [:fieldset
                 [:legend "Create new Topic"]
                 (label "title" "Topic Title")
                 (text-field "h")
                 (label "topic" "Topic Text")
                 (text-area {:style "width:98%;min-height:140px;"} "b")
                 (submit-button "Submit")]) (common/please-login))))

(defpage [:post "/add"] {:keys [h b]}
  (let [id (inc (topics/ids-get-latest)) user (sess/get :email)]
    (user-add-topic! user id)
    (topics/store-raw-topic {:title h :body b :author user :id id}))
  (do (resp/redirect "/")))
