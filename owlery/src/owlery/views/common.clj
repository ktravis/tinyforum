(ns owlery.views.common
  (:use [noir.core :only [defpartial]]
        [markdown.core]
        [hiccup.page :only [include-css html5]]))

(defn map-tag [tag xs]
  (map (fn [x] [tag x]) xs))

(defpartial site-layout [& content]
            (html5
              [:head
               [:title "owlery"]
               (include-css "/css/style.css")
               (include-css "/css/bootstrap-responsive.css")
               (include-css "/css/bootstrap.css")]
              [:body
               [:div.container-narrow
               [:div.masthead 
                [:ul.nav.nav-pills.pull-right
                 [:li [:a {:href "/main"} "Home"]]
                 [:li [:a {:href "/faq"} "FAQ"]]
                 [:li [:a {:href "/main"} "Login"]]]
                [:h3.muted "I'm Working On It"]]
                [:hr]
                [:div.marketing content]
                [:hr]
                [:div.footer "Infringing all the copyrights."]
                ]]))
