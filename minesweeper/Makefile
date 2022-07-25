FLAGS=-cp .:lib/$(LIBS)

autoSolve.class:  ./MineSweeperLogic.java ./Field.java ./Adjacent.java
	javac $(FLAGS) MineSweeperLogic.java
run: autoSolve.class ./refact.class
	java $(FLAGS) refact
clean:
	rm *.class
