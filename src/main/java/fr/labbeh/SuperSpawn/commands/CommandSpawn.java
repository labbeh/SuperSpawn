package fr.labbeh.SuperSpawn.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.flowpowered.math.vector.Vector3d;

import fr.labbeh.SuperSpawn.SuperSpawn;

public class CommandSpawn implements CommandExecutor{
	/**
	 * Instance de la classe princiaple afin de partager les informations sur le point de spawn
	 * */
	private SuperSpawn ctrl;
	
	/**
	 * Constructeur par défaut
	 * @param ctrl pointeur vers le classe principale du plugin
	 * */
	public CommandSpawn(SuperSpawn ctrl) {
		super();
		this.ctrl = ctrl;
	}



	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(src instanceof Player) {
			Player player = (Player)src;
			
			if(ctrl.getSpawn() == null) {
				SuperSpawn.sendMsgToPlayer(player, TextColors.RED, "Pas de spawn défini sur le serveur, contactez votre administrateur");
				return CommandResult.success();
			}
			
			String worldName;
			double x, y, z;
			
			worldName = ctrl.getSpawn().getWorld();
			x = ctrl.getSpawn().getX();
			y = ctrl.getSpawn().getY();
			z = ctrl.getSpawn().getZ();
			
			Vector3d position = new Vector3d(x, y, z);
			player.setLocation(position , ctrl.getWorld(worldName).getUniqueId());
			
			SuperSpawn.sendMsgToPlayer(player, TextColors.GREEN, "Retour au spawn...");
			
		}
		else
			src.sendMessage(Text.of("ERREUR: vous devez être un joueur pour exécuter cette commande"));
		
		
        return CommandResult.success();
	}

}
