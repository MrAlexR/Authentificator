import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import NetworkingStuff.NTPacket;
import NetworkingStuff.NTPacket.PacketContentType;

public class PacketManager {
	public static void ProcessPacket(NTPacket Packet, Socket MySocket, Map<Socket, NTClient> Clients) throws IOException
	{
		if(Packet.PacketType == PacketContentType.InitialPacket)
		{
			//process packet for new client
			LogFile.LogInformation("Connection added : " + Packet.PacketSender);
			Clients.get(MySocket).Username = Packet.PacketSender;
		}
		if(Packet.PacketType == PacketContentType.Request_NewPassword)
		{
			//if client already has a token registered
			int count = 0;
			for(Entry<Socket, NTClient> Entry : Clients.entrySet())
			{
				if(Entry.getValue().Username.equals(Clients.get(MySocket).Username) && Entry.getValue().HasValidCode)
					count++;
			}
			if(count > 0)
			{
				NTPacket ClientPacket = new NTPacket("SEVER",PacketContentType.Request_NewPasswordDenied, 0, null);
				SendPacket(Clients.get(MySocket), ClientPacket);
				return;
			}
				
			//generate password for client
			LogFile.LogInformation("Password request from: " + Clients.get(MySocket).Username + ", generating...");
			Clients.get(MySocket).HasValidCode = true;
			Clients.get(MySocket).Passcode = new String(NextValue() + "-" + NextValue() + "-" + NextValue() + "-" + NextValue());
			Clients.get(MySocket).HasValidCode = true;
			Clients.get(MySocket).TimeLeft = 30;
			LogFile.LogInformation("Password generated for: " + Clients.get(MySocket).Username + " : " + Clients.get(MySocket).Passcode);
			//send packet to client containing the code
			NTPacket ClientCode = new NTPacket("SERVER", PacketContentType.Request_NewPassword, 11, Clients.get(MySocket).Passcode.getBytes());
			SendPacket(Clients.get(MySocket), ClientCode);
		}
		if(Packet.PacketType == PacketContentType.Request_UserPassword)
		{
			LogFile.LogInformation("Connection added : " + Packet.PacketSender);
			Clients.get(MySocket).Username = Packet.PacketSender;
			//web page requested server to check if given passcode is valid
			LogFile.LogInformation("Webpage requested password check for:" + Clients.get(MySocket).Username);
			String username = new String(Packet.PacketSender);
			String passcode = new String(Packet.Payload);
			for(Entry<Socket, NTClient> Entry : Clients.entrySet())
			{
				if(Entry.getValue().Username.equals(username) && Entry.getValue().HasValidCode)//all users registered with the given name and has valid passcode 
				{
					if(Entry.getValue().Passcode.contentEquals(passcode))
					{
						// user entered the right passcode
						NTPacket Result = new NTPacket("SERVER", PacketContentType.Request_Existing, 8, new String("succeded").getBytes());
						SendPacket(Clients.get(MySocket), Result);
						LogFile.LogInformation("User: " + Entry.getValue().Username + " has used his/her code");
						Entry.getValue().TimeLeft = 1;
						return;
					}
					else
					{
						// user entered the wrong passcode
						NTPacket Result = new NTPacket("SERVER", PacketContentType.Request_Existing, 8, new String("failed").getBytes());
						SendPacket(Clients.get(MySocket), Result);
						return;
					}
				}
			}
			//didn't find any registered users with the specified name
			NTPacket Result = new NTPacket("SERVER", PacketContentType.Request_Existing, 8, new String("failed").getBytes());
			SendPacket(Clients.get(MySocket), Result);
		}
	}
	
	//sending packets
	public static void SendPacket(NTClient MySocket, NTPacket Packet) throws IOException
	{
		MySocket.COut.writeObject(Packet);
		MySocket.COut.flush();
	}
	
	//retrieve packets
	public static NTPacket GetPacket(NTClient MySocket) throws IOException, ClassNotFoundException
	{
		NTPacket Packet;
		Packet = (NTPacket)MySocket.CIn.readObject();
		return Packet;
	}
	
	//generate random value
	private static String NextValue()
	{
		return Integer.toString(new Random().nextInt(90) + 10);
	}
}
