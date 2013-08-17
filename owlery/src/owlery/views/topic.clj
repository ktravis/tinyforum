(ns owlery.views.topic
  (:require [owlery.views.common :as common]
            [owlery.models.topics :as topics]
            [owlery.models.removal :as remo]
            [noir.response :as resp]
            [noir.session :as sess]
            [noir.content.getting-started])
  (:use [noir.core :only [defpage defpartial]]
        [owlery.views.common :only [logged-in? flash!]]
        [owlery.models.comments :only [cids-get-latest is-comment-author?]]
        [owlery.models.users :only [is-admin?]]
        [owlery.util.timing :only [format-time]]
        [hiccup.form :only [form-to label text-field label text-area submit-button]])
  )


(defpartial comment-item [comm]
  [:div.clearfix.content-heading.comment
   (if (:author comm)
     [:a.user {:href "/"} (str "@" (common/strip-email-domain (:author comm)))])
   " "
   (:body comm)
   (if (:post-time comm) 
     [:div.clearfix.content-heading.comment.hoverhide {:style "float:right"} (format-time (:post-time comm))])
   (if (and (:id comm) 
            (or (is-admin? (sess/get :email))
                (topics/is-topic-author? (:parent comm) (sess/get :email))
                (is-comment-author? (:id comm) (sess/get :email))))  
     (form-to [:post (str "/topic/" (:parent comm) "/remove/" (:id comm))]
           (submit-button {:class "removebutton"} "remove")))
   ]
  [:hr])

(defpartial comment-list [comments]
  [:div#comments
   (map comment-item comments)])

(defpage "/topic/:id" {:keys [id]}
       (let [topic (topics/topic-get id) comments (topics/topic-get-comments id)]
         (common/site-layout
            [:h4 {:style "color:#aaa;float:right"} (common/strip-email-domain (:author topic)) ]
            [:h3 (:title topic)
             (if (or (is-admin? (sess/get :email))
                   (topics/is-topic-author? id (sess/get :email)))  
               (form-to [:post (str "/topic/" id "/remove")]
                     (submit-button {:class "removebutton"} "remove")))
             ]
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
                 (submit-button "Submit")])
             (common/please-login (str "/topic/" id))
             ))))

(defpage "/topic/:id/remove" {:keys [id]}
 (common/site-layout "whoops")  
  )

(defpage [:post "/topic/:id/remove"] {:keys [id]}
  (if (logged-in?)
    (if (or (topics/is-topic-author? id (sess/get :email)) (is-admin? (sess/get :email)))
      (do (remo/remove-topic! id)      
      (flash! "Topic successfully deleted.")) 
      )
    )
  (resp/redirect "/")
  )

(defpage "/topic/:id/remove/:cid" {:keys [id cid]}
 (common/site-layout "whoops comment")  
  )

(defpage [:post "/topic/:id/remove/:cid"] {:keys [id cid]}
  (if (logged-in?)
    (if (or (topics/is-topic-author? id (sess/get :email)) (is-comment-author? cid (sess/get :email)) (is-admin? (sess/get :email)))
      (do (remo/remove-comment! cid)      
      (flash! "Comment successfully deleted.")) 
      )
    )
  (resp/redirect (str "/topic/" id))
  )

(defpage [:post "/topic/:id"] {:keys [id c]}
  (topics/topic-add-comment id {:id (inc (cids-get-latest)) :parent id :body c :author (sess/get :email)})
  (resp/redirect (str "/topic/" id)))
