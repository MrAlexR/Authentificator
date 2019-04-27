import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import NetworkingStuff.NTPacket;
import NetworkingStuff.NTPacket.PacketContentType;

@SuppressWarnings("serial")
public class Client extends WindowDefinition
{
	//Client variables
	private Socket Server;
	private ObjectOutputStream Output;
	private ObjectInputStream Input;

	//Callback from window features
	@Override
	public void ClickConnect() throws IOException{
		if(!AbleToGo) 
		{
			Info.setText("Unable to connect to server!");
			return;
		}
		try {
			Server = new Socket("127.0.0.1", 1111);
			Output = new ObjectOutputStream(Server.getOutputStream());
			Input = new ObjectInputStream(Server.getInputStream());
		} catch (Exception e){
			Info.setText("Unable to connect to server!");
			CloseConnection();
			return;
		}
		
		if(Server != null && Server.isConnected())
		{
			Info.setText("Connected to server!");
			NTPacket payload = new NTPacket(UserName, PacketContentType.InitialPacket, 6, new String("hello!").getBytes());
			Output.writeObject(payload);
			Output.flush();
		}
		
		Connect.setEnabled(false);
		RequestCode.setEnabled(true);
		Thread Listener = new Thread() {public void run() {try {
			Listen();
		} catch (IOException e) {
			//---
		}}};
		Listener.start();
	};
	
	//Callback from request button
	@Override
	public void ClickRequestCode() throws IOException
	{
		NTPacket request = new NTPacket(UserName, PacketContentType.Request_NewPassword, 0, null);
		try {
			Output.writeObject(request);
		}
		catch(Exception e)
		{
			CloseConnection();
		}
		RequestCode.setEnabled(false);
	}
	
	//For closing the connection properly
	public void CloseConnection() throws IOException
	{
		Output.close();
		Input.close();
		Server.close();
	}
	
	//Listen for packets
	private void Listen() throws IOException
	{
		NTPacket incoming;
		while(true)
		{
			try {
				incoming = (NTPacket) Input.readObject();
				if(incoming.PacketType == PacketContentType.Request_NewPassword)
					Code.setText("Your passcode: " + new String(incoming.Payload));
				if(incoming.PacketType == PacketContentType.Request_NewPasswordDenied)
					Code.setText("Code already requested!");
			} catch (Exception e) {
				Info.setText("Disconnected from server!");
				break;
			} 
		}
		CloseConnection();
	}
}
