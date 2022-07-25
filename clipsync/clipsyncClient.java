import java.net.*; 
import java.io.*;
import java.util.Scanner; 
import java.util.concurrent.TimeUnit; 
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;



class clipsyncClient extends JFrame {

    final String HOSTNAME = "localhost";
    final int PORT = 2020;

    private void execute(){ 
	Scanner scan; 

	try  { 
	    Socket socket = new Socket(HOSTNAME, PORT); 

	    System.out.println("[DEBUG] connection stablished. aditional set up is still needed. working on it...");

	    BufferedReader outstream = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
	    PrintStream printwriter = new PrintStream(socket.getOutputStream()); 
	    scan = new Scanner(System.in); 

	    Thread prompt = new Thread(new Runnable (){
		String msg; 
		public void run(){
		    try { 
			System.out.println("[DEBUG] prompt thread initializing...");
			while (socket.isConnected()){
			    msg = scan.nextLine(); 

			    if (socket.isClosed())
				break; 

			    printwriter.println(msg);
			    printwriter.flush(); 
			} 
		    } 
		    catch (NullPointerException e){ } catch (Exception e){ } 
		}
	    }); 
	    prompt.start(); 

	    String msg; 
	    try { 
		while (socket.isConnected() && (msg = outstream.readLine()) != null) 
		    System.out.println(msg);

		System.out.println("[DEBUG] server disconnected"); 

		outstream.close();
		if (socket.isConnected())
		    socket.close(); 
		scan.close(); 
	    }
	    catch (Exception e){
		e.printStackTrace(); 
	    } 
	}
	catch (ConnectException e) {
	}
	catch (Exception e){
	    e.printStackTrace(new PrintStream(System.out)); 
	} 


	return;
    } 

    private void timeout() {
	int i = 5;

	while (true) { 
	    try {
		System.out.println("[DEBUG] attempting to stablish a connection with " + HOSTNAME + " at " + PORT + "..." );
		execute();
		System.out.println("[DEBUG] connection to host was not possible. retrying in " + i + " seconds.");
		TimeUnit.SECONDS.sleep(i);
		i += i; 
	    } 
	    catch (Exception e) {} 
	} 
    } 

    // ====================== constructor ======================== //
    clipsyncClient()
    { 
	try {
	    UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel"); 
	} 
	catch (Exception e){ } 

	SwingUtilities.invokeLater(new Runnable(){
	    public void run(){ 
		setSize(500,500);
		setTitle("clip-client");
		setDefaultCloseOperation(EXIT_ON_CLOSE); 
		setVisible(true); 

		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEADING)); 

		add(panel); 
		JButton connect = new JButton("connect");
		JButton exit =  new JButton("exit");


		connect.addActionListener(new ActionListener(){ 
		    public void actionPerformed(ActionEvent e)
		    { 
			new SwingWorker<Integer, Integer>()
			{ 
			    public Integer doInBackground(){ 
				if (connect.isEnabled())
				{ 
				    connect.setText("connecting..."); 
				    connect.setEnabled(false); 
				    timeout(); 
				    connect.setEnabled(true);
				    connect.setText("connect"); 
				} 
				return 0; 
			    } 
			    public void done(){
				return;
			    } 
			}.execute(); 
		    } 
		}); 

		exit.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e){
			System.out.println("exit");
			System.exit(0);
		    }
		}); 

		JButton hello = new JButton("hello world");

		hello.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e){
			System.out.println("hello world");
		    }
		}); 

		panel.add(connect); 
		panel.add(hello);
		panel.add(exit); 

	    } 
	}); 

    }



    public static void main(String[] args)
    { 
	new clipsyncClient(); 
    }
}
