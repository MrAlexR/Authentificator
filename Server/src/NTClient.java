import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NTClient //Client structure
{
	public NTClient(Socket s, String ip, int port, String username) throws IOException
	{
		//Setup client data
		this.socket = s;
		this.Ip = ip;
		this.Port = port;
		this.Passcode = null;
		this.HasValidCode = false;
		this.TimeLeft = 0;
		this.Username = username;
		//Setup client data stream readers
		CIn = new ObjectInputStream(s.getInputStream());
		COut = new ObjectOutputStream(s.getOutputStream());
	}
	
	public Socket socket;
	public String Ip;
	public int Port;
	
	ObjectInputStream CIn;
	ObjectOutputStream COut;
	
	public String Username;
	public String Passcode;
	public Boolean HasValidCode;
	public int TimeLeft; 
}
