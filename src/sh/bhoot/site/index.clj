(ns sh.bhoot.site.index
  (:use [hiccup.page :only (html5 include-css)]))


(defn render [{global-meta :meta posts :entries}]
  (html5 {:lang "en" :itemtype "http://schema.org/Blog"}
    [:head
      [:title (:site-title global-meta)]
      [:meta {:charset "utf-8"}]
      [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
      [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0, user-scalable=no"}]
      (include-css "/styles.css")]
    [:body
      [:img {:src "https://www.gravatar.com/avatar/0c189999e2c7abcf648d662080912928?s=200"}]
      [:ul
        [:li [:a {:href "/blog"} "Blog"]]
        [:li [:a {:href "/blog/tags"} "Tags"]]]
      [:ul
          (for [post posts]
            [:li
              [:a {:href (:permalink post)}(:title post)]])]
      [:address
        [:h2 "Find me on"]
        [:ul
          [:li [:a {:href "mailto:jayesh@bhoot.sh"} "jayesh@bhoot.sh"]]
          [:li [:a {:href "https://github.com/jayesh-bhoot"} "GitHub"]]
          [:li [:a {:href "https://stackoverflow.com/users/663911/jayesh-bhoot"} "Stack Overflow"]]
          [:li [:a {:href "https://twitter.com/jayeshbhoot"} "Twitter"]]
          [:li [:a {:href "https://medium.com/@jayesh.bhoot"} "Medium"]]
          [:li [:a {:href "https://www.linkedin.com/in/jayesh-bhoot/"} "LinkedIn"]]]]]))
