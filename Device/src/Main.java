import java.io.IOException;

public class Main 
{
	public static void main(String[] args) throws IOException, InterruptedException
	{	
		//start the client - device process
		Client ClientDevice = new Client();
		ClientDevice.RollWindow();
	}
}
