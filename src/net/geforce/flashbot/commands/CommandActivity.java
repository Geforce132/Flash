package net.geforce.flashbot.commands;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.embed.EmbedBuilder;

import com.google.gson.Gson;

public class CommandActivity implements Command {
	
	public static final String NAME = "activity";

	@Override
	public void onMessageCreated(List<String> args, Message message, MessageAuthor author, TextChannel channel) {		
		String activity = String.join(" ", args);
		
		InputStream is = this.getClass().getResourceAsStream("cards.json");			
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		   
		Gson gson = new Gson();
		Activities activities = gson.fromJson(br, Activities.class);

		for(int i = 0; i < activities.strategies.length; i++) {
			if(activities.strategies[i].name.toLowerCase().equals(activity)){
				EmbedBuilder eb = new EmbedBuilder().setTitle(activities.strategies[i].name).setDescription(activities.strategies[i].description).addField("VARK", activities.strategies[i].vark);
				channel.sendMessage(eb);
				break;
			}
		}
	}
	
	@Override
	public String getName() {
		return NAME;
	}

}