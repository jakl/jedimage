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

public class game {
	public ArrayList<player> players;
	public int turn;//Holds index of currentplayer
	public player currentplayer;
	public boolean tie, gameover;
	
	public game(){
		init();
	}
	
	void init(){
		turn = -1;
		players = new ArrayList<player>();
	}
	
	public void add(player wizard){
		players.add(new player(wizard));
	}
	
	public void start(){
		//Ready players for the first turn where their upkeep brings their stats back to starting values
		for(int i = 0; i < players.size(); i ++){
			players.get(i).beast -= players.get(i).zoo;
			players.get(i).gem -= players.get(i).magic;
			players.get(i).brick -= players.get(i).quarry;
			for(int j = 0; j < 6; j++)
				players.get(i).draw();
		}
	}
	
	public boolean quickstart(int numberofplayers){
		deckgenerator dg = new deckgenerator();
		if(dg.decks == null || dg.decks.length == 0)//if no decks are found return false
			return false;
		deck library = null;
		for (int i = 0; i < dg.decks.length; i++){//Get quickstart deck to use
			if(dg.decks[i].equalsIgnoreCase("quickstart.txt"))
				if(dg.open(dg.decks[i]))
					library = dg.library;
		}
		if(library == null)//if quickstart deck isn't found return false
			return false;
		for(int i = 0; i < numberofplayers; i++){//Quickly add two players with standard decks
			player wizard = new player("Player "+i);
			wizard.usedeck(library);
			this.add(wizard);
		}
		return true;
	}
	
	public void newturn(){
		turn ++;
		if(turn == players.size())
			turn = 0;
		currentplayer = players.get(turn);
		currentplayer.upkeep();
	}
	
	public boolean canplay(card c){
		return players.get(turn).canplay(c);
	}
	
	/**
	 * Assumed: Can play the card
	 * @param cardindex
	 * @param targetindex
	 * @return false:can't pay cost
	 */
	public void playcard(int cardindex, int targetindex){
		//Determine targets: you, enemy
		player you = currentplayer;
		player enemy = players.get(targetindex);
		//Remove the card from the player's hand
		card ctemp = you.hand.play(cardindex);
		
		//See if the card is one with effects
		cardwitheffects c;
		try{c = (cardwitheffects)ctemp;}
		catch(Exception e){return;}
		
		you.pay(c.cost);
		
		for(int i = 0; i < c.effects.size(); i++){
			switch(c.effects.get(i).who){
			case you: you.add(c.effects.get(i).vs); break;
			case enemy: enemy.add(c.effects.get(i).vs); break;
			case all: for(int j = 0; j < players.size(); j++)
				players.get(j).add(c.effects.get(i).vs);
			break;
			}
		}
		you.checkhandsize();
	}
	
	public void playcard(int cardindex){
		//Determine targets: you
		player you = currentplayer;
		
		System.out.println("You are "+you.name);
		
		//Remove the card from the player's hand
		cardwitheffects c = (cardwitheffects)you.hand.play(cardindex);
		
		you.pay(c.cost);
		
		for(int i = 0; i < c.effects.size(); i++){
			switch(c.effects.get(i).who){
			case you: you.add(c.effects.get(i).vs);break;
			case all: for(int j = 0; j < players.size(); j++)
				players.get(j).add(c.effects.get(i).vs);
			break;
			}
		}
		you.checkhandsize();
	}
	
	public void remove(int index){
		players.remove(index);
		turn--;
	}
	
	/**
	 * @return winning player or null if none
	 */
	public player winner(){
		int living = 0;
		player victor = null;
		for(int i = 0; i < players.size(); i++)
			if(players.get(i).alive){
				living++;
				victor = players.get(i);
			}
		if(living == 1)
			return victor;
		return null;
	}
	
}
