package fr.labbeh.SuperSpawn;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.world.World;

import com.google.inject.Inject;

import fr.labbeh.SuperSpawn.commands.CommandHome;
import fr.labbeh.SuperSpawn.commands.CommandSet;
import fr.labbeh.SuperSpawn.commands.CommandSethome;
import fr.labbeh.SuperSpawn.commands.CommandSpawn;
import fr.labbeh.SuperSpawn.files.FileUtility;

/**
 * Classe principale du plugin permettant de créer un point de spawn.
 * @author labbeh
 * @version 1.0.0
 * */
@Plugin(id = "superspawn", name = "SuperSpawn", authors = "labbeh")
public class SuperSpawn {
	/**
	 * constante pour afficher le nom du plugins lorsqu'il envoie un message dans la console
	 * ou dans le chat
	 * */
	public static final String PLUGIN_NAME = "[SuperSpawn] ";
	
	@Inject
	Logger logger;
	
	/**
	 * Point de spawn du serveur
	 * */
	private SpawnPoint spawn;
	
	/**
	 * Gestion du fichier de données sur disque pour sauver le point de spawn
	 * */
	private FileUtility fu;
	
	/**
	 * Ensemble des points home de chaque joueur (1 par joueur accessible via /home)
	 * La clef correspond au nom du joueur et la valeur à un objet SpawnPoint
	 * correspondant à sa maison
	 * Le point est chargé à la connexion du joueur et supprimé à sa déconnexion
	 * afin de limiter l'encombrement de la mémoire RAM
	 * */
	private Map<String, SpawnPoint> homes;

	@Listener
	public void onGameInit(GameInitializationEvent evt) {
		logger.info("Démarrage de superspawn...");
		fu = new FileUtility(this);
		fu.init();
		homes = new HashMap<>();
		
		// enregistrement de la commande setspawn
		CommandSpec cmdSpec = CommandSpec.builder()
				.description(Text.of("Commande pour définir le spawn"))
			    .permission("fr.labbeh.SuperSpawn.set")
			    .executor(new CommandSet(this))
			    .build();
		Sponge.getCommandManager().register(this, cmdSpec, "setspawn");
		
		// enregistrement de la commande spawn
		cmdSpec = CommandSpec.builder()
				.description(Text.of("Commande aller au point de spawn"))
			    .executor(new CommandSpawn(this))
			    .build();
		Sponge.getCommandManager().register(this, cmdSpec, "spawn");
		
		// enregistrement de la commande sethome
		cmdSpec = CommandSpec.builder()
				.description(Text.of("Créer son point home"))
				.executor(new CommandSethome(this))
				.build();
		Sponge.getCommandManager().register(this, cmdSpec, "sethome");
		
		// enregistrement de la commande home
		cmdSpec = CommandSpec.builder()
				.description(Text.of("Aller à son point home"))
				.executor(new CommandHome(this))
				.build();
		Sponge.getCommandManager().register(this, cmdSpec, "home");
		
	}
	
	/**
	 * Permet de définir le point de spawn du serveur dans le monde courant du joueur
	 * et de sauvgarder dans le fichier
	 * @param x position x
	 * @param y position y
	 * @param z position z
	 * */
	public void setSpawn(String worldName, double x, double y, double z) {
		spawn = new SpawnPoint(worldName, x, y, z);
		fu.saveOnFile();
	}
	
	/**
	 * Permet de définir le point de spawn du serveur dans le monde courant du joueur
	 * à partir d'une instance de SpawnPoint sans sauvegarde dans le fichier
	 * Utilisé entre autre au démarrage du serveur après lecture d'un fichier
	 * déjà existant
	 * @param point instance de SpawnPoint
	 * */
	public void setSpawn(SpawnPoint spawn) {
		this.spawn = spawn;
	}
	
	/**
	 * Ajoute un point home pour le joueur ou remplace l'existant
	 * @param playerName nom du joueur
	 * @param point SpawnPoint de la maison du joueur
	 * */
	public void addHome(String playerName, SpawnPoint point) {
		homes.put(playerName, point);
		fu.saveOnFile(FileUtility.CONFIG_FOLDER_URL + "/" +playerName+ ".conf", point);
	}
	
	/**
	 * Retourne le point de spawn
	 * @return point de spawn, null s'il n'y en a pas
	 * */
	public SpawnPoint getSpawn() {
		return spawn;
	}
	
	/**
	 * Retourne le point home du joueur dont le nom est passé
	 * en paramètre ou null si le joueur n'a pas de point home
	 * @param name nom du joueur
	 * @return un objet SpawnPoint correspondant au point de home
	 * du joueur, null si pas de point home pour ce joueur
	 * */
	public SpawnPoint getHomePoint(String name) {
		return homes.get(name);
	}
	
	/**
	 * Retourne un monde du serveur à partir de son nom
	 * @param worldName nom du monde
	 * @return le monde en lui même associé au nom
	 * */
	public World getWorld(String name) {
		return Sponge.getServer().getWorld(name).get();
	}
	
	/**
	 * Permet d'envoyer un message dans la console du joueur
	 * en ajoutant le nom du plugin et une couleur précise
	 * @param player instance du joueur destinataire du message
	 * @param color couleur dans laquelle apparaitra le message,
	 * des constantes sont définis dans la classe ChatColor
	 * @param msg contenu du message
	 * */
	public static void sendMsgToPlayer(Player player, TextColor color, String msg) {
		Text toPlayer = Text.builder( PLUGIN_NAME + msg).color(color).build();
		player.sendMessage(toPlayer);
	}
	
	public static void sendMsgToPlayer(Player player, String msg) {
		player.sendMessage(Text.of(PLUGIN_NAME+ msg));
	}
	
	/**
	 * Charge le point home à la connexion du joueur
	 * */
	@Listener
	public void onPlayerJoin(ClientConnectionEvent.Join evt) {
		Player player = (Player)evt.getTargetEntity();
		String name = player.getName();
		
		SpawnPoint home = fu.loadPointFromFile(FileUtility.CONFIG_FOLDER_URL + "/" +name+ ".conf");
		
		if(home != null)
			homes.put(name, home);
	}
	
	/**
	 * Nettoie la mémoire à la déconnexion du joueur en enlevant de la map
	 * le point home du joueur parti
	 * */
	@Listener
	public void onPlayerExit(ClientConnectionEvent.Disconnect evt) {
		Player player = (Player)evt.getTargetEntity();
		String name = player.getName();
		
		homes.remove(name);
	}
	
	@Listener
	public void onPlayerTeleport(MoveEntityEvent.Teleport.Portal tpEvt/*, DestructEntityEvent.Death deathEvt*/, @Getter("getTargetEntity") Player player) {
	    //event.setCancelled(true);
	    //player.transferToWorld(Sponge.getServer().getWorld("DIM6").get());
		player.sendMessage(Text.of("Joueur tp ou mort"));
	}

}
