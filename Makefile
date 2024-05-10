.PHONY: clean
clean:
	rm -rf build/*

.PHONY: build-dev
build-dev:
	./soupault

.PHONY: build-prod
build-prod:
	./soupault --profile production

.PHONY: dev
dev:
	while true ; do \
		rm -rf build/* ; \
		./soupault ; \
		sleep 2 ; \
	done

.PHONY: build
build:
	./soupault

.PHONY: serve
serve:
	caddy file-server --root build

.PHONY: deploy
deploy:
	tar -C build -cvz . > publish.tar.gz \
	&& hut pages publish -d jysh.net publish.tar.gz \
	&& rm publish.tar.gz

.PHONY: publish
publish : clean build-prod deploy
