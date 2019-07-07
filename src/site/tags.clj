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