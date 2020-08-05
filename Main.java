package yost.drew.client;

import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

public class Main {

	public static void main(String[] args) throws UnknownHostException, IOException {
		try {
			new Window("",25564); //ip goes here
		} catch (ConnectException e) {
			new ErrorWindow();
		}
		
	}

}
