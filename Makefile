.PHONY: clean
clean:
	rm -rf build/*

.PHONY: build-dev
build-dev:
	soupault

.PHONY: dev
dev:
	 find . -type f \( -path './index_processors/*' -o -path './plugins/*' -o -path './scripts/*' -o -path './site/*' -o -path './templates/*' -o -path './Makefile' -o -path './soupault.toml' \) ! -name '.DS_Store' | entr -s 'make clean && make build-dev'

.PHONY: serve
serve:
	caddy file-server --root build

.PHONY: build-prod
build-prod:
	soupault --profile production
