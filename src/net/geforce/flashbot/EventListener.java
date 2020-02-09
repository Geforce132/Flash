package net.geforce.flashbot;

import java.util.ArrayList;

import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import net.geforce.flashbot.commands.Command;
import net.geforce.flashbot.misc.Utils;

public class EventListener implements MessageCreateListener {

	@Override
	public void onMessageCreate(MessageCreateEvent event) {
		if(event.getMessageContent().startsWith("!")) {
			ArrayList<String> commandArgs = new ArrayList<String>();
			
			for(String arg : event.getMessageContent().split(" "))
				commandArgs.add(arg);
			
			commandArgs.set(0, commandArgs.get(0).substring(1, commandArgs.get(0).length()));

			for(Command command : FlashBot.commandManager.getCommands()) {
				if(commandArgs.get(0).equals(command.getName())) {
					ArrayList<String> args = Utils.copyArray(commandArgs);
					args.remove(0);

					command.onMessageCreated(args, event.getMessage(), event.getMessageAuthor(), event.getChannel());
				}
			}
		}
	}

}
