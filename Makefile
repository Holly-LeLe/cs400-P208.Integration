compile:
	javac -cp .:$$HOME/junit5.jar *.java

test:
	java -jar $$HOME/junit5.jar -cp . --scan-class-path

startServer:
	java WebApp 8080

clean:
	rm -f *.class
