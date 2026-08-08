compile:
	javac -cp .:$$HOME/junit5.jar *.java

startServer:
	javac -cp .:$$HOME/junit5.jar *.java
	java WebApp 8080

runAllTests:
	javac -cp .:$$HOME/junit5.jar *.java
	java -jar $$HOME/junit5.jar -cp . --scan-class-path

clean:
	rm -f *.class
