(ns site.tags
  (:require [hiccup.page :refer [html5]]))

(defn render-tag-index [{:keys [meta entries]}]
  (let [posts-by-tags
        (reduce
         (fn [acc post]
           (reduce
            (fn [acc keyword]
              (-> acc
                  (update-in [keyword :n] (fnil inc 0))
                  (update-in [keyword :posts] (fnil conj []) post)))
            acc
            (:tags post)))
         {}
         entries)]
    (html5
     {:lang "en" :itemtype "http://schema.org/Blog"}
     [:body
      [:div.main
       [:div.container
        [:h1 "Tags"]
        [:ul.tag-cloud
         (for [[tag {:keys [n]}] posts-by-tags]
           [:li.tag
            [:a {:href (str "#" tag)} tag]])]

        (for [[tag {:keys [posts]}] posts-by-tags]
          [:div
           [:h3 [:a {:name tag}] tag]
           [:ul
            (for [{:keys [permalink title]} (sort-by :name posts)]
              [:li [:a {:href permalink} title]])]])]]])))

(defn render-tag-page [{global-meta :meta posts :entries entry :entry}]
  (html5 {:lang "en" :itemtype "http://schema.org/Blog"}
         [:head
          [:title (str (:site-title global-meta) "|" (:tag entry))]
          [:meta {:charset "utf-8"}]
          [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
          [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0, user-scalable=no"}]]
         [:body
          [:h1 (:title entry)]
          [:ul.items.columns.small-12
           (for [post posts]
             [:li (:title post)])]]))
