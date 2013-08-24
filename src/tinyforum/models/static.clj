(ns tinyforum.models.static
  (:use [tinyforum.models.client]))

(def r @client)

(defn set-faq! [new-faq]
  (r [:set "FAQ" new-faq]))

(defn get-faq []
  (r [:get "FAQ"]))

(defn set-masthead! [new-masthead]
  (r [:set "MASTHEAD" new-masthead]))

(defn get-masthead []
  (r [:get "MASTHEAD"]))

(defn set-banner! [new-banner]
  (r [:set "BANNER" new-banner]))

(defn get-banner []
  (r [:get "BANNER"]))

(defn set-lead! [new-lead]
  (r [:set "LEAD" new-lead]))

(defn get-lead []
  (r [:get "LEAD"]))

(defn set-footer! [new-footer]
  (r [:set "FOOTER" new-footer]))

(defn get-footer []
  (r [:get "FOOTER"]))
