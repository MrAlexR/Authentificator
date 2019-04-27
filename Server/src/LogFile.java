import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogFile 
{
	public static synchronized void LogInformation(String information)
	{
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String Info = new String("[" + format.format(now) + "]: " + information);
		try {
			PrintWriter File = new PrintWriter(new FileWriter("ServerLog.txt", true));
			File.append(Info + "\n");
			File.close();
		} catch (Exception e) {
		}
	}
}
