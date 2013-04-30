all: net1.class
 
net1.class: net1/net1.java
	javac net1/*.java httpfoundation/*.java httpclient/*.java httpserver/*.java
 
run: net1.class
	java net1/net1
