package net.geforce.flashbot;

import java.util.logging.Logger;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import net.geforce.flashbot.commands.CommandManager;
import net.geforce.flashbot.misc.Passwords;

public class FlashBot {
	
public static final String VERSION = "1.0.0";
	
	public static final Logger LOGGER = Logger.getLogger("Flash");
	
	public static DiscordApi api;
	
	public static CommandManager commandManager = new CommandManager();
	public static EventListener eventListener = new EventListener();
	
	public static void main(String[] args) {
        api = new DiscordApiBuilder().setToken(Passwords.DISCORD_TOKEN).login().join();

        api.addMessageCreateListener(eventListener);
	}

}
