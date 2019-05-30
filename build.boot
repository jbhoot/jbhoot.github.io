(set-env!
  ; https://github.com/boot-clj/boot/wiki/Boot-Environment#env-keys
  ; for files which are to be emitted as final artifacts,
  ; use either :resource-paths or :asset-paths, depending on usage given in the docs.
  :source-paths #{"src" "content"}
  :dependencies '[[perun "0.4.3-SNAPSHOT" :scope "test"]
                  [hiccup "1.0.5" :exclusions [org.clojure/clojure]]
                  [pandeiro/boot-http "0.8.3" :exclusions [org.clojure/clojure]]])

(require '[clojure.string :as str]
         '[io.perun :as perun]
         '[site.index :as index-view]
         '[site.post :as post-view]
         '[pandeiro.boot-http :refer [serve]])

(deftask build
  "Build test blog. This task is just for testing different plugins together."
  []
  (comp
        (perun/global-metadata)
        (perun/markdown)

        (perun/draft)
        (perun/permalink)

        (perun/ttr)
        (perun/word-count)
        (perun/build-date)
        (perun/gravatar :source-key :author-email :target-key :author-gravatar)
        
        (perun/render :renderer 'site.post/render)
        (perun/collection :renderer 'site.index/render :page "index.html")
        (perun/tags :renderer 'site.tags/render)
        (perun/paginate :renderer 'site.paginate/render)
        (perun/assortment :renderer 'site.assortment/render
                          :grouper (fn [entries]
                                     (->> entries
                                          (mapcat (fn [entry]
                                                    (if-let [kws (:keywords entry)]
                                                      (map #(-> [% entry]) (str/split kws #"\s*,\s*"))
                                                      [])))
                                          (reduce (fn [result [kw entry]]
                                                    (let [path (str kw ".html")]
                                                      (-> result
                                                          (update-in [path :entries] conj entry)
                                                          (assoc-in [path :entry :keyword] kw))))
                                                  {}))))
        (perun/static :renderer 'site.about/render :page "me/index.html")
        
        (perun/sitemap)
        (perun/rss :description "bhoot.sh blog")
        (perun/atom-feed :filterer :original)

        (target)
        (notify)))

(deftask dev
  []
  (comp (watch :verbose true)
        (build)
        (serve :resource-root "public")))
