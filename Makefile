app = mp
jar = build/libs/$(app)-1.0-SNAPSHOT-all.jar

$(app): $(jar)
	native-image --report-unsupported-elements-at-runtime -jar $(jar) bin/$(app) --no-server

$(jar):
	./gradlew build

clean:
	rm $(app) $(jar)
