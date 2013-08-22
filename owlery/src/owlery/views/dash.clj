(ns owlery.views.dash
  (:require [noir.response :as resp])
  (:require [noir.session :as sess])
  (:require [owlery.views.common :as common])
  (:use [noir.core :only [defpage]])
  (:use [owlery.models.users :only [is-admin? users-get-all user-promote! user-demote!]]))


(defpage "/manage" []
         (if-not (common/logged-in?) (resp/redirect "/login"))
         (common/site-layout
           (if (is-admin? (sess/get :email))
             [:table.table.table-hover
              [:thead {:style "color:#777"} [:tr [:th "users"] [:th "admin?"]]]
              (for [u (users-get-all)]
                [:tr [:th u] [:th (is-admin? u)]])]))) 

(defpage [:post "/promote"] {:keys [email]}
  (if-not (is-admin? (sess/get :email)) (resp/redirect "/"))
  (user-promote! email)
  (common/flash! (str "User " email " has been promoted to an administrator."))
  (resp/redirect "/manage"))

(defpage [:post "/demote"] {:keys [email]}
  (if-not (is-admin? (sess/get :email)) (resp/redirect "/"))
  (user-demote! email)
  (common/flash! (str "User " email " has been demoted to a normal user."))
  (resp/redirect "/manage"))
