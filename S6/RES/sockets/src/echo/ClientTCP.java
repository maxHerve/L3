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
		String host = "localhost";
		int port = 9999;

		try{
			Socket socket = new Socket(host, port);

			BufferedReader reader = creerReader(socket);
			PrintWriter writer = creerPrinter(socket);

			String messageToSend = null;
		
			boolean quit = false;

			do{
				messageToSend = lireMessageAuClavier();

				if(messageToSend != null){
					envoyerMessage(writer, messageToSend);
				}

				String receivedMessage = recevoirMessage(reader);
				if(receivedMessage != null){
					System.out.println(receivedMessage);
				}

				quit = messageToSend != null && messageToSend.equals("fin");
			}while(!quit);

			reader.close();
			writer.close();

		}
		catch(UnknownHostException e){
			System.err.println("Erreur lors de la création du socket : " + e);
		}
		catch(IOException e){
			System.err.println("IOException : " + e);
			e.printStackTrace();
		}

	}
	public static String lireMessageAuClavier() throws IOException {
		//lit un message au clavier en utilisant par exemple un BufferedReader
		//sur System.in
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		String message = reader.readLine();

		return message;
	}
	public static BufferedReader creerReader(Socket socketVersUnClient) throws IOException {
		//identique serveur
		return new BufferedReader(new InputStreamReader(socketVersUnClient.getInputStream()));
	}
	public static PrintWriter creerPrinter(Socket socketVersUnClient) throws IOException {
		//identique serveur
		return new PrintWriter(socketVersUnClient.getOutputStream());
	}
	public static String recevoirMessage(BufferedReader reader) throws IOException {
		//identique serveur
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
	public static void envoyerMessage(PrintWriter p, String message) throws IOException {
		//identique serveur
		p.print(message);
		p.print("\n");
		p.flush();
	}
}
