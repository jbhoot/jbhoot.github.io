.PHONY: build
build:
	ls soupault.toml site/**/*.* templates/**/*.* | entr -s 'soupault'

.PHONY: serve
serve:
	caddy file-server --root build

.PHONY: publish
publish: 
	tar -C build -cvz . > publish.tar.gz && hut pages publish -d jysh.net publish.tar.gz && rm publish.tar.gz