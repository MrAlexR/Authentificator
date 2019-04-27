package NetworkingStuff;

import java.io.Serializable;

@SuppressWarnings("serial")
public class NTPacket implements Serializable
{
	public NTPacket(String oSender, PacketContentType content, int size, byte[] data)
	{
		this.PacketSender = oSender;
		this.PacketType = content;
		this.PacketSize = size;
		this.Payload = data;
	}
	
	public String PacketSender;
	public PacketContentType PacketType;
	public int PacketSize;
	public byte[] Payload;
	
	public enum PacketContentType
	{
		Request_NewPassword,
		Request_ExpirationTime,
		Request_Existing,
		InitialPacket,
		Request_NewPasswordDenied,
		unknown,
		Request_UserPassword
	}
}
