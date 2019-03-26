package istic.pr.socket.tcp.nom;

import java.io.*;
import java.util.*;
import java.net.*;

public class ServeurTCP extends Thread{
	public static void main(String[] args) {
		//Attente des connexions sur le port 9999
		//Traitement des exceptions
		//Dans une boucle, pour chaque socket clientes, appeler traiterSocketCliente

		ServeurTCP mainServerThread = new ServeurTCP();

		// Starting to handle client requests
		mainServerThread.start();

		// Block until user type 'q'
		quitServer();

		// Thread will stop after the request timeout is over
		mainServerThread.interrupt();
	}
	public static void traiterSocketCliente(Socket socketVersUnClient) {
		//Créer printer et reader
		//Tant qu’il y’a un message à lire via recevoirMessage
		//Envoyer message au client via envoyerMessage
		//Si plus de ligne à lire fermer socket cliente

		try{
			BufferedReader bufferReader = creerReader(socketVersUnClient);
			PrintWriter printer = creerPrinter(socketVersUnClient);

			String name = avoirNom(bufferReader);
			if(name == null || name.equals("default")){
				envoyerMessage(printer, "Vous utilisez le nom par défaut !");
			}

			String currentMessage = null;
			while((currentMessage = recevoirMessage(bufferReader)) != null){
				System.out.println("Message reçu !");
				envoyerMessage(printer, name + " > " +  currentMessage);
			}

			bufferReader.close();
			printer.close();
		}
		catch(SocketException e){
			System.err.println("A client suddenly leaved the server : " + e);
		}
		catch(IOException e){
			System.err.println("Error while reading or writing message : " + e);
		}

		try{
			socketVersUnClient.close();
		}
		catch(IOException e){
			System.err.println("Error while closing socket : " + e);
		}
	}
	public static BufferedReader creerReader(Socket socketVersUnClient) throws IOException {
		//créé un BufferedReader associé à la Socket
		return new BufferedReader(new InputStreamReader(socketVersUnClient.getInputStream()));
	}
	public static PrintWriter creerPrinter(Socket socketVersUnClient) throws IOException {
		//créé un PrintWriter associé à la Socket
		return new PrintWriter(socketVersUnClient.getOutputStream());
	}
	public static String recevoirMessage(BufferedReader reader) throws IOException {
		//Récupérer une ligne
		//Retourner la ligne lue ou null si aucune ligne à lire.
		String message = null;
		try{
			message = reader.readLine();
		}
		catch(IOException e){
			System.err.println("Error while reading message : " + e);
		}
		return message;
	}
	public static void envoyerMessage(PrintWriter printer, String message) throws IOException {
		//Envoyer le message vers le client
		printer.print(message);
		printer.print("\n");
		printer.flush();
	}

	public static void quitServer(){
		Scanner scanner = new Scanner(System.in);
		String prompt = "Exit the server by typing 'q'";
		System.out.println(prompt);
		boolean quit = false;

		while(!quit){
			if(scanner.hasNextLine()){
				String token = scanner.next();
				quit = token.equals("q");
				if(!quit){
					System.out.println(prompt);
				}
			}
		}
		scanner.close();
	}

	public void run(){
		final int port = 9999;
		// The timeout in milliseconds
		final int timeout = 1000;

		ServerSocket socket = null;

		try{
			socket = new ServerSocket(port);

			try{
				socket.setSoTimeout(timeout);
			}
			catch(SocketException e){
				System.err.println("Error while setting socket timeout : " + e);
			}


			try{
				boolean quit = false;

				while(!quit){
					try{
						Socket clientSocket = socket.accept();
						Runnable clientRequestHandler =
							new Runnable(){
								public void run(){
									traiterSocketCliente(clientSocket);
								}
							};

						Thread clientThread = new Thread(clientRequestHandler);
						clientThread.start();
					}
					catch(SocketTimeoutException e){
					}

					quit = Thread.currentThread().isInterrupted();
				}
			}
			catch(Exception e){
				System.err.println("Error while accepting connection : " + e);
			}

			try{
				socket.close();
			}
			catch(IOException e){
				System.err.println("Error while closing socket : " + e);
			}

		}
		catch(Exception e){
			System.err.println("Error while creating socket : " + e);
		}
	}

	public static String avoirNom(BufferedReader reader) throws IOException
	{
		//retourne le nom du client (en utilisant split de la classe String par exemple)
		String message = null;
		String name = "default";

		try{
			message = reader.readLine();

			if(message != null){
				if(message.split("NAME:").length == 2){
					name = message.split("NAME:")[1].trim();
				}
			}
		}
		catch(IOException e){
			System.err.println("Error while receiving name : " + e);
		}

		return name;
	}
}
