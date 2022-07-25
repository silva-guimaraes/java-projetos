import java.util.Arrays.*;
import java.util.Scanner;

class Table{
	char[] table; 
	Table(){
		this.table = new char[9];
	} 
	public boolean isWin(char y){
		int chance = 0;
		final Integer[][] lookUp = {	{1,2,3},
						{4,5,6},
						{7,8,9},
						{1,4,7},
						{2,5,8},
						{3,6,9},
						{1,5,9},
						{3,5,7}}; 

		for (int i = 0; i < lookUp.length; ++i){ 
			int z = table[lookUp[i][0] - 1] + table[lookUp[i][1] - 1 ] + table[lookUp[i][2] - 1];
			if ( z == y * 3)
				return true; 
			if (z == y * 2) 
				chance++; 
		}
		if (chance >= 2) return true; 
		return false;
	}

	public void printTable(){
		System.out.println(table[0] + "  |  " + table[1] + "  |  " + table[2]);
		System.out.println("------------");
		System.out.println(table[3] + "  |  " + table[4] + "  |  " + table[5]);
		System.out.println("------------");
		System.out.println(table[6] + "  |  " + table[7] + "  |  " + table[8]);
	}

	public void placeMark(char y){
		Scanner input = new Scanner(System.in);

		System.out.println("from left to right, top to bottom, ");
		System.out.println("select using numbers 1 to 9 where to place your mark.");

		while(true){
			int x = input.nextInt();
			if (x <= 9 && table[x - 1] == 0){
				table[x - 1] = y;
				return; 
			}
			else System.out.println("invalid input"); 
		}
	}

	public boolean checkToe(){
		for (int i : table)
			if ( i == 0 ) return false;
		return true;
	}
}


class tictac{
	public static void main(String[] agrs){
		Table newGame = new Table();

		while(true){
			System.out.println("\n\n\n");
			System.out.println("X turn: ");
			newGame.printTable();
			newGame.placeMark('X');
			if (newGame.isWin('X')){
				newGame.printTable();
				System.out.println("X victory!");
				break;
			} 
			else if (newGame.checkToe()){
				newGame.printTable();
				System.out.println("it's a tie!");
				break;
			}

			System.out.println("\n\n\n");
			System.out.println("O turn: "); 
			newGame.printTable();
			newGame.placeMark('O');
			if (newGame.isWin('O')){
				newGame.printTable();
				System.out.println("O victory!");
				break;
			} 
			else if (newGame.checkToe()){
				newGame.printTable();
				System.out.println("it's a tie!");
				break;
			}
		}
	}
}
