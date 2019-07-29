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
      [:header {:class "header"}
        [:nav {:class "nav-menu"}
          [:ul
            [:li [:a {:href "/"} "home"]]
            [:li [:a {:href "/blog"} "blog/time"]]
            [:li [:a {:href "/blog/tags"} "blog/tags"]]]]]
      [:main {:class "main"}
        [:article {:class "cell"}
          [:h3 {:class "cell-title"} "what I think I look like"]
          [:img {:src "https://www.gravatar.com/avatar/0c189999e2c7abcf648d662080912928?s=200"}]]
        [:article {:class "cell"}
          [:h3 {:class "cell-title"} "find me on"]
          [:address {:class "web-presence"}
            [:ul
              [:li [:a {:href "mailto:jayesh@bhoot.sh"} "jayesh@bhoot.sh"]]
              [:li [:a {:href "https://github.com/jayesh-bhoot"} "github"]]
              [:li [:a {:href "https://stackoverflow.com/users/663911/jayesh-bhoot"} "stack overflow"]]
              [:li [:a {:href "https://twitter.com/jayeshbhoot"} "twitter"]]
              [:li [:a {:href "https://medium.com/@jayesh.bhoot"} "medium"]]
              [:li [:a {:href "https://www.linkedin.com/in/jayesh-bhoot/"} "linkedin"]]]]]
        [:article {:class "cell"}
          [:h3 {:class "cell-title"} "blog/time"]
          [:ul {:class "article-list"}
            (for [post posts]
              [:li
                [:a {:href (:permalink post)}(:title post)]])]]]
      [:footer {:class "footer"}
        [:p
          [:span "powered by "]
          [:a {:href "https://perun.io/"} "perun "]
          [:span "and "]
          [:a {:href "https://weavejester.github.io/hiccup/"} "hiccup"]]]]))
