import java.util.Random;
import java.util.Date;
import javax.swing.*;
import java.awt.*; 
import java.awt.event.*; 
import javax.swing.event.*; 
import java.util.concurrent.TimeUnit; 
import java.util.LinkedList;


public class Field extends RootField implements Content { 

    protected int x;
    protected int y;
    protected int risk;
    protected boolean hasMine;
    protected boolean isCovered;
    protected boolean hasFlag;

    Field(int x, int y) {
	risk = 0;
	isCovered = true;
	this.x = x; this.y = y; 
    }
    public int getRisk(){
	if (isCovered == false)
	    return risk;
	return -1;
    }
    public void plantMine(){
	hasMine = true;
    }
    public void increaseRisk(){ 
	++risk;
    } 
    public void decreaseRisk(){ 
	if (risk > 0)
	    --risk;
    } 
    public boolean returnCover(){
	return isCovered;
    }
    public boolean canFieldReveal(){
	if (hasMine == false && isCovered == true)
	    return true;
	else return false;
    }
    public int returnX(){
        return x;
    }
    public int returnY(){
        return y;
    }
    public void placeFlag(){
	if (isCovered){ 
	    hasFlag = true;
	    repaint();
	}
    } 
    public boolean returnFlag(){
	return hasFlag;
    }
    public String hasUnder(){
	if (hasMine)
	    return Content.MINE;
	else if (risk > 0)
	    return Integer.toString(risk);
	else return Content.BLANK;
    }
    public String toString(){
	if (hasFlag)
	    return Content.FLAG;
	else if (isCovered)
	    return Content.COVER;
	else return hasUnder();
    }
    public void uncover(boolean repaint){  ////////////////////////////////////////
	if(hasFlag == false){
	    if (isCovered == true){
		isCovered = false; 

		if (hasMine == false)
		    --MineSweeperLogic.fieldsLeft;
		System.out.println( "safe fields left: " + MineSweeperLogic.fieldsLeft ); //debug

		if(repaint)
		    repaint(); 
	    }
	}
    }
    private FieldListener superFieldListener;
    private EventListenerList ell = new EventListenerList();

    public void addFieldListener(FieldListener fieldlistener) { 
	this.superFieldListener = fieldlistener; 
    } 
    public void removeFieldListener(){
	this.superFieldListener = null;
    }
    private void fireFieldListener() {
	superFieldListener.actionPerformed(new FieldEvent(this)); 
    } 
    @Override
    public void mouseReleased(MouseEvent e){
	if (rect.contains(e.getPoint())){

	fireFieldListener();
	this.fill = null;
	repaint(); 
	}
    } 
    @Override
    public void mousePressed(MouseEvent e){ 
	if (isSpaceAvailable()){
	    this.fill = Color.gray;
	    repaint(); 
	}
    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g); 
        Graphics2D g2d = (Graphics2D) g; 
        //todo: consertar esse stroke
        g2d.setStroke(new BasicStroke(3));

	switch(toString()){

	case Content.FLAG:
	    g2d.setPaint(Color.blue);
	    g2d.fill(rect);
	    g2d.draw(rect);
	    break; 
	case Content.COVER: 
	    if (Settings.SHOW_COVER == true){
		g2d.setPaint(Color.black);
		g2d.fill(rect);
		g2d.draw(rect);
		break; 
	    } else {
		g2d.drawString(hasUnder(), 10,20); 
		emptySpace(g2d, rect); 
		break;
	    }
	case Content.MINE:
	    g2d.setPaint(Color.red);	 	g2d.fill(rect);
	    g2d.setPaint(Color.black);		g2d.drawString(toString(), 10,20); 
	    break;
	default:
	    g2d.drawString(toString(), 10,20); 
	    emptySpace(g2d, rect); 
	    break;
	}
    } 
    @Override
    public void mouseEntered(MouseEvent e){ } 
    @Override
    public boolean isSpaceAvailable(){
	if (isCovered == false)
	    return true;
	else return false;
    }

} 
class Casa extends JComponent implements MouseListener { 

    protected Color fill;
    protected int orderNumber;

    protected Casa() {
	this.fill = null;

	addMouseListener(this);
	setVisible(true);
	setPreferredSize(new Dimension(SIZE + 1,SIZE + 1)); 
    } 
    protected final int SIZE = Settings.FIELD_SIZE; 
    protected Rectangle rect = new Rectangle(0,0, SIZE, SIZE); 
    public void paintComponent(Graphics g){
	super.paintComponent(g);

	Graphics2D g2d = (Graphics2D) g; 
	//todo: consertar esse stroke
	g2d.setStroke(new BasicStroke(3));

	//g2d.drawString(Integer.toString(this.orderNumber), 20,20);


	emptySpace(g2d, rect);
    } 
    protected void emptySpace(Graphics2D g2d, Rectangle rect) { 
	if (fill != null){ 
	    g2d.setPaint(fill);
	    g2d.fill(rect);
	}
	g2d.draw(rect); 
    } 
    protected static final Color cinza = new Color(227,227,227);
    protected static final Color verde = new Color(112, 255, 99);
    public void mouseEntered(MouseEvent e){
	if (isSpaceAvailable()){
	    //System.out.println(orderNumber + " entered");//debug
	    this.fill = cinza;
	    repaint(); 
	}
    }
    public void mouseExited(MouseEvent e){
	if (isSpaceAvailable()){
	    //System.out.println(orderNumber + " exited");//debug
	    this.fill = null;
	    repaint(); 
	}
    }
    public void mousePressed(MouseEvent e){
	if (isSpaceAvailable()){
	    //System.out.println(orderNumber + " pressed");//debug
	    fill = Color.lightGray;
	    repaint(); 
	}
    }
    public void mouseReleased(MouseEvent e){
	if (isSpaceAvailable()){
	    //System.out.println(orderNumber + " released"); //debug
	    if(this.contains(e.getPoint()))
		this.fill = cinza;
	    else fill = null;
	    repaint(); 
	}
    } 
    public void mouseClicked(MouseEvent e){}

    public boolean isSpaceAvailable(){
	if (true)
	    return true;
	else return false;
    }
} 
class RootField extends FieldProbability implements Content {

    //private LinkedList<ProbabilityGroup> probGroup; 

    //RootField(){ 
    //    super(null, null); 
    //} 

    //public LinkedList getProbGroup(){
    //    return probGroup;
    //} 
    //public void scanAdjacent(){
    //    Adjacent adj = new Adjacent(this.x, this.y);

    //    if (probGroup == null)
    //        probGroup = new ProbabilityGroup(groupID);

    //    for (int i = adj.top; i <= adj.bottom; ++i) {
    //        for (int j = adj.back; j <= adj.front; ++j)
    //        { 
    //    	Field field = MineSweeperLogic.board[i][j];
    //    	if (field.toString() == Content.COVER)
    //    	    probGroup.addField(field);
    //        }
    //    }
    //} 

    //public void subtractProbGroup(FieldProbability fieldprob){
    //    if (fieldprob.getProbGroup().checkIfElementsWithin(new Adjacent(this))){ 
    //        if ()

    //    }

    //}
}
class ProbabilityGroup implements Content {


    //private LinkedList<FieldProbability> fieldList;
    //private float probability;

    //ProbabilityGroup()
    //{ 
    //    fieldList = new LinkedList<FieldProbability>(); 
    //} 

    //public boolean checkIfElementsWithin(Adjacent adj){
    //    if (fieldList.size() == 0){ 
    //        System.out.println("linked list empty" + this.toString());
    //        return false;
    //    }
    //    for (FieldProbability currentField : fieldList){
    //        if (currentField.checkIfWithin(adj) == false)
    //    	return false;
    //    }
    //    return true;
    //}

    //public void calculateProbability()
    //{
    //    int probabilitySum = 0; 
    //}

    //public void addField(Field field)
    //{
    //    fieldList.get(fieldList.size()).setGroup(this);
    //} 
    //public float getProbability(){
    //    return probability;
    //}
    //public LinkedList getFieldList(){
    //    return fieldList;
    //}

} 
class FieldProbability extends Casa implements Content {

    //private LinkedList<ProbabilityGroup> probGroupList;
    //private Field wrappedField;

    ////FieldProbability(Field wrappedField, ProbabilityGroup probGroup){
    ////    setWrappedField(wrappedField);
    ////    setGroup(probGroup);
    ////}

    //public void setWrappedField(Field wrappedField)
    //{
    //    this.wrappedField = wrappedField; 
    //}

    //public void setGroup(ProbabilityGroup probGroup)
    //{
    //    probGroupList.add(probGroup);
    //} 

    //public int getX()
    //{
    //    return wrappedField.x;
    //}
    //public int getY()
    //{
    //    return wrappedField.y;
    //}

    //public boolean checkIfWithin(Adjacent adj)
    //{
    //    return 	(getX() >= adj.back) ? true : false && (getX() <= adj.front) ? true : false 
    //        	&& (getY() >= adj.top) ? true : false && (getY() <= adj.bottom) ? true : false;
    //}

    //public float getProbability(int i)
    //{ 
    //    return probGroupList.get(i).getProbability(); 
    //} 

    //public void helloWorld()
    //{
    //    System.out.println("hello world");
    //} 
}
