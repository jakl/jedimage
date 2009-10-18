/*
 * Copyright 2009 James Koval
 * 
 * This file is part of Jedi Mage
 * 
 * Jedi Mage is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 * 
 * Jedi Mage is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Jedi Mage. If not, see <http://www.gnu.org/license/>
 */
import java.util.ArrayList;

public class playergenerator {
	public player wizard;
	filemanager fileman;
	public String[] players;
	String playersfolder;
	
	/**
	 * Default constructor searches for deck txt files inside the decks folder. If it found decks, this.saDecks will be populated with valid decks.
	 * A valid deck is given to this.opendeck(String deckname). If the deck can be opened, this.library is populated with a deck.
	 */
	public playergenerator(){
		fileman = new filemanager();
		if(!getplayers())
			System.out.println("Couldn't find players");
	}
	
	/**
	 * Looks for decks in the decks folder
	 * @return false if the decks folder isn't found or if no decks are found
	 */
	boolean getplayers(){
		String[] saFolders;
		players = null;
		
		saFolders = fileman.ls();
		for(int i = 0; i < saFolders.length; i++)
			if(saFolders[i].equalsIgnoreCase("players")){//Search for decks folder and save the name
				players = fileman.ls(saFolders[i]);
				playersfolder = saFolders[i];
			}
		
		if(players == null || players.length == 0)//There is no decks folder or no docks
			return false;
		
		return true;
	}
	
	/**
	 * Opens a player by filename.
	 * @param deckname Name of the player including .txt
	 * @return false if player can't be opened or parsed. Otherwise, this.wizard is now usable
	 */
	public boolean open(String playername){
		//Check for valid deck
		boolean found = false;
		for(int i = 0; i < players.length; i++)
			if(playername.equalsIgnoreCase(players[i]))
				found = true;
		if(!found)
			return false;
		
		
		if(!lex(fileman.read("./"+playersfolder+"/"+playername), removetxt(playername)))
			return false;
		
		return true;
	}
	
	boolean lex(ArrayList<String> saPlayerinfo, String playername){
		if(saPlayerinfo == null || saPlayerinfo.size() < 2)
			return false;
		
		wizard = new player(playername);
		
		valuedstat vs = null;
		effect win = null;
		
		for(int i = 0; i < saPlayerinfo.size(); i++){
				switch(saPlayerinfo.get(i).split(" ").length){
					case 2: vs = valuedstat.getvaluedstat(saPlayerinfo.get(i));
						win = null;
						break;
					case 3: win = effect.geteffect(saPlayerinfo.get(i));
						vs = null;
						break;
					default: System.out.println("This line means nothing:\n"+saPlayerinfo.get(i));
				}
				if(win != null)
					wizard.addwincondition(win);
				else if(vs != null)
					wizard.setstat(vs);
				else
					System.out.println("This line wasn't parsed correctly:\n"+saPlayerinfo.get(i));
		}
		return true;
	}
	
	/**
	 * Determine if the first character of a string is a number
	 * @param s
	 * @return
	 */
	boolean startswithnum(String s){
		char c = s.charAt(0);
		if(c == '-' || (c >= '0' && c <= '9'))
			return true;
		return false;
	}
	
	String removetxt(String s){
		return s.substring(0, s.length()-4);
	}
}