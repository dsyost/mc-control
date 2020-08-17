package yost.drew.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class Panel extends JPanel {
    private JTextArea incoming;
    private JTextField output;
	private Thread textThread;
	private JScrollPane scrollbar;
    
    private class UpdateText implements Runnable{
		JTextArea window;
		BufferedReader reader;
		public UpdateText(JTextArea w, BufferedReader r) {
			window = w;
			reader = r;
		}
		@Override
		public void run() {
			while(true) {
				String text = window.getText();
				try {
					text += reader.readLine() + "\n";
				} catch (IOException e) {
					e.printStackTrace();
				}
				String[] s = text.split("\n");
				window.setText(text);
				window.setCaretPosition(window.getDocument().getLength());
			}
		}
	}

    public Panel(SocketClient socket) {
    	
        incoming = new JTextArea ("You are connected to " + socket.getIP()+"\n",5, 5);
        output = new JTextField (5);
        scrollbar = new JScrollPane(incoming);
        scrollbar.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        
        setPreferredSize (new Dimension (1000, 600));
        setLayout (null);

        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setBackground(Color.black);
        incoming.setForeground(Color.white);
        incoming.setFont(new Font("Consolas", Font.BOLD, 12));
        
        add (output);
        add (scrollbar);
        
        output.setBounds (0, 550, 1000, 50);
        scrollbar.setBounds(0,0,1000,550);
        
        incoming.setEditable(false);
        output.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					socket.sendMessage(e.getActionCommand());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				output.setText("");
			}
		});
        textThread = new Thread(new UpdateText(incoming, socket.getPrinter()));
		textThread.start();
    }
}
