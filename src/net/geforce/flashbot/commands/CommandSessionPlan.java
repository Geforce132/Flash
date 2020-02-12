package net.geforce.flashbot.commands;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.embed.EmbedBuilder;

import com.google.gson.Gson;

import net.geforce.flashbot.commands.strategies.Activities;
import net.geforce.flashbot.commands.strategies.Strategy;
import net.geforce.flashbot.misc.Utils;

public class CommandSessionPlan implements Command {
	
	public static final String NAME = "sessionplan";

	@Override
	public void onMessageCreated(List<String> args, Message message, MessageAuthor author, TextChannel channel) {		
		InputStream is = this.getClass().getResourceAsStream("cards.json");			
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		   
		Gson gson = new Gson();
		Activities allActivities = gson.fromJson(br, Activities.class);
		ArrayList<Strategy> mainActivities = new ArrayList<Strategy>();
		ArrayList<Strategy> openCloseActivities = new ArrayList<Strategy>();

		// The deafult session time is 60 minutes (one hour)
		int timeRemaining = 60;
		
		if(args.size() == 1) {
			try {
				timeRemaining = Integer.parseInt(args.get(0));
				if(timeRemaining < 30 || timeRemaining % 10 != 0) {
					channel.sendMessage("Times have to be 30 minutes or more and divisible by 10.");
					return;
				}
			}
			catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}

		for(int i = 0; i < allActivities.strategies.length; i++) {
			if(allActivities.strategies[i].mainActivity.equals("true"))
				mainActivities.add(allActivities.strategies[i]);
			else
				openCloseActivities.add(allActivities.strategies[i]);
		}

		Strategy opener = getActivity(openCloseActivities);
		Strategy closer = getActivity(openCloseActivities, opener);
		Strategy mActivity1 = null;
		Strategy mActivity2 = null;
		Strategy mActivity3 = null;
		String learningTypesUsed = "";

		learningTypesUsed += opener.vark;
		learningTypesUsed += closer.vark;
		
		timeRemaining -= opener.time + closer.time;
		
		// Keep creating new plans until all of VARK is included
		while(true) {

			// Keep creating new plans until the times of the activities add up to the time needed
			int mainActivityTime;
			while(true) {
				mainActivityTime = 0;
				
				mActivity1 = getActivity(mainActivities);
				mainActivityTime += mActivity1.time;
				learningTypesUsed += mActivity1.vark;
				if(mainActivityTime == timeRemaining)
					break;
				
				mActivity2 = getActivity(mainActivities, mActivity1);
				mainActivityTime += mActivity2.time;
				learningTypesUsed += mActivity2.vark;
				if(mainActivityTime == timeRemaining)
					break;
				
				mActivity3 = getActivity(mainActivities, mActivity1, mActivity2);
				mainActivityTime += mActivity3.time;
				learningTypesUsed += mActivity3.vark;
				if(mainActivityTime == timeRemaining)
					break;
				
				learningTypesUsed = "";
			}
			
			if(learningTypesUsed.contains("V") && learningTypesUsed.contains("A") && learningTypesUsed.contains("R") && learningTypesUsed.contains("K")) {
				learningTypesUsed = "";
				break;
			}
		}

		postSessionPlan(opener, mActivity1, mActivity2, mActivity3, closer, channel);
	}
	
	private void postSessionPlan(Strategy opener, Strategy act1, Strategy act2, Strategy act3, Strategy closer, TextChannel channel) {
		EmbedBuilder eb = new EmbedBuilder().setTitle("Session plan");

		String s = "**Opener**: " + opener.name + " " + opener.getAllEmojis() + "\nTime: " + opener.time + "m - VARK: " + opener.vark;
		
		if(act1 != null)
			s += "\n\n**Activity 1**: " + act1.name + " " + act1.getAllEmojis() + "\nTime: " + act1.time + "m - VARK: " + act1.vark;
		
		if(act2 != null)		
			s += "\n\n**Activity 2**: " + act2.name + " " + act2.getAllEmojis() + "\nTime: " + act2.time + "m - VARK: " + act2.vark;

		if(act3 != null)
			s += "\n\n**Activity 3**: " + act3.name + " " + act3.getAllEmojis() + "\nTime: " + act3.time + "m - VARK: " + act3.vark;

		s += "\n\n**Closer**: " + closer.name + " " + closer.getAllEmojis() + "\nTime: " + closer.time + "m - VARK: " + closer.vark;

		eb.setDescription(s);
		channel.sendMessage(eb);
	}
	
	private Strategy getActivity(ArrayList<Strategy> activities) {
		return activities.get(Utils.getRandomNumber(activities.size()));
	}
	
	private Strategy getActivity(ArrayList<Strategy> activities, Strategy... usedActivities) {
		Strategy strategy = null;
		
		boolean foundActivity = false;
		while(!foundActivity) {
			strategy = activities.get(Utils.getRandomNumber(activities.size()));
			
			boolean activityInList = false;
			for(int i = 0; i < usedActivities.length; i++) {
				if(strategy.name.equals(usedActivities[i].name))
					activityInList = true;
			}
			
			if(!activityInList)
				foundActivity = true;
		}

		return strategy;
	}
	
	@Override
	public String getName() {
		return NAME;
	}

}
