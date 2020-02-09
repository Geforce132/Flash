package net.geforce.flashbot.commands;

import java.util.ArrayList;
import java.util.logging.Level;

import net.geforce.flashbot.FlashBot;

public class CommandManager {
	
	private ArrayList<Command> commands = new ArrayList<Command>();
	
	public CommandManager() {
		addCommand(new CommandActivity());
		addCommand(new CommandSessionPlan());
	}
	
	public void addCommand(Command command) {
		if(commands.contains(command)) {
			FlashBot.LOGGER.log(Level.WARNING, "A command named " + command.getName() + " already exists. This may cause unexpected results!");
		}
		
		commands.add(command);
	}

	public ArrayList<Command> getCommands() {
		return commands;
	}

}
