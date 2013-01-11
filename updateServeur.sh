mvn clean install assembly:single -DskipTests=true
if [ $? -eq 0 ]
then
	cp scripts/* ../CodeStory-server/
	cp target/code-story.jar ../CodeStory-server/code-story.jar.new
	cd ../CodeStory-server
	./stopServeur.sh
	mv code-story.jar code-story.jar.old
	mv code-story.jar.new code-story.jar
	./startServeur.sh
	sleep 1
	tail -10 serveur.log
fi

