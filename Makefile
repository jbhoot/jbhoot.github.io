.PHONY: clean
clean:
	rm -rf build/*

.PHONY: build-dev
build-dev:
	./soupault

.PHONY: dev
dev:
	ls soupault.toml templates/* site/**/*.* | entr -s 'make clean && make build-dev'

.PHONY: serve
serve:
	caddy file-server --root build

.PHONY: build-prod
build-prod:
	./soupault --profile production