package P2P;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Pear {

	//Number of recent file
	//static int nfile = 0;	
	
	public static void main(String[] args) {
		try {
			
				InetAddress IP =  InetAddress.getLocalHost();
		
				Socket peerSocket = new Socket(IP, 3500);
				
				DataInputStream inn = new DataInputStream(peerSocket.getInputStream());
				DataOutputStream out = new DataOutputStream(peerSocket.getOutputStream());
				Scanner scanner = new Scanner(System.in);


				//User name
				System.out.println(inn.readUTF());
				out.writeUTF(scanner.nextLine());
				
				//Password
				System.out.println(inn.readUTF());
				out.writeUTF(scanner.nextLine());
				
				//Successful or not
				System.out.println(inn.readUTF());
				
				//Port
				int port = inn.read();
				
				
				Thread server = new ServerConnection(port);
				server.start();
				
				
				//Creating a file
				char number_of_file = '1';
				File peerfile = new File("F:\\JavaFolder\\Peer2Java1.txt");
				PrintWriter inpeerfile = new PrintWriter(peerfile);
				
				
				//Number of file
				int file_nums[] = new int[4];
				
				
				int nfile = 0;
				//Receiving file number
				file_nums[nfile] = inn.read();
				nfile++;
				
				
	
				for (int i = 0; i < 5; i++) {
					
				//Receiving a file from the tracker
				String fileinfo = inn.readUTF();

				//Storing the info of the tracker in a file
				inpeerfile.println(fileinfo);
				
				}
				inpeerfile.close();
				
				
				
				
				
				
				//Connecting to another peer
				Socket peer = new Socket(IP, 160);
				
				DataInputStream innpeer = new DataInputStream(peer.getInputStream());
				DataOutputStream outpeer = new DataOutputStream(peer.getOutputStream());
				
				//Hello from a peer
				System.out.println(innpeer.readUTF());
				
				//Receiving a file form another peer
				System.out.println(innpeer.readUTF());
					

			
		} catch (IOException e) {
			System.out.println("Problem with the Server Socket.");
		}
	}

	
	
	static class ServerConnection extends Thread {

		int port;

		public ServerConnection(int port) {
			this.port = port;
		}

		public void run() {
			try {

	                ServerSocket PeerSocket = new ServerSocket(port);
					System.out.println("The peer uploads on port " + port);
					
					while(true) {
						
						Socket clientSocket = PeerSocket.accept();
						System.out.println("A new client [" + clientSocket + "] is connected .");
						
						DataInputStream innpeer = new DataInputStream(clientSocket.getInputStream());
						DataOutputStream outpeer = new DataOutputStream(clientSocket.getOutputStream());
		
						outpeer.writeUTF("Hello Peer");
						
						//char number_of_file = '1';
						
						try{
							
							
							//Reading the peer file data
							File peerfile = new File("F:\\JavaFolder\\Peer2Java1.txt");
							FileInputStream inpeerfile = new FileInputStream(peerfile);
							Scanner scanpeerfile = new Scanner(inpeerfile);
							
							while(scanpeerfile.hasNext()) {
							
								//Sending this peer file to another peer 
								outpeer.writeUTF(scanpeerfile.nextLine());
								
							}
							
						}catch(IOException e) {
							System.out.println("Error in peer server");
						}
						
						
				
				}
				
				
			} catch (IOException e) {
				
			}
		}
	}
}

