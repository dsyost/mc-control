package yost.drew.client;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JFrame;

public class Window extends JFrame{
	public Window(String addr, int port) throws UnknownHostException, IOException {
		SocketClient socket = new SocketClient(addr, port);
		this.setDefaultCloseOperation (JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					socket.sendMessage("close");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				System.exit(0);
			}
		});
        this.getContentPane().add(new Panel(socket));
        this.pack();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        this.setVisible (true);
	}
}
