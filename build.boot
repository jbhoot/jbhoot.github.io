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
  ; global-meta -> delete tasks -> content -> metadata tasks -> move tasks?
  []
  (comp
        (perun/global-metadata)
        (perun/draft)
        (perun/markdown)
        (perun/permalink)
        (perun/build-date)
        (perun/collection :renderer 'site.index/render :page "index.html")
        (perun/collection :renderer 'site.tags/render-tag-index :page "tags/index.html")
        (perun/render :renderer 'site.post/render)
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
