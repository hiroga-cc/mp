app = mp
bin = bin/$(app)
jar = build/libs/$(app)-1.0-SNAPSHOT-all.jar

$(app): $(jar)
	native-image --report-unsupported-elements-at-runtime -jar $(jar) $(bin) --enable-https --no-server

$(jar):
	./gradlew build

clean:
	rm $(bin) $(jar)
