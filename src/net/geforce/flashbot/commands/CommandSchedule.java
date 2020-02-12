package net.geforce.flashbot.commands;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.embed.EmbedBuilder;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import net.geforce.flashbot.misc.Passwords;

public class CommandSchedule implements Command {
	
	public static final String NAME = "schedule";
	private static final String SCHEDULE_SHEET_ID = "1rJAHHYB5chCweTuAwqrJD7ZuwWoWCoC6bav6RnwFJfY";

	@Override
	public void onMessageCreated(List<String> args, Message message, MessageAuthor author, TextChannel channel) {
		
		if(args.size() != 1) {
			channel.sendMessage("Include the first name of the SI leader that you want to search for.");
			return;
		}

		String leaderName = args.get(0);
		
		try {
			NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			String nameRange = "Spring 2020!E4:E28";
			String timeRange = "Spring 2020!F4:F28";
			String roomRange = "Spring 2020!G4:G28";
			String noteRange = "Spring 2020!I4:I28";

			Sheets service = new Sheets.Builder(HTTP_TRANSPORT, Passwords.JSON_FACTORY, Passwords.getSheetsAPICredentials(HTTP_TRANSPORT)).setApplicationName(Passwords.APPLICATION_NAME).build();
	        ValueRange nameResponses = service.spreadsheets().values().get(SCHEDULE_SHEET_ID, nameRange).execute();
	        ValueRange timeResponses = service.spreadsheets().values().get(SCHEDULE_SHEET_ID, timeRange).execute();
	        ValueRange roomResponses = service.spreadsheets().values().get(SCHEDULE_SHEET_ID, roomRange).execute();
	        ValueRange noteResponses = service.spreadsheets().values().get(SCHEDULE_SHEET_ID, noteRange).execute();

	        List<List<Object>> nameValues = nameResponses.getValues();
	        List<List<Object>> timeValues = timeResponses.getValues();
	        List<List<Object>> roomValues = roomResponses.getValues();
	        List<List<Object>> noteValues = noteResponses.getValues();
	        if (nameValues == null || nameValues.isEmpty()) {
	            System.out.println("No data found.");
	        } 
	        else {
	        	
	        	boolean foundLeader = false;

	        	for(int i = 0; i < nameValues.size(); i++) {
	        		List<?> row = nameValues.get(i);

	        		if(row.get(0).toString().equalsIgnoreCase(leaderName)) {		        		
		        		String times = timeValues.get(i).get(0).toString();
		        		
		        		// For leaders which have links to separate schedule spreadsheets
		        		if(times.startsWith("www.")) {
		        			channel.sendMessage("See " + times + " for " + row.get(0) + "'s session times.");
		        			return;
		        		}
		        		
		        		String rooms = roomValues.get(i).get(0).toString();
		        		String[] timeLines = times.split("\\r?\\n");
		        		String[] roomLines = rooms.split("\\r?\\n");
		        		
		        		EmbedBuilder eb = new EmbedBuilder().setTitle(row.get(0) + "'s session times and locations");
		        		String description = "";

		        		for(int j = 0; j < timeLines.length; j++) {
		        			description += timeLines[j] + " | " + roomLines[j] + "\n";
		        		}
		        		
		        		eb.setDescription(description);
		        		
		        		try {
		        			eb.addField("Special note", noteValues.get(i).get(0).toString());
		        		}
		        		catch(IndexOutOfBoundsException e) {}
		        		
		        		channel.sendMessage(eb);
		        		
		        		foundLeader = true;
		        		break;
	        		}
	            }
	        	
	        	if(!foundLeader)
	        		channel.sendMessage("Times for leader '" + leaderName + "' not found.");
	        }
        
		} catch (GeneralSecurityException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		return NAME;
	}

}
