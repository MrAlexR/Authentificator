import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
	public class Interface extends JFrame
	{
		private JButton Stop;
		public static Boolean ServerRunning = true;
		
		public Interface()
		{
			super("Server control");
			setLayout(new FlowLayout());
			Stop = new JButton("Stop server!");
			add(Stop);
			Stop.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {StopServer();}});
		}
		public void RollWindow()
		{
			JOptionPane.showMessageDialog(null, "Server started!");
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setSize(260, 100);
			setResizable(false);
			setVisible(true);
		}
		
		public void StopServer()
		{
			ServerRunning = false;
		}
	}
	