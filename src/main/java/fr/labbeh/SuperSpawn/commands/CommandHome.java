package fr.labbeh.SuperSpawn.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.format.TextColors;

import com.flowpowered.math.vector.Vector3d;

import fr.labbeh.SuperSpawn.SpawnPoint;
import fr.labbeh.SuperSpawn.SuperSpawn;

public class CommandHome implements CommandExecutor {
	/**
	 * Instance de la classe principale du plugin
	 * */
	private SuperSpawn ctrl;

	public CommandHome(SuperSpawn ctrl) {
		this.ctrl = ctrl;
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(src instanceof Player) {
			Player player = (Player)src;
			SpawnPoint home = ctrl.getHomePoint(player.getName());
			
			if(home != null) {
				Vector3d location = new Vector3d(home.getX(), home.getY(), home.getZ());
				player.setLocation(location, ctrl.getWorld(home.getWorld()).getUniqueId());
				SuperSpawn.sendMsgToPlayer(player, TextColors.GREEN, "Retour à la maison...");
			}
			else SuperSpawn.sendMsgToPlayer(player,
											TextColors.RED,
											"Vous n'avez pas encore défini de point home\n"
											+ "Utilisez /sethome pour utiliser votre position "
											+ "courante comme point home");
		}
		return CommandResult.success();
	}

}
