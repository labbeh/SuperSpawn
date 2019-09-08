package fr.labbeh.SuperSpawn.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.format.TextColors;

import fr.labbeh.SuperSpawn.SpawnPoint;
import fr.labbeh.SuperSpawn.SuperSpawn;

public class CommandSethome implements CommandExecutor {
	/**
	 * Instance de la classe principale du plugin
	 * */
	private SuperSpawn ctrl;

	public CommandSethome(SuperSpawn ctrl) {
		this.ctrl = ctrl;
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(src instanceof Player) {
			Player player = (Player)src;
			
			double x = player.getPosition().getX();
			double y = player.getPosition().getY();
			double z = player.getPosition().getZ();
			String worldName = player.getWorld().getName();
			
			SpawnPoint point = new SpawnPoint(worldName, x, y, z);
			ctrl.addHome(player.getName(), point);
			
			SuperSpawn.sendMsgToPlayer(player, TextColors.GREEN, "Vous avez défini votre point de home en:"
					+ "[" +x+ ";" +y+ ";" +z+ "] dans le monde " +worldName);
		}
		return CommandResult.success();
	}
}
