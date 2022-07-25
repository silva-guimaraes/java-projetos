//import java.awt.datatransfer.*;
import java.awt.*;
import javax.swing.*;



public class test extends JFrame {

    test(){
	setLayout(new FlowLayout());
	add(new JButton("hue hue")); 
    }

    public static void main(String args[])throws Exception
    {
	SwingUtilities.invokeLater(new Runnable(){
	    public void run(){
		JFrame frame = new test();
		frame.setVisible(true);
		frame.setSize(500,500);
		frame.setTitle("teste");
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE); 
	    }
	}); 

	//Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();

	//// Get data stored in the clipboard that is in the form of a string (text)
	//System.out.println(c.getData(DataFlavor.stringFlavor));

    }
}
