jar       = $(CURDIR)/otto-standalone.jar
deps      = $(CURDIR)/deps
curl      = curl -fso
bootstrap = 3.1.1
lein      = 2.3.4
jdk       = 8
jquery    = 2.1.0

# Bundle a JDK on Linux.
ifeq ($(shell uname -s),Linux)
	jdk_deps = $(deps)/jdk-$(jdk)
	jdk_lein = PATH=$(deps)/jdk-$(jdk)/bin:$(PATH) $(deps)/lein-$(lein)
	jre_deps = $(CURDIR)/jre
else
	jdk_deps =
	jdk_lein = $(deps)/lein-$(lein)
	jre_deps =
endif

# Ensure the uberjar is built unconditionally.
.PHONY: $(jar)

$(jar): $(jdk_deps) $(jre_deps) $(deps)/lein-$(lein) resources/public/bootstrap.min.css resources/public/jquery.min.js
	$(jdk_lein) uberjar && \
		install target/otto-standalone.jar $@

clean: $(deps)/lein-$(lein)
	$(jdk_lein) clean && \
		rm -rf resources/public/bootstrap.min.css \
			resources/public/jquery.min.js \
			$(jar)

mrproper: clean
	rm -rf $(deps) $(jre_deps)

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

$(deps)/jdk-$(jdk): $(deps)
	mkdir -p $@ && \
		curl -s -L -C - -b "oraclelicense=accept-securebackup-cookie" \
			http://download.oracle.com/otn-pub/java/jdk/8-b132/jdk-$(jdk)-linux-x64.tar.gz | \
		tar xz -C $@ --strip-components=1

$(CURDIR)/jre: $(deps)/jdk-$(jdk)
	rm -rf $@
	cp -r $</jre $@
	touch $@

$(deps):
	mkdir -p $(deps)

# Build tool specific target.
build: $(jar)
