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
		ArrayList<Strategy> usedActivities = new ArrayList<Strategy>();

		int timeRemaining = 60;
		
		if(args.size() == 1) {
			try {
				timeRemaining = Integer.parseInt(args.get(0));
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
		
		timeRemaining -= opener.time + closer.time;
		usedActivities.add(opener);
		
		int mainActivityTime;
		while(true) {
			mainActivityTime = 0;
			
			mActivity1 = getActivity(mainActivities);
			mainActivityTime += mActivity1.time;
			usedActivities.add(mActivity1);
			
			if(mainActivityTime == timeRemaining)
				break;
			
			mActivity2 = getActivity(mainActivities, mActivity1);
			mainActivityTime += mActivity2.time;
			usedActivities.add(mActivity2);

			if(mainActivityTime == timeRemaining)
				break;
			
			mActivity3 = getActivity(mainActivities, mActivity1, mActivity2);
			mainActivityTime += mActivity3.time;
			usedActivities.add(mActivity3);

			if(mainActivityTime == timeRemaining)
				break;
			
			usedActivities.remove(mActivity1);
			usedActivities.remove(mActivity2);
			usedActivities.remove(mActivity3);	
		}
		
		usedActivities.add(closer);

		EmbedBuilder eb = new EmbedBuilder().setTitle("Session plan");
		
		eb.addInlineField("Opener", opener.name).addInlineField("Time", opener.time + "").addInlineField("VARK", opener.vark);
		
		if(mActivity1 != null)
			eb.addInlineField("Activity 1", mActivity1.name).addInlineField("Time", mActivity1.time + "").addInlineField("VARK", mActivity1.vark);
		
		if(mActivity2 != null)
			eb.addInlineField("Activity 2", mActivity2.name).addInlineField("Time", mActivity2.time + "").addInlineField("VARK", mActivity2.vark);
		
		if(mActivity3 != null)
			eb.addInlineField("Activity 3", mActivity3.name).addInlineField("Time", mActivity3.time + "").addInlineField("VARK", mActivity3.vark);
		
		eb.addInlineField("Closer", closer.name).addInlineField("Time", closer.time + "").addInlineField("VARK", closer.vark);
		
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
