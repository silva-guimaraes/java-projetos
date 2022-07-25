import java.awt.*;
import java.util.Random;
import java.util.Date;
import javax.swing.*;
import javax.swing.event.*; 
import java.awt.event.*;
import java.util.concurrent.TimeUnit;


//todo: arraylist

class Settings {
    public static final boolean GOD_MODE 	= false; //debug
    public static final boolean SHOW_COVER 	= true; //debug
    public static final int BOARD_SIZE_Y 	= 20;
    public static final int BOARD_SIZE_X 	= 30;
    public static final int FIELD_SIZE 		= 30;
    public static final int MINE_COUNT 		= 120;
    public static final int SAFE_FIELDS 	= BOARD_SIZE_X * BOARD_SIZE_Y - MINE_COUNT;

} 

class refact extends JFrame implements Content { 


    refact()
    { 
	MineSweeperLogic jpane = new MineSweeperLogic();
	setLayout(new FlowLayout());
	add(jpane);
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	setVisible(true);
	setSize(500,500);
	setTitle("teste"); 
	setMinimumSize(new Dimension(601,601));

    }
    public static void main(String[] args){
	run();
    }
    public static void run(){
	SwingUtilities.invokeLater(new Runnable(){
	    public void run(){
		new refact();
	    }
	}); 
    } 
} 
class MineSweeperLogic extends AutoSolve implements Content {
    public static void main(String[] args){
	refact.run();
    }

    //protected static final 	int RARITY = 570; //de 0 a 1000
    protected static boolean 	firstMove;
    protected static boolean 	lockInput;
    protected static int 	leftFlags;
    protected static JPanel 	gameGrid;
    protected static JPanel 	buttonPanel;
    public static int 		fieldsLeft = 0; 

    JButton buttonteste 	= new JButton("reiniciar");
    JButton solve 		= new JButton("resolver");
    JButton revertbutton 	= new JButton("reverter");
    //JButton revertbutton 	= new JButton("revert");

    protected static Field[][] board = new Field[Settings.BOARD_SIZE_Y][Settings.BOARD_SIZE_X];
    protected static Field[][] revert;

    /* constructor begin*/
    MineSweeperLogic()
    { 
	this.setLayout(new GridLayout(2, 1));
	
	//nota: tamanho?
	this.setMinimumSize(new Dimension(
		    Settings.FIELD_SIZE + 10 * Settings.BOARD_SIZE_X,
		    Settings.FIELD_SIZE + 10 * Settings.BOARD_SIZE_Y));
	
	/*campos*/
	gameGrid = new JPanel();
	gameGrid.setMinimumSize(new Dimension(
		    Settings.FIELD_SIZE + 10 * Settings.BOARD_SIZE_X,
		    Settings.FIELD_SIZE + 10 * Settings.BOARD_SIZE_Y)); 
	gameGrid.setLayout(new GridLayout(Settings.BOARD_SIZE_Y,Settings.BOARD_SIZE_X)); 

	initializeBoard();
	seedMines(Settings.MINE_COUNT);
	setRevert(board);
	/*campos*/

	/* botoes */
	buttonPanel = new JPanel(new FlowLayout());
	buttonPanel.add(buttonteste);
	buttonPanel.add(solve);
	buttonPanel.add(revertbutton); 

	buttonteste.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e){
		//setRevert(board);
		firstMove = true; fieldsLeft = Settings.SAFE_FIELDS; 
		lockInput = false;
		gameGrid.repaint(); //repintaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa todo
		initializeBoard(); 
		seedMines(Settings.MINE_COUNT); 
		gameGrid.repaint(); //repintaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa todo
	    }
	}); 
	solve.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e){
		findOutliers();
		uncoverSafeFields();
		checkWin();
	    }
	}); 
	revertbutton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e){
		revertMove();
	    }
	}); 
	/* botoes */ 

	this.add(gameGrid);
	this.add(buttonPanel); 


	fieldsLeft = Settings.SAFE_FIELDS;
	lockInput = false;
	this.firstMove = true;

    }
    /* constructor end*/ 
    public void initializeBoard() {

	gameGrid.removeAll();

	for (int i = 0; i < Settings.BOARD_SIZE_Y; ++i) {
	    for (int j = 0; j < Settings.BOARD_SIZE_X; ++j)
	    {
		board[i][j] = new Field(j, i);
		board[i][j].addFieldListener(new ReceiveInput()); 
		gameGrid.add(board[i][j]);
	    }
	} 
	repaint();
    }
    public void seedMines(int mineAmount){
	int totalMines = mineAmount;
	Random random = new Random(new Date().getTime());

	while ( totalMines > 0 ){
	    int randY = Math.abs(random.nextInt() % Settings.BOARD_SIZE_Y);
	    int randX = Math.abs(random.nextInt() % Settings.BOARD_SIZE_X);

	    Field field = board[randY][randX]; 

	    if (field.hasMine == false && field.isCovered == true){ 
		field.plantMine();
		updateAdjacentFields(randX, randY);
		--totalMines;
	    }
	}
    }
    class ReceiveInput implements FieldListener { 
	public void actionPerformed(FieldEvent e) 
	{ 
	    if (lockInput == false){
		revealField(e.getX(), e.getY());

		checkWin(); 
	    }
	}
    }
    public void revealField(int x, int y){

	Field field = board[y][x];

	    if (firstMove)
		displaceFirstMine(x, y); 
	    firstMove = false;

	    if(field.returnFlag() == true)
		return;

	switch(field.hasUnder())
	{
	    case Content.BLANK:
		setRevert(board);
		revealBlankField(x, y);
		break;

	    case Content.MINE:
		field.uncover(true);
		if (Settings.GOD_MODE == true)
		    break;
		else { 
		    setRevert(board);
		    System.out.println("mine struck. game is over"); 
		    lockInput = true;
		    break;
		} 
	    default:
		setRevert(board);
		field.uncover(true);
		break;
	} 
    }
    private void displaceFirstMine(int x, int y){

	if (board[y][x].hasMine == false)
	    return;

	board[y][x].hasMine = false; 
	System.out.println("mine at first move struck at " + x + ", " + y); //debug

	Adjacent adj = new Adjacent(x, y);
	for (int i = adj.top; i <= adj.bottom; ++i) {
	    for (int j = adj.back; j <= adj.front; ++j)
	    { 
		board[i][j].decreaseRisk(); 
	    }
	} 

	int mineCount = 0;
	for (int i = adj.top; i <= adj.bottom; ++i) {
	    for (int j = adj.back; j <= adj.front; ++j)
	    { 
		if (board[i][j].hasUnder() == Content.MINE)
		    ++mineCount; 
	    }
	}
	board[y][x].risk = mineCount;
	seedMines(1);
	repaint();
    }
    public void revealBlankField(int x, int y){
	board[y][x].uncover(false); 

	Adjacent adj = new Adjacent(x, y);
	for (int i = adj.top; i <= adj.bottom; ++i) {
	    for (int j = adj.back; j <= adj.front; ++j)
	    { 
		Field field = board[i][j];

		if(field.isCovered == true && field.hasUnder() == Content.BLANK)
		    revealBlankField(j, i); 
		else field.uncover(false); //todo: remover bandeiras falsas
	    }
	} 
	this.repaint();
    }
    public void updateAdjacentFields(int x, int y){
	Adjacent adj = new Adjacent(x, y);
	//adj.dumpAdj();

	for (int i = adj.top; i <= adj.bottom; ++i) {
	    for (int j = adj.back; j <= adj.front; ++j)
	    { 
		if (board[i][j].hasMine == false){ 
		    board[i][j].increaseRisk(); 
		}
	    }
	}
    }
    public void checkWin(){
	if (winCondition()){
	    System.out.println("all safe fields cleared.");
	    System.exit(0);
	} 
    }
    private boolean winCondition(){
	boolean ret = true;
	int freeField = 0; //debug
	for (int i = 0; i < Settings.BOARD_SIZE_Y; ++i) {
	    for (int j = 0; j < Settings.BOARD_SIZE_X; ++j)
	    {
		if (board[i][j].canFieldReveal()){

		    ret = false;
		    freeField++;
		}
	    }
	}
	System.out.println("wincondition: " + freeField); //debug
	return ret;
    }
    private void setRevert(Field[][] board){
	this.revert = board.clone(); 
    }
    private void swapBoardHandles(){
	Field[][] tmp;

	tmp = board; 
	this.board = revert; 
	this.revert = tmp;
    }
    private void revertMove() { 

	gameGrid.removeAll(); 

	//gameGrid.add(new JButton("hello warudo"));

	for (int i = 0; i < Settings.BOARD_SIZE_Y; ++i) {
	    for (int j = 0; j < Settings.BOARD_SIZE_X; ++j)
	    {
		revert[i][j].addFieldListener(new ReceiveInput()); 
		gameGrid.add(revert[i][j]);
	    }
	} 
	swapBoardHandles();
	repaint(); 
    }
    public void hey(){
	System.out.println("a man has fallen into the river in Lego City"); //debug
	assert board != null : board;
    }
} 
class AutoSolve extends JPanel { 

    public void findOutliers(){ 
	for (int i = 0; i < Settings.BOARD_SIZE_Y; ++i) {
	    for (int j = 0; j < Settings.BOARD_SIZE_X; ++j) { 
		Field riskField = MineSweeperLogic.board[i][j]; 

		if ( riskField.getRisk() > 0 ){ //getrisk retorna -1 se o campo estiver coberto
		    if ( evaluateRisk(riskField) == 0 )
			flagAdjacents(riskField);
		} 
	    }
	}
    }
    private void flagAdjacents(Field riskField){
	Adjacent adj = new Adjacent(riskField);

	for (int i = adj.top; i <= adj.bottom; ++i) {
	    for (int j = adj.back; j <= adj.front; ++j)
	    { 
		Field adjField = MineSweeperLogic.board[i][j];

		if (adjField != riskField )
		    adjField.placeFlag(); 
	    }
	}
    }
    public void uncoverSafeFields(){
	for (int i = 0; i < Settings.BOARD_SIZE_Y; ++i) {
	    for (int j = 0; j < Settings.BOARD_SIZE_X; ++j) {
		Field riskField = MineSweeperLogic.board[i][j];

		if (countFlags(riskField) == riskField.getRisk()){
		    uncoverSafeAdjacents(riskField);
		    //System.out.println(riskField.returnX() + ", " + riskField.returnY() + " - " + evaluateRisk(riskField)); }
	    }
	}
    }
}
    private void uncoverSafeAdjacents(Field field){
	Adjacent adj = new Adjacent(field);

	for (int i = adj.top; i <= adj.bottom; ++i) {
	    for (int j = adj.back; j <= adj.front; ++j)
	    { 
		Field adjField = MineSweeperLogic.board[i][j];
		if (adjField != field)
		    adjField.uncover(false); //todo: revealField() e nao uncover()
	    }
	}
	repaint();
    }
    private int countFlags(Field field){
	Adjacent adj = new Adjacent(field);
	int ret = 0;

	for (int i = adj.top; i <= adj.bottom; ++i) {
	    for (int j = adj.back; j <= adj.front; ++j)
	    { 
		Field adjField = MineSweeperLogic.board[i][j];

		if(adjField.returnFlag() == true)
		    ++ret;
	    }
	}
	return ret;
    }
    private int evaluateRisk(Field field){ 
	int ret = 0;
	int flag = 0;
	Adjacent adj = new Adjacent(field); 

	for (int i = adj.top; i <= adj.bottom; ++i) {
	    for (int j = adj.back; j <= adj.front; ++j)
	    { 
		Field adjField = MineSweeperLogic.board[i][j];
		if ( adjField.toString() == Content.COVER)
		    ++ret; 
		else if (adjField.toString() == Content.FLAG || adjField.toString() == Content.MINE)
		    ++flag;
	    }
	}
	return ret - (field.getRisk() - flag);
    } 
} 
//391
