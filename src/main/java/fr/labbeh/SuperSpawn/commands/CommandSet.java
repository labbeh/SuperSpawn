package fr.labbeh.SuperSpawn.commands;

import java.text.DecimalFormat;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import fr.labbeh.SuperSpawn.SuperSpawn;

public class CommandSet implements CommandExecutor {
	/**
	 * Pointeur vers la classe principale afin de communiquer le point de spawn
	 * */
	private SuperSpawn ctrl;
	
	

	public CommandSet(SuperSpawn ctrl) {
		super();
		this.ctrl = ctrl;
	}



	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(src instanceof Player) {
			Player player = (Player)src;
			
			double x, y, z;
			x = player.getPosition().getX();
			y = player.getPosition().getY();
			z = player.getPosition().getZ();
			
			ctrl.setSpawn(player.getWorld().getName(), x, y, z);
			
			// affichage de ce qui viens d'être fait
			DecimalFormat df = new DecimalFormat("0.##");
			SuperSpawn.sendMsgToPlayer(player, TextColors.GOLD, "Spawn créé en: { " 
										+df.format(x)+ ";"
										+df.format(y)+ ";" 
										+df.format(z)+ " }");
		}
		else
			src.sendMessage(Text.of("ERREUR: vous devez être un joueur pour exécuter cette commande"));
		
		
        return CommandResult.success();
	}

}
