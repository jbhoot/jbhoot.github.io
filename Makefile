.PHONY: clean
clean:
	rm -rf build

.PHONY: dev
dev:
	while true ; do \
		soupault ; \
		sleep 2 ; \
	done

.PHONY: build
build:
	soupault

.PHONY: serve
serve:
	caddy file-server --root build

.PHONY: publish
publish: 
	tar -C build -cvz . > publish.tar.gz && hut pages publish -d jysh.net publish.tar.gz && rm publish.tar.gz