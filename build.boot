(set-env!
  ; https://github.com/boot-clj/boot/wiki/Boot-Environment#env-keys
  ; use either :resource-paths or :asset-paths, for files which are to be emitted as final artifacts,
  :source-paths #{"src"}
  :resource-paths #{"resources"}
  :dependencies '[[perun "0.4.2-SNAPSHOT"]
                  [hiccup "1.0.5" :exclusions [org.clojure/clojure]]
                  [pandeiro/boot-http "0.7.6" :exclusions [org.clojure/clojure]]
                  ;; tools.nrepl can be removed after
                  ;; https://github.com/pandeiro/boot-http/pull/61
                  ;; is merged
                  [org.clojure/tools.nrepl "0.2.11" :exclusions [org.clojure/clojure]]])
  
(require  '[clojure.string :as str]
          '[io.perun :as perun]
          '[sh.bhoot.site.index :as index-view]
          '[sh.bhoot.site.post :as post-view]
          '[pandeiro.boot-http :refer [serve]])

(deftask build
  "Build test blog. This task is just for testing different plugins together."
  ; global-meta -> delete tasks -> content -> metadata tasks -> move tasks?
  []
  (comp
   (perun/global-metadata)
   (perun/draft)
   (perun/build-date)
   (perun/markdown :out-dir "public/blog")
   (perun/permalink)
   (perun/collection :renderer 'sh.bhoot.site.index/render :page "index.html")
   (perun/collection :renderer 'sh.bhoot.site.blog/render :page "blog/index.html")
   (perun/collection :renderer 'sh.bhoot.site.tags/render-tag-index :page "blog/tags/index.html")
   (perun/sitemap)
   (target)
   (notify)))

(deftask dev
  []
  (comp (watch :verbose true)
        (build)
        (serve :resource-root "public")))
