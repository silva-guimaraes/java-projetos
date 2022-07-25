import java.awt.*;
import java.awt.event.*;
import javax.swing.*; 

public class Board extends JPanel { 

    public static Space[][] grid;
    //todo: descobrir de onde vem aquele cavalo fantasma em (0,0)
    public static Knight knight;
    public static boolean isFirstMove;

    public Board(){
	isFirstMove = true;
	int foo = 8;
	this.grid = new Space[foo][foo];

	initGrid();
	gameLoop();
	repaintGrid(Casa.verde);
    } 
    public void initGrid(){
	for (int i = 0; i < grid.length; ++i){ 
	    for (int j = 0; j < grid[0].length; ++j){ 
		grid[i][j] = new Space(i, j); 
	    }
	} 
    }
    public void resetGrid(){
	for (int i = 0; i < grid.length; ++i){ 
	    for (int j = 0; j < grid[0].length; ++j){ 
		grid[i][j].taken = false; 
		grid[i][j].fill = null; 
	    }
	} 
	knight.resetPosition();
	isFirstMove = true;
	repaintGrid(Casa.verde);
    }
    public static void repaintGrid(Color fill){
	for (int i = 0; i < grid.length; ++i){ 
	    for (int j = 0; j < grid[0].length; ++j){ 
		grid[i][j].fill = fill; 
		grid[i][j].repaint(); 
	    }
	} 
    }
    private void hey(){
	System.out.println("hey there i am using whatsapp"); //debug
    } 
    public Space[][] returnGrid(){
	return grid;
    } 
    public int returnGridLength(){
	return grid.length;
    } 
    public static void setKnightPosition(int x, int y){ 
	if (isFirstMove){
	    knight = new Knight(x, y); 
	    knight.moveKnight(x, y); 
	    isFirstMove = false;
	}
	else if (isValidMove(x, y)) { 
	    knight.moveKnight(x, y); 
	}
	else System.out.println("invalid move");
	
	gameLoop();
    } 
    public static void gameLoop(){ 
	repaintGrid(null);
	highlightSpace(); 

	switch(winCondition()){
	    case 1:
		System.out.println("you are left with no available moves.\ngame is over.");
		//System.exit(0);
		break;
	    case 2:
		System.out.println("you win =)");
		break;
	    default:
		break; 
	}
    } 
    public static void highlightSpace(){
	int i = 0;
	while (i < knight.possibleMoves.length){
	    int x = knight.positionX + knight.possibleMoves[i][1];
	    int y = knight.positionY + knight.possibleMoves[i][0];

	    if (isValidMove(x, y)){
		grid[y][x].fill = Space.verde; 
		grid[y][x].repaint();
	    }
	    ++i;
	}
    }
    public static boolean isValidMove(int x, int y){
	if ( insideBounds(x, y) == false )
	    return false;
	else if ( knight.canMove(y, x) == false )
	    return false;
	else if ( grid[y][x].taken == true )
	    return false;
	else 
	    return true; 
    } 
    //private static void moveKnight(int x, int y){
    //    grid[y][x].takeSpace(knight.moveCount + 1);
    //    grid[knight.positionY][knight.positionX].repaint();
    //    grid[y][x].repaint();
    //    knight.setPosition(x, y);
    //    knight.moveCount += 1; 
    //    System.out.println("knight pos: " + knight.positionX + ", " + knight.positionY); 
    //}
    private static int winCondition(){
	if (leftMoves() == 0)
	    return 1;
	else if (checkClear() == true)
	    return 2; 
	else return 0;
    } 
    private static int leftMoves(){
	int ret = 0;
	int i = 0;
	while ( i < knight.possibleMoves.length ) {
	    int y = knight.positionY + knight.possibleMoves[i][0];
	    int x = knight.positionX + knight.possibleMoves[i][1];

	    if ( isValidMove(x, y) ){ 
		++ret;
	    }
	    ++i;
	}
	return ret;
    } 
    private static boolean checkClear(){
	for (int i = 0; i < grid.length; ++i){
	    for (int j = 0; j < grid[0].length; ++j){
		if (grid[i][j].taken == false)
		    return false;
	    }
	} 
	return true; 
    } 
    //private void autoMove(){ 
    //    int i = 0;
    //    while ( i < knight.possibleMoves.length ){
    //        int y = knight.positionY + knight.possibleMoves[i][0];
    //        int x = knight.positionX + knight.possibleMoves[i][1];

    //        if ( isValidMove(x, y) )
    //    	setKnightPosition(x, y);
    //        ++i;
    //    }
    //} 
    private static boolean insideBounds(int x, int y){
	if (y >= grid.length || y < 0)
	    return false;
	else if (x >= grid[0].length || x < 0)
	    return false;
	else 
	    return true;
    } 
} 
class Space extends Casa { 
    protected int whenTaken; 
    public final int positionX;
    public final int positionY;

    Space(int i, int j){
	taken = false;
	this.positionY = i; 
	this.positionX = j;
    } 
    public void takeSpace(int move){
	System.out.println("taken: " + positionX + ", " + positionY);
	taken = true;
	this.whenTaken = move; 
	repaint();
    }
    @Override
    public void paintComponent(Graphics g){
	super.paintComponent(g);
	Graphics2D g2d = (Graphics2D) g; 
	//todo: consertar esse stroke
	g2d.setStroke(new BasicStroke(3));


	if (knightPresent()){
	    fill = null;
	    g2d.drawString("K", 20,20);
	    g2d.draw(rect);

	    //emptySpace(g2d, rect);
	}
	else if(taken){
	    g2d.setPaint(Color.lightGray);
	    g2d.fill(rect);

	    g2d.setPaint(Color.black);
	    g2d.drawString(Integer.toString(whenTaken), 20,20);
	}
	else emptySpace(g2d, rect);
    }
    @Override
    public void mousePressed(MouseEvent e){
	if(knightPresent()){
	    System.out.println("knight present"); 
	}
	if (isSpaceAvailable()){
	    Board.setKnightPosition(positionX, positionY);
	}
    } 
    @Override
    public void mouseEntered(MouseEvent e){
	System.out.println(positionX + ", " + positionY + " - " + fill);
	if (fill == verde){  
	    this.fill = Color.green;
	    repaint();
	}
	else if (isSpaceAvailable()){
	    this.fill = cinza;
	    repaint(); 
	}
    }
    @Override
    public void mouseExited(MouseEvent e){
	if (fill == verde || fill == Color.green){  
	    this.fill = verde;
	    repaint();
	}
	else if (isSpaceAvailable()){
	    this.fill = null;
	    repaint(); 
	}
    }
    @Override
    public void mouseReleased(MouseEvent e){
	repaint();
    } 
    @Override
    public boolean isSpaceAvailable(){
	if (taken == false && knightPresent() == false)
	    return true;
	else return false;
    }
    private boolean knightPresent(){
	if (positionX == Board.knight.positionX 
		&& positionY == Board.knight.positionY )
	    return true;
	else return false;
    } 
} 
class Knight {
    protected static final int[][] possibleMoves = { 	
	{-2, -1},
	{-2, 1},
	{-1, 2},
	{1, 2},
	{2, 1},
	{2, -1},
	{1, -2},
	{-1, -2}, }; 

    public static int positionX; 
    public static int positionY;
    public static int moveCount; 

    //protected Knight(){}
    protected Knight(int x, int y){
	positionX = x;
	positionY = y; 
	moveCount = 0;
    } 
    public static void setPosition(int x, int y){
	positionX = x;
	positionY = y;
    }
    public static boolean canMove(int y, int x){ 
	for (int i = 0; i < possibleMoves.length; ++i){
	    if (y == positionY + possibleMoves[i][0] && x == positionX + possibleMoves[i][1]){ 
		return true;
	    }
	} 
	return false;
    } 
    protected static void moveKnight(int x, int y){
	Board.grid[y][x].takeSpace(moveCount + 1);

	setPosition(x, y);
	Board.grid[positionY][positionX].repaint();

	moveCount += 1; 
	System.out.println("knight pos: " + positionX + ", " + positionY); 
    }
    public void resetPosition(){
	positionX = -1;
	positionY = -1;
	moveCount = -1; 
    }
}
