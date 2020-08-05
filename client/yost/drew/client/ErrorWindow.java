package yost.drew.client;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class ErrorWindow extends JFrame{
	public ErrorWindow() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(210,75);
		JTextArea error = new JTextArea(" Could not connect");
		this.add(error);
		error.setBounds(0,0,100,100);
		error.setEditable(false);
		error.setFont(new Font("Consolas", Font.BOLD, 19));
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		this.setVisible(true);
	}
}
