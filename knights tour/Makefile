CLASSPATH=./:
FLAGS=-cp $(CLASSPATH) -Xdiags:verbose 

mainBody.class: mainBody.java Board.java
	javac $(FLAGS) mainBody.java
run: mainBody.class ./Board.class
	java mainBody
jar: mainBody.class
	jar cfm knights.jar MANIFEST.MF *.class
	chmod +x knights.jar
clean: 
	rm *class
