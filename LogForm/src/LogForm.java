import java.io.*;
import java.net.Socket;
import NetworkingStuff.NTPacket;
import NetworkingStuff.NTPacket.PacketContentType;

@SuppressWarnings("serial")
public class LogForm extends FormDesign
{
	private Socket Server;
	private ObjectInputStream Input;
	private ObjectOutputStream Output;
	
	@Override
	public void ClickedConnect()
	{
		//check is username and passcode were provided
		if(!StringCheck.IsValidString(User.getText()) || !StringCheck.IsValidString(Code.getText()))
		{
			Status.setText("Provide valid username/passcode!");
			return;
		}	
		//try to setup the server
		Connect.setEnabled(false);
		try
		{
			Server = new Socket("127.0.0.1", 1111);
			Output = new ObjectOutputStream(Server.getOutputStream());
			Input = new ObjectInputStream(Server.getInputStream());
		}
		catch(Exception e)
		{
			Status.setText("Failed to connect to server!");
			CloseConnection();
			Connect.setEnabled(true);
			return;
		}
		
		//send checking packet
		if(Server != null && Server.isConnected())
		{
			NTPacket payload = new NTPacket(User.getText(), PacketContentType.Request_UserPassword, 0, Code.getText().getBytes());
			try {
				Output.writeObject(payload);
				Output.flush();
			}catch(Exception e)
			{
				Status.setText("Failed to verify passcode!");
				CloseConnection();
				Connect.setEnabled(true);
				return;
			}
		}
		//retrieve result
		try {
			NTPacket Packet = (NTPacket) Input.readObject();
			if(new String(Packet.Payload).equals("succeded"))
				Status.setText("Code accepted! Welcome!");
			else
				Status.setText("Wrong passcode");
		}catch(Exception e)
		{
			Status.setText("Failed to verify passcode!");
			CloseConnection();
			Connect.setEnabled(true);
		}
		Connect.setEnabled(true);
		CloseConnection();
	}
	
	public void CloseConnection() 
	{
		try {
			Server.close();
			Input.close();
			Output.close();
		}catch(Exception e) {}
	}
}
