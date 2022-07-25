import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.lang.Integer; 

class UIinit extends JFrame { 
    
    mainBody mainbody;

    protected UIinit(){

	mainbody = new mainBody();
	add(mainbody); 
	setTitle("Knight's Tour");
	setMinimumSize(new Dimension(500,500));
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	setVisible(true); 
    }
    public static void init(){
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
        	UIinit mainUI = new UIinit();
            }
        }); 
    }
    public static void main(String[] args){
	init(); 
    }
} 
class mainBody extends Board {

    UItable uitable;

    //todo: ui 
     mainBody(){ 
	uitable = new UItable(returnGrid(), returnGridLength());
	add(uitable);


	JButton jenson = new JButton("hello world");
	jenson.addActionListener(new ActionListener(){
	    public void actionPerformed(ActionEvent e){
		resetGrid();
		highlightSpace(); 
		repaint();
	    }
	});
	add(jenson);
	//todo: alinhar isso para baixo
	add(new JLabel("hello world"), BOTTOM_ALIGNMENT); 
    } 
    class UItable extends JPanel {

	UItable(Space[][] grid, int gridsize){
	    setSize(1000,1000);
	    setLayout(new GridLayout(gridsize, gridsize));
	    initGrid(grid);
	} 
	private void initGrid(Space[][] grid){
	    for (int i = 0; i < grid.length; ++i){
		for (int j = 0; j < grid[0].length; ++j){
		    add(grid[i][j]);
		}
	    }
	}
    } 
    public static void main(String[] agrs){

	UIinit UI = new UIinit();

    }
} 
class Casa extends JComponent implements MouseListener {

    protected Color fill;
    protected int number;
    protected boolean taken;

    protected Casa(){
	this.fill = null;

	addMouseListener(this);
	setVisible(true);
	setPreferredSize(new Dimension(size + 1,size + 1)); 
    }


    protected final int size = 50; 
    protected Rectangle rect = new Rectangle(0,0, size, size); 
    public void paintComponent(Graphics g){
	super.paintComponent(g);

	Graphics2D g2d = (Graphics2D) g; 
	//todo: consertar esse stroke
	g2d.setStroke(new BasicStroke(3));



	emptySpace(g2d, rect);
    } 
    protected void emptySpace(Graphics2D g2d, Rectangle rect){ 
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
	    System.out.println(number + " entered");
	    this.fill = cinza;
	    repaint(); 
	}
    }
    public void mouseExited(MouseEvent e){
	if (isSpaceAvailable()){
	    System.out.println(number + " exited");
	    this.fill = null;
	    repaint(); 
	}
    }
    public void mousePressed(MouseEvent e){
	if (isSpaceAvailable()){
	    System.out.println(number + " pressed");
	    fill = Color.lightGray;
	    repaint(); 
	}
    }
    public void mouseReleased(MouseEvent e){
	if (isSpaceAvailable()){
	    System.out.println(number + " released"); 
	    if(this.contains(e.getPoint()))
		this.fill = cinza;
	    else fill = null;
	    repaint();

	}
    } 
    public void mouseClicked(MouseEvent e){}

    public boolean isSpaceAvailable(){
	if (taken == false)
	    return true;
	else return false;
    }
}
