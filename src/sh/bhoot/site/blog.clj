(ns sh.bhoot.site.blog
  (:require [hiccup.page :refer [html5]]))


(defn render [{global-meta :meta posts :entries}]
  (html5 {:lang "en" :itemtype "http://schema.org/Blog"}
    [:head
      [:title (:site-title global-meta)]
      [:meta {:charset "utf-8"}]
      [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
      [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0, user-scalable=no"}]]
    [:body
      [:h1 "Jayesh Bhoot"]
      [:ul
        [:li [:a {:href "/blog/tags/"} "Tags"]]]
      [:ul
          (for [post posts]
            [:li
              [:a {:href (:permalink post)}(:title post)]])]]))
