import java.io.*;
import java.net.*;
import java.util.*; 

//https://www.codejava.net/java-se/networking/how-to-create-a-chat-console-application-in-java-using-socket 

class User { 
    String ID = "0";
    Socket socket;
    BufferedReader input_stream;
    PrintWriter output_stream; 

    User(Socket socket, String ID){
	this.socket = socket;
	this.ID = ID;
	try { 
	    this.input_stream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    this.output_stream = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))); 
	} 
	catch (Exception e){ 
	    e.printStackTrace();
	} 
    }
} 

class clipsyncServer { 

    final int PORT = 2020;
    final String SERVERNAME = "server";
    final String SERVERCLIENT = "server-client";
    Set<User> users; 
    Stack<String> log; 


    //private void print_log(){
    //    for (String string : log){
    //        System.out.println(string);
    //    } 
    //}

    private void prompt(String string){
        String message = SERVERNAME + ": " + string;

        System.out.println(message);
        log.push(message); 
    }
    private void listen(){
	Scanner scan = new Scanner(System.in); 
	ServerSocket serversocket;

	try  { 
	    serversocket = new ServerSocket(PORT); 

	    while (true) {
		prompt("listening to new connections..."); 
		Socket socket = serversocket.accept(); 
		prompt("new user connectiong was made. adding to the room..."); 
		addUser(new User( socket, Integer.toString(users.size() + 1))); 
	    } 
	} 
	catch (Exception e){
	    scan.close();
	    e.printStackTrace(); 
	} 
    }

    //todo: passar isso pro User
    private String readUserLine(User user){
	try 
	{ 
	    return user.input_stream.readLine(); 
	}
	catch (Exception e){ e.printStackTrace(); } 
	return null; 
    }
    private void destructUser(User user){
	try
	{ 
	    user.input_stream.close();
	    user.socket.close(); 
	}
	catch (Exception e){ e.printStackTrace(); }

	return; 
    }


    private void addUser(User user)
    { 
	if (users.contains(user)){
	    prompt(user.ID + " is already present in room.");
	    return; 
	}

	Thread receive = new Thread(new Runnable (){ 
	    User user_handle = user;
	    String message; 
	    @Override
	    public void run(){
		while ((message = readUserLine(user_handle)) != null) 
		    broadcast(message, user_handle.ID); 

		prompt("user " + user_handle.ID + " disconnected."); 
		destructUser(user_handle);
		users.remove(user_handle); 
	    } 

	}); 


	users.add(user);
	receive.start(); 
	broadcast("user " + user.ID + " joined the room", SERVERNAME); 
    }
    public void broadcast(String message, String username){ 
	String msg = username + ": " + message;
	System.out.println(msg);

	for (User foo : users)
	{
	    foo.output_stream.println(msg); 
	    foo.output_stream.flush(); 
	} 
    } 
    clipsyncServer(){ 

	users = new HashSet<User>(); 
	log = new Stack<String>(); 

	prompt("initialiing."); 

	    Thread sender = new Thread(new Runnable (){

		Scanner scan;
		String msg; 
		@Override
		public void run(){
		    prompt("initializing prompt."); 

		    scan = new Scanner(System.in);

		    while (true){
			msg = scan.nextLine();
			broadcast(msg, SERVERCLIENT); 
		    } 
		} 
	    }); 

	    sender.start(); 

	    this.listen(); 
    } 

    public static void main(String[] args)
    { 
	new clipsyncServer(); 
    } 
}
