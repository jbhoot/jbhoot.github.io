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
      [:header
        [:nav
          [:ul {:class "nav-menu"}
            [:li [:a {:href "/blog"} "blog/dates"]]
            [:li [:a {:href "/blog/tags"} "blog/tags"]]]]]
      [:main
        [:article
          [:h3 "what I think I look like"]
          [:img {:src "https://www.gravatar.com/avatar/0c189999e2c7abcf648d662080912928?s=200"}]]
        [:article 
          [:h3 "thoughts in words"]
          [:ul
            (for [post posts]
              [:li
                [:a {:href (:permalink post)}(:title post)]])]]
        [:article 
          [:address
            [:h3 "find me on"]
            [:ul
              [:li [:a {:href "mailto:jayesh@bhoot.sh"} "jayesh@bhoot.sh"]]
              [:li [:a {:href "https://github.com/jayesh-bhoot"} "github"]]
              [:li [:a {:href "https://stackoverflow.com/users/663911/jayesh-bhoot"} "stack overflow"]]
              [:li [:a {:href "https://twitter.com/jayeshbhoot"} "twitter"]]
              [:li [:a {:href "https://medium.com/@jayesh.bhoot"} "medium"]]
              [:li [:a {:href "https://www.linkedin.com/in/jayesh-bhoot/"} "linkedin"]]]]]]
      [:footer
        [:span "Powered by perun and hiccup"]]]))
