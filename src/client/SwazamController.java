package client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import net.microtrash.SearchResponse;

public class SwazamController {

	String serverAddrss = "localhost";
	int port = 8090;
	SearchResponse response;
	ClientAgent clientAgent;

	public void sendRequestAndHandleResponse(Request r) {

		try {
			InetAddress addr;
			Socket sock = new Socket(serverAddrss, port);
			addr = sock.getInetAddress();
			System.out.println("Connected to " + addr);

			ObjectOutputStream output = new ObjectOutputStream(
					sock.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(sock.getInputStream());

			output.writeObject(r);
			System.out.println("Sent to server:" + r);

			Object ore = in.readObject();
			if (ore instanceof SearchResponse) {
				response = (SearchResponse) ore;
			}
			System.out.println(response);
			sock.close();
		} catch (java.io.IOException e) {

			System.out.println(e);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public SearchResponse getResponse() {
		return response;
	}
}