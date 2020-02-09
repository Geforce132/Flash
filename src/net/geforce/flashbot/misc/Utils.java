package net.geforce.flashbot.misc;

import java.util.ArrayList;
import java.util.Random;

import org.javacord.api.entity.message.MessageAuthor;

public class Utils {
	
	public static void sendPrivateMessageTo(MessageAuthor author, String message) {
		author.asUser().get().sendMessage(message);
	}
	
	public static int getRandomNumber(int upperBound) {
		Random random = new Random();
		
		return random.nextInt(upperBound);
	}
	
	public static <M> ArrayList<M> copyArray(ArrayList<M> list) {
		ArrayList<M> copiedlist = new ArrayList<M>();
		
		for(int i = 0; i < list.size(); i++) {
			M var = list.get(i);
			copiedlist.add(var);
		}
		
		return copiedlist;
	}

}
