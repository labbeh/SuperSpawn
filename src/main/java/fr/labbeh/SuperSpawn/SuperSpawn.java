package fr.labbeh.SuperSpawn;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.world.World;

import com.google.inject.Inject;

import fr.labbeh.SuperSpawn.commands.CommandSet;
import fr.labbeh.SuperSpawn.commands.CommandSpawn;
import fr.labbeh.SuperSpawn.files.FileUtility;

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

	@Listener
	public void onGameInit(GameInitializationEvent evt) {
		logger.info("Démarrage de superspawn...");
		fu = new FileUtility(this);
		fu.init();
		
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
		
	}
	
	/**
	 * Permet de définir le point de spawn du serveur dans le monde courant du joueur
	 * @param x position x
	 * @param y position y
	 * @param z position z
	 * */
	public void setSpawn(String worldName, double x, double y, double z) {
		spawn = new SpawnPoint(worldName, x, y, z);
		fu.saveOnFile();
	}
	
	/**
	 * Retourne le point de spawn
	 * @return point de spawn, null s'il n'y en a pas
	 * */
	public SpawnPoint getSpawn() {
		return spawn;
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
	
	/*@Listener
	public void onPlayerJoin(ClientConnectionEvent.Join evt) {
		Player player = (Player)evt.getTargetEntity();
		//player.sendMessage(Text.of("Coucou"));
	}*/

}
