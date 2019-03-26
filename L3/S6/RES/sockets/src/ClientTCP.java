package istic.pr.socket.tcp.echo;

import java.io.*;
import java.util.*;
import java.net.*;

public class ClientTCP {
	public static void main(String[] args) {
		//créer une socket client
		//créer reader et writer associés
		//Tant que le mot «fin» n’est pas lu sur le clavier,
		//Lire un message au clavier
		//envoyer le message au serveur
		//recevoir et afficher la réponse du serveur
		final String host = null;
		final int port = 9999;

		try{
			InetAddress address = InetAddress.getByName(host);

			try{
				Socket clientSocket = new Socket(address, port);

				BufferedReader reader = creerReader(clientSocket);
				PrintWriter writer = creerPrinter(clientSocket);

				boolean quit = false;

				System.out.println("Communication avec le serveur en cours :");

				while(!quit){
					String messageSent = lireMessageAuClavier();

					if(messageSent != null){
						envoyerMessage(writer, messageSent);
					}

					String messageReceived = recevoirMessage(reader);

					if(messageReceived != null){
						System.out.println(messageReceived);
					}

					if(messageSent != null && messageSent.equals("fin")){
						System.out.println("This is a very !");
						quit = true;
					}

				}

				try{
					clientSocket.close();
				}
				catch(IOException e){
					System.err.println("Error while closing socket : " + e);
				}
			}
			catch(IOException e){
				System.err.println("Error while creating socket : " + e);
			}
		}
		catch(UnknownHostException e){
			System.err.println("Error while fetching address : " + e);
		}

	}
	public static String lireMessageAuClavier() throws IOException {
		//lit un message au clavier en utilisant par exemple un BufferedReader
		//sur System.in
		Scanner scanner = new Scanner(System.in);
		String message = null;

		if(scanner.hasNextLine()){
			message = scanner.nextLine();
		}

		scanner.close();

		return message;
	}
	public static BufferedReader creerReader(Socket socketVersUnClient) throws IOException {
		//identique serveur
		return ServeurTCP.creerReader(socketVersUnClient);
	}
	public static PrintWriter creerPrinter(Socket socketVersUnClient) throws IOException {
		//identique serveur
		return ServeurTCP.creerPrinter(socketVersUnClient);
	}
	public static String recevoirMessage(BufferedReader reader) throws IOException {
		//identique serveur
		return ServeurTCP.recevoirMessage(reader);
	}
	public static void envoyerMessage(PrintWriter p, String message) throws IOException {
		//identique serveur
		ServeurTCP.envoyerMessage(p, message);
	}
}
