(ns tinyforum.views.faq
  (:require [tinyforum.views.common :as common])
  (:require [noir.session :as sess])
  (:require [noir.response :as resp])
  (:require [tinyforum.models.static :as static])
  (:require [tinyforum.models.users :as users])
  (:use [markdown.core]
        [hiccup.form :only [form-to text-area submit-button]]) 
  (:use [noir.core :only [defpage]]))


(defpage "/faq" []
         (sess/put! :referral "/faq")
         (common/site-layout
           [:div.jumbotron
            [:h1 "FAQ"]
            [:p.lead
             (md-to-html-string (static/get-faq))]
            (if (users/is-admin? (sess/get :email))
             [:a {:style "float:right"
                  :href (str "/faq/edit")} "edit"])]))

(defpage "/faq/edit" []
         (sess/put! :referral "/faq")
         (common/site-layout
           [:div.jumbotron
            [:h1 "FAQ"]
            [:p.lead 
             ]
            (if (users/is-admin? (sess/get :email))
             (form-to [:post "/faq/edit"]
               (text-area {:style "width:97%;min-height:250px"}
                          "editfaq" (static/get-faq))
                      (submit-button "Submit"))
              (common/please-login))]))

(defpage [:post "/faq/edit"] {:keys [editfaq]} 
  (if (users/is-admin? (sess/get :email))
    (do (static/set-faq! editfaq)
        (common/flash! "FAQ successfully edited.")))
  (resp/redirect "/faq"))
