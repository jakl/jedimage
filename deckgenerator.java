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

public class deckgenerator {
	public deck library;
	filemanager fileman;
	public String[] decks;
	String decksfolder;
	
	/**
	 * Default constructor searches for deck txt files inside the decks folder. If it found decks, this.saDecks will be populated with valid decks.
	 * A valid deck is given to this.opendeck(String deckname). If the deck can be opened, this.library is populated with a deck.
	 */
	public deckgenerator(){
		fileman = new filemanager();
		if(!getdecks())
			System.out.println("Couldn't find decks");
	}
	
	/**
	 * Looks for decks in the decks folder
	 * @return false if the decks folder isn't found or if no decks are found
	 */
	boolean getdecks(){
		String[] saFolders;
		decks = null;
		
		saFolders = fileman.ls();
		for(int i = 0; i < saFolders.length; i++)
			if(saFolders[i].equalsIgnoreCase("decks")){//Search for decks folder and save the name
				decks = fileman.ls(saFolders[i]);
				decksfolder = saFolders[i];
			}
		
		if(decks == null || decks.length == 0)//There is no decks folder or no docks
			return false;
		
		return true;
	}
	
	/**
	 * Opens a deck by filename.
	 * @param deckname Name of the deck included .txt
	 * @return false if deck can't be opened or parsed. Otherwise, library is now populated with a deck
	 */
	public boolean open(String deckname){
		//Check for valid deck
		boolean found = false;
		for(int i = 0; i < decks.length; i++)
			if(deckname.equalsIgnoreCase(decks[i]))
				found = true;
		if(!found)
			return false;
					
		
		if(!lex(fileman.read("./"+decksfolder+"/"+deckname), removetxt(deckname)))
			return false;
		
		return true;
	}
	
	boolean lex(ArrayList<String> saCardlist, String deckname){
		if(saCardlist == null || saCardlist.size() < 2)
			return false;
		
		library = new deck(deckname);
		
		//Organize list of all cards into sublists of individual cards
		ArrayList<ArrayList<String>> cards = new ArrayList<ArrayList<String>>();
		ArrayList<String> tempcard = null;
		for(int i=0; i < saCardlist.size(); i++){
			if(!startswithnum(saCardlist.get(i))){
				if(tempcard != null)
					cards.add(tempcard);
				tempcard = new ArrayList<String>();
				tempcard.add(saCardlist.get(i));//Card name
			}
			else{
				tempcard.add(saCardlist.get(i));//Card data: cost, effects
				if(i == saCardlist.size()-1)//Add the last card on the list
					cards.add(tempcard);
			}
		}
		
		cardwitheffects spell;
		String name;
		valuedstat cost;
		for(int i = 0; i < cards.size(); i++){
			
			//Get the card name and cost and create the card
			try{
				name = cards.get(i).get(0);
				//System.out.println("Name text:"+cards.get(i).get(0));
				cost = valuedstat.getvaluedstat(cards.get(i).get(1));
				//System.out.println("Cost text:"+cost.value);
			}catch(Exception e){e.printStackTrace(); return false;}
			if(cost == null)
				return false;
			spell = new cardwitheffects(name, cost);//create card
			//System.out.println("    spell.getstring:\n" + spell.getstring()+"\n    end spell.getstring");
			
			for(int j = 2; j < cards.get(i).size(); j++){//cycle through the info on each card
				//System.out.println("Effect text: "+cards.get(i).get(j));
				if(!spell.addeffect(cards.get(i).get(j)))//Add the card's effects
					return false;
			}
			//System.out.println("    spell.getstring:\n" + spell.getstring()+"\n    end spell.getstring");
			library.add(spell);//Add the card to the library
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