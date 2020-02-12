package net.geforce.flashbot.commands.strategies;

/**
 * A dummy class that lists the name of and describes every strategy loaded from cards.json
 */
public class Strategy {
	
	public String name;
	public String description;
	public String mainActivity;
	public String vark;
	public int time;

	public String getAllEmojis() {
		String s = "";
		
		if(vark.contains("V"))
			s += ":eyes:";
		
		if(vark.contains("A"))
			s += (s.length() > 0 ? " " : "") + ":speaking_head:";
		
		if(vark.contains("R"))
			s += (s.length() > 0 ? " " : "") + ":pencil:";
		
		if(vark.contains("K"))
			s += (s.length() > 0 ? " " : "") + ":man_running:";
		
		return s;
	}

}
