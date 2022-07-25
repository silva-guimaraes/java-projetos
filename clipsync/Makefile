

client-class: ./clipsyncClient.java	
	javac clipsyncClient.java

run: client-class
	java clipsyncClient 

build: client-class ./clipsyncClient.mf
	jar cmf clipsyncClient.mf clipclient.jar clipsyncClient.class clipsyncClient.java 
	chmod 777 clipclient.jar

server: ./clipsync-server.java
	javac clipsync-server.java
	java clipsyncServer
clean:
	rm *.class
