package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import net.microtrash.SearchResponse;

public class TestWebServer {

	/**
	 * WebServer constructor.
	 */
	protected void start() {
		ServerSocket s;

		System.out.println("Webserver starting up on port 8090");
		System.out.println("(press ctrl-c to exit)");
		try {
			// create the main server socket
			s = new ServerSocket(8090);
		} catch (Exception e) {
			System.out.println("Error: " + e);
			return;
		}

		System.out.println("Waiting for connection");
		for (;;) {
			try {
				// wait for a connection
				Socket remote = s.accept();
				// remote is now the connected socket
				System.out.println("Connection, sending data.");
				ObjectInputStream in = new ObjectInputStream(
						remote.getInputStream());
				ObjectOutputStream out = new ObjectOutputStream(
						remote.getOutputStream());

				Object o = in.readObject();
				System.out.println("reecived in server " + o);

				SearchResponse rs = new SearchResponse("");
				rs.setTitle("ttttt");
				rs.setAlbum("aaaa");
				rs.setArtist("arttt");
				
				out.writeObject(rs);
				out.flush();
				remote.close();
			} catch (Exception e) {
				System.out.println("Error: " + e);
			}
		}
	}

	/**
	 * Start the application.
	 * 
	 * @param args
	 *            Command line parameters are not used.
	 */
	public static void main(String args[]) {
		TestWebServer ws = new TestWebServer();
		ws.start();
	}
}