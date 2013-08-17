(ns owlery.views.common
  (:require [noir.response :as resp])
  (:require [noir.session :as sess])
  (:require [owlery.models.users :as users])
  (:require [noir.util.crypt :as crypt])
  (:use [noir.core :only [defpartial defpage]]
        [owlery.util.utils]
        [clojure.string :only [split]]
        [hiccup.form :only [form-to label text-field label password-field submit-button]]
        [hiccup.page :only [include-css include-js html5]]))

(defn map-tag [tag xs]
  (map (fn [x] [tag x]) xs))

(defn strip-email-domain [email]
  (first (split email #"@")))

(defn flash! [m] (sess/flash-put! :message m) nil)
(defn refer! [ref] (sess/flash-put! :referral ref) nil)

(defn create-user [email password]
  (users/user-set-email! email email)
  (users/user-set-pass! email password)
  (sess/put! :email email)
  (users/user-get email))

(defn check-login [email password]
  (if (some empty? [email password])
    (resp/redirect "/login")
    (if-not (re-find email-regex email)
      (resp/redirect "/login")
      (if-let [user (users/user-get email)]
        (if (crypt/compare password (:pass user))
          (do
            (sess/put! :email email)
            user)
          (resp/redirect "/login"))
        (create-user email password)))))

(defn logged-in? []
  (if (sess/get :email)
    true
    false))

(defpartial site-layout [& content]
            (html5
              [:head
               [:title "owlery"]
               (include-js "http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js")
               (include-js "/js/main.js")
               (include-css "/css/style.css")
               (include-css "/css/bootstrap-responsive.css")
               (include-css "/css/bootstrap.css")]
              [:body
               [:div.container-narrow
               [:div.masthead 
                [:ul.nav.nav-pills.pull-right
                 [:li [:a {:href "/"} "Home"]]
                 [:li [:a {:href "/faq"} "FAQ"]]
                 [:li (if-not (logged-in?)
                        [:a {:href "/login"} "Login"]
                        (form-to [:post "/logout"]
                          (submit-button {:id "logout"} "Logout")))]]
                [:h3.muted "I'm Working On It"]]
                [:hr]
                [:div.marketing
                 (when-let [message (sess/flash-get :message)]
                           [:div.message [:h4#messagecontent message]])
                 content]
                [:hr]
                [:div.footer "Infringing all the copyrights."]
                ]]))

(defpartial please-login [& refer]
            [:div.nologin {:style ""}
             [:h4 "please login to post"]]
            (if refer (refer! refer))
            )

(defpartial login-form []
  (form-to [:post "/login"]
           [:fieldset {:style "margin-left: 34%"}
            [:label "email:"]
            (text-field "e")
            [:label "password:"]
            (password-field "p")
            [:br] (submit-button "Submit")]
           ))

(defpartial register-form []
  (form-to [:post "/register"]
           [:fieldset {:style "margin-left: 34%"}
            [:label "email:"]
            (text-field "re")
            [:label "password:"]
            (password-field "rp")
            [:label "re-enter password:"]
            (password-field "rp-too")
            [:br](submit-button "Submit")]))

(defpage "/logout" []
  (site-layout
    (if (logged-in?)
      [:h2 "I don't think you want to be here..."])))

(defpage [:post "/logout"] []
  (sess/remove! :email) 
  (resp/redirect "/"))

(defpage "/login" []
  (site-layout
    [:h2 {:style "text-align:center;"} "Login?"] 
    (login-form)
    [:br] [:hr] [:br]
    [:h2 {:style "text-align:center;"} "Register?"]
    (register-form)))

(defpage [:post "/login"] {:keys [e p]}
  (check-login e p)
  (flash! (str "You have logged in successfully."))
  (resp/redirect "/"))

(defpage [:post "/register"] {:keys [re rp rp-too]}
  (if (= rp rp-too) (check-login re rp)) 
  (resp/redirect "/"))


