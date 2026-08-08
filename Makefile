compile:
	javac -cp .:$$HOME/junit5.jar *.java

startServer:
	java WebApp 8080

runAllTests:
	java -jar $$HOME/junit5.jar -cp . --scan-class-path

clean:
	rm -f *.class
