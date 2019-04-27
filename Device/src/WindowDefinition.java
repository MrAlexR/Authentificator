import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;

//class for client interface window
@SuppressWarnings("serial")
public class WindowDefinition extends JFrame
{
	//connect button 
	public JButton Connect;
	public void ClickConnect() throws IOException, InterruptedException {};
	//request code button
	public JButton RequestCode;
	public void ClickRequestCode() throws IOException, InterruptedException {};
	//field for information
	public JLabel Info;
	//field for code
	public JLabel Code;
	//CLient username
	public String UserName;
	Boolean AbleToGo;
	
	public WindowDefinition() 
	{
		//partial window setup
		super("Client-Device");
		try {
			UserName = UserData.GetUserName();
			setTitle(UserName);
			AbleToGo = true;
		}catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "Failed to obtain user data: " + e.toString());
			AbleToGo = false;
		}
		setLayout(null);
		
		//connect button setup
		Connect = new JButton("Connect to server");
		Connect.setSize(140, 30);
		Connect.setLocation(8, 10);
		add(Connect);
		Connect.addActionListener(new ActionListener(){	public void actionPerformed(ActionEvent e) { try {ClickConnect();} catch (Exception e1) {}}});
		//request code setup
		RequestCode = new JButton("Request code");
		RequestCode.setSize(140, 30);
		RequestCode.setLocation(150, 10);
		add(RequestCode);
		RequestCode.addActionListener(new ActionListener(){	public void actionPerformed(ActionEvent e) { try {ClickRequestCode();} catch (Exception e1) {}}});
		RequestCode.setEnabled(false);
		//info field setup
		Info = new JLabel("Disconnected from server");
		Info.setSize(160, 30);
		Info.setLocation(50, 50);
		add(Info);
		//code field setup
		Code = new JLabel("");
		Code.setSize(190, 30);
		Code.setLocation(50, 80);
		add(Code);
	}
	
	public void RollWindow()
	{
		//window setup
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(300, 200);
		this.setLocation(300, 300);
		this.setResizable(false);
		this.setVisible(true);
	}
}
