package net.geforce.flashbot.commands;

import java.util.List;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;

/**
 * A base class that every other Command extends
 */
public interface Command {
	
	public void onMessageCreated(List<String> args, Message message, MessageAuthor author, TextChannel channel);
	
	public String getName();

}
