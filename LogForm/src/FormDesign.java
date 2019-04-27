import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class FormDesign extends JFrame
{
	//label for username and passcode
	public JLabel Username;
	public JLabel Passcode;
	public JLabel Info;
	public JLabel Status;
	//user input fields for username and passcode
	public JTextField User;
	public JTextField Code;
	//log button
	public JButton Connect;
	
	//window setup
	public FormDesign()
	{
		super("Log Form");
		setLayout(null);
		
		//add elements
		Username = new JLabel("Username");
		Username.setSize(120, 30);
		Username.setLocation(10, 10);
		add(Username);
		Passcode = new JLabel("Passcode");
		Passcode.setSize(120, 30);
		Passcode.setLocation(10, 40);
		add(Passcode);
		Info = new JLabel("Get passcode via your device. Code valid for 1 minute");
		Info.setSize(300, 30);
		Info.setLocation(10, 70);
		add(Info);
		Status = new JLabel("");
		Status.setSize(200, 30);
		Status.setLocation(10, 100);
		add(Status);
		User = new JTextField("");
		User.setSize(120, 20);
		User.setLocation(100, 15);
		add(User);
		Code = new JTextField("");
		Code.setSize(120, 20);
		Code.setLocation(100, 50);
		add(Code);
		Connect = new JButton("Validate");
		Connect.setSize(100, 50);
		Connect.setLocation(250, 15);
		Connect.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {ClickedConnect();}});
		add(Connect);
	}
	
	//button callback
	public void ClickedConnect() {}
	
	//show window
	public void RunWindow()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 200);
		setVisible(true);
		setResizable(false);
	}
}
