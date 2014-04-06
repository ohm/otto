jar       = $(CURDIR)/otto-standalone.jar
deps      = $(CURDIR)/deps
curl      = curl -fso
bootstrap = 3.1.1
lein      = 2.3.4
jquery    = 2.1.0

# Ensure the uberjar is built unconditionally.
.PHONY: $(jar)

$(jar): $(deps)/lein-$(lein) resources/public/bootstrap.min.css resources/public/jquery.min.js
	$< uberjar && \
		install target/otto-standalone.jar $@

clean: $(deps)/lein-$(lein)
	$< clean && \
		rm -rf resources/public/bootstrap.min.css \
			resources/public/jquery.min.js \
			$(jar)

mrproper: clean
	rm -rf $(deps)

resources/public/bootstrap.min.css: $(deps)/bootstrap-$(bootstrap).min.css
	install $< $@

resources/public/jquery.min.js: $(deps)/jquery-$(jquery).min.js
	install $< $@

$(deps)/bootstrap-$(bootstrap).min.css: $(deps)
	$(curl) $@ http://netdna.bootstrapcdn.com/bootstrap/$(bootstrap)/css/bootstrap.min.css

$(deps)/lein-$(lein): $(deps)
	$(curl) $@ https://raw.github.com/technomancy/leiningen/$(lein)/bin/lein && \
		chmod +x $@

$(deps)/jquery-$(jquery).min.js: $(deps)
	$(curl) $@ http://code.jquery.com/jquery-$(jquery).min.js

$(deps):
	mkdir -p $(deps)

# Build tool specific target.
build: $(jar)
