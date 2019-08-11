package fr.labbeh.SuperSpawn.files;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import fr.labbeh.SuperSpawn.SuperSpawn;

/**
 * Cette classe permet de sauver sur disque les info sur le point de spawn
 * @author labbeh
 * @version 2019-08-09, 1.0
 * */
public class FileUtility {
	/**
	 * Chemin d'accès au dossier du/des fichier(s) de configuration
	 * */
	private static final String CONFIG_FOLDER_URL = "./SuperSpawnDatas";
	
	/**
	 * Chemin d'accès au fichier de configuration
	 * */
	private static final String CONFIG_FILE_URL = CONFIG_FOLDER_URL + "/config.conf";
	
	/**
	 * Instance de la classe principale
	 * */
	private SuperSpawn ctrl;
	
	
	
	public FileUtility(SuperSpawn ctrl) {
		super();
		this.ctrl = ctrl;
	}

	/**
	 * Permet de lire les données au lancement du plugin
	 * Si le fichier de configuration est inexistant, il est créé
	 * Sinon, il est lu et chargé en mémoire
	 * */
	public void init() {
		File configFolder = new File(CONFIG_FOLDER_URL);
		File configFile = new File(CONFIG_FILE_URL);
		
		// création du dossier de configuration si il n'existe pas (premier lancement du plugin)
		if(!configFolder.exists())configFolder.mkdir();
		
		// si le fichier de configuration existe on procède à sa lecture
		if(configFile.exists()) {
			System.out.println("Chargement du point de spawn...");
			try {
				Scanner sc = new Scanner(configFile);
				sc.useDelimiter(";");
				
				String worldName = sc.next();
				double x = Double.parseDouble(sc.next());
				double y = Double.parseDouble(sc.next());
				double z = Double.parseDouble(sc.next());
				
				sc.close();
				
				ctrl.setSpawn(worldName, x, y, z);
			}
			catch (Exception e) {System.out.println("Erreur: fichier de configuration invalide ou inaccessible");}
		}
	} 
	
	/**
	 * Sauve sur disque les informations sur le point de spawn
	 * */
	public void saveOnFile() {
		try {
			FileWriter fw = new FileWriter(CONFIG_FILE_URL);
			PrintWriter pw = new PrintWriter(fw);
			
			pw.print(ctrl.getSpawn().toString());
			
			pw.close();
			fw.close();
		}
		catch (IOException e) {System.out.println("Erreur lors de l'écriture du fichier le point de spawn ne sera pas restauré "
												+ "au redemarrage du serveur");
		}
	}
}
