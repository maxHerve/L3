package istic.pr.socket.address;

import java.util.*;
import java.net.*;

public class AfficheInterfaces{
	public static void main(String[] args){

		try{
			Enumeration<NetworkInterface> listInterfaces = NetworkInterface.getNetworkInterfaces();

			int interfaceNumber = 1;
			while(listInterfaces.hasMoreElements()){
				NetworkInterface currentInterface = listInterfaces.nextElement();
				System.out.println("Interface n°" + interfaceNumber + " :");
				System.out.println("\tNom : " + currentInterface.getName());
				if(!currentInterface.getName().equals(currentInterface.getDisplayName())){
					System.out.println("\tNom humain : " + currentInterface.getDisplayName());
				}
				Enumeration<InetAddress> adresses = currentInterface.getInetAddresses();
				int interfaceAdresse = 1;
				while(adresses.hasMoreElements()){
					InetAddress currentAddr = adresses.nextElement();
					System.out.println("\tAdresse n°" + interfaceAdresse + " : " + currentAddr);
					interfaceAdresse++;
				}
				interfaceNumber++;
			}

		}
		catch(SocketException e){
			System.out.println("Error while fetching network interfaces : " + e);
		}
	}
}
