(ns site.index
  (:use [hiccup.core :only (html)]
        [hiccup.page :only (html5)]))


(defn render [{global-meta :meta posts :entries}]
  (html5 {:lang "en" :itemtype "http://schema.org/Blog"}
    [:head
      [:title (:site-title global-meta)]
      [:meta {:charset "utf-8"}]
      [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
      [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0, user-scalable=no"}]]
    [:body
      [:h1 "Ghosthouse"]
      [:ul
        [:li [:a {:href "/me"} "About Me"]]
        [:li [:a {:href "/jrnl"} "Blog"]]
        [:li [:a {:href "/tags"} "Tags"]]
        [:li [:a {:href "/feed.rss"} "RSS"]]
        [:li [:a {:href "/atom.xml"} "Atom Feed"]]]
      [:ul
          (for [post posts]
            [:li
              [:a {:href (:permalink post)}(:title post)]])]]))
