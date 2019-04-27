import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import NetworkingStuff.NTPacket;

public class Main 
{	
	public static Map<Socket, NTClient> Clients = new HashMap<Socket, NTClient>(); // List off all active clients
	public static ServerSocket Server = null;
	public static Interface Window;
	
	public static void main(String[] args) throws IOException, ClassNotFoundException
	{
		//server setup
		try {
			Server = new ServerSocket(1111);
			Thread Validation = new Thread(new CheckValidation());
			Validation.start();
			Thread WThread = new Thread() {public void run() {WindowThread();}};
			WThread.start();
		} catch (IOException e) {
			LogFile.LogInformation("Failed to start server!");
			return;
		}
		LogFile.LogInformation("-----------------------------Server started!--------------------------");
		
		while(Interface.ServerRunning) // up to 4 maximum connections
		{
			Socket Client;
			try
			{
				Client = Server.accept();
			}catch(Exception e) {
				break;
			}
			
			if(Client != null && Client.isConnected())
			{
				synchronized(Clients)
				{
					try
					{
						LogFile.LogInformation("Client connected! " + Client.getLocalPort() + " " + Client.getInetAddress().getHostAddress() + " Getting user info...");
						Clients.put(Client, new NTClient(Client, Client.getInetAddress().getHostAddress(), Client.getLocalPort(), "unknown"));
						//Create new thread for each connection
						Thread thread = new Thread(new Manager(Client));
						thread.start();
					}catch(Exception e)
					{
						if(Interface.ServerRunning)
							LogFile.LogInformation("Socket reset !" + e.toString());
						else break;
					}
				}
			}
			else
			{
				LogFile.LogInformation("Client failed to connect!");	
			}
		}
		LogFile.LogInformation("Main thread stopped!");
	}
	
	public static class Manager implements Runnable // Client's thread
	{
		public Socket MySocket;
		public Manager(Socket sock)
		{
			this.MySocket = sock;
		}
		public void run()
		{
			//Gets the packets and process them
			NTPacket Packet;
			while(Interface.ServerRunning)
			{
				try {
					Packet = PacketManager.GetPacket(Clients.get(MySocket));
					PacketManager.ProcessPacket(Packet, MySocket, Clients);
				}catch(Exception e)
				{
					if(MySocket.isClosed())
						LogFile.LogInformation("User socket was null!" + e.toString());
					break;
				}
			}
			//in case of failure (exception thrown) remove client
			LogFile.LogInformation("Lost connection with: " + Clients.get(MySocket).Username);
			try {
				Clients.get(MySocket).CIn.close();
				Clients.get(MySocket).COut.close();
				MySocket.close();
				if(!Clients.get(MySocket).HasValidCode)
				{
					Clients.remove(MySocket);
					LogFile.LogInformation("Tokens left: " + new Integer(Clients.size()).toString());
				}
				else
					LogFile.LogInformation("Tokens left: " + new Integer(Clients.size()).toString());
			} catch (IOException e) {
				LogFile.LogInformation("Failed to close user actions!" + e.toString());
			}
			LogFile.LogInformation("Thread: " + Thread.currentThread().getName() + " finished executing!");
		}
	}
	
	//Thread for checking the remaining time for valid passcode
	public static class CheckValidation implements Runnable
	{
		//time variables
		long LastTime = 0;
		long TimeNow = 0;
		long difference = 0;
		Set<Socket> ToRemove = new HashSet<Socket>();
		
		public void run()
		{
			while(Interface.ServerRunning)
			{
				difference += TimeNow - LastTime;
				LastTime = System.nanoTime(); //the time now
				
				if(TimeUnit.SECONDS.convert(difference, TimeUnit.NANOSECONDS) >= 1)
				{
					synchronized(Clients)
					{
						difference = 0;
						if(Clients.size() > 0) //if we have tokens active
						{
							for(Map.Entry<Socket, NTClient> Entry : Clients.entrySet())
							{
								if(Clients.get(Entry.getKey()).TimeLeft - 1 <= 0) //if the time left for token expired
								{
									if(Clients.get(Entry.getKey()).HasValidCode)//if client has valid code
									{
										Clients.get(Entry.getKey()).HasValidCode = false;
										LogFile.LogInformation("Code validation expired for: " + Clients.get(Entry.getKey()).Username);
									}
									if(Clients.get(Entry.getKey()).socket.isClosed())//remote token if client is disconnected
									{
										ToRemove.add(Entry.getKey());//add to removel list - so we don't iterate through null 
									}
								}
								else Clients.get(Entry.getKey()).TimeLeft--; //lower the time left for all active tokens
							}
						}
						if(ToRemove.size() > 0)
						{
							Clients.keySet().removeAll(ToRemove);//remove all garbage
							LogFile.LogInformation("Tokens removed, remaining: " + new Integer(Clients.size()).toString());
							ToRemove.clear();
						}
					}
				}
				TimeNow = System.nanoTime();// the time after checking
			}
			LogFile.LogInformation("Manager thread stopped!");
			LogFile.LogInformation("Server stopped!");
			try {
				Server.close();
				CloseAllConnections();
			} catch (Exception e) {
				LogFile.LogInformation("Failed to stop server! " + e.toString());
			}
		}
	}
	
	static void CloseAllConnections()
	{
		for(Map.Entry<Socket, NTClient> Entry : Clients.entrySet())
		{
			try {
				Entry.getValue().CIn.close();
				Entry.getValue().COut.close();
				Entry.getValue().socket.close();
			}catch(Exception e)
			{
				//------
			}
		}
	}
	
	static void WindowThread()
	{
		Window = new Interface();
		Window.RollWindow();
	}
}
