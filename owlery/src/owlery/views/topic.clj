(ns owlery.views.topic
  (:require [owlery.views.common :as common]
            [owlery.models.topics :as topics]
            [noir.response :as resp]
            [noir.content.getting-started])
  (:use [noir.core :only [defpage defpartial]]
        [owlery.views.common :only [logged-in?]]
        [owlery.models.comments :only [cids-get-latest]]
        [owlery.util.timing :only [format-time]]
        [hiccup.form :only [form-to label text-field label text-area submit-button]])
  )

(defpartial comment-item [comm]
  [:div.clearfix.content-heading.comment
   (if (:author comm)
     [:a.user {:href "/"} (str "@" (:author comm) " ")])
   (:body comm)
   (if (:post-time comm) 
     [:div.clearfix.content-heading.comment.hoverhide {:style "float:right"} (format-time (:post-time comm))])
   ]
  [:hr])

(defpartial comment-list [comments]
  [:div#comments
   (map comment-item comments)])

(defpage "/topic/:id" {:keys [id]}
       (let [topic (topics/topic-get id) comments (topics/topic-get-comments id)]
         (common/site-layout
            [:h4 {:style "color:#aaa;float:right"} (:author topic)]
            [:h3 (:title topic)]
            [:p {:style "margin-top: -18px;font-size:8pt;color:#999"} (format-time (:post-time topic))]
            [:p (:body topic)]
            [:br]
            [:h4 {:style "margin-bottom:-10px;"}"Comments"]
            [:hr]
            (if (not (empty? comments))
              (comment-list comments)
              (comment-list [{:body "no comment"}]))
           (if (logged-in?)
             (form-to [:post (str "/topic/" id)]
               [:fieldset
                 [:legend "Add a Comment"]
                 (text-area {:style "width:98%;min-height:150px;"} "c")
                 (submit-button "Submit")])))))

(defpage [:post "/topic/:id"] {:keys [id c]}
  (topics/topic-add-comment id {:id (inc (cids-get-latest)) :body c :author "me"})
  (resp/redirect (str "/topic/" id)))
