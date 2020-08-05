package yost.drew.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClient {
	
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	
	public SocketClient(String address, int port) throws UnknownHostException, IOException {
		socket = new Socket(address, port);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);
	}
	
	public void sendMessage(String msg) throws IOException {
		out.println(msg);
	}
	
	public BufferedReader getPrinter() {
		return in;
	}
	
	public String getIP() {
		return socket.getInetAddress().getHostAddress();
	}
}
