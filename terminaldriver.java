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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class terminaldriver {
	game g;
	BufferedReader cin;//console input -- named c++ style

	void init(){
		cin = new BufferedReader(new InputStreamReader(System.in));
		game();
	}
	
	void game(){
		g = new game();
		//Push terminal content off screen by 100 lines
		for(int i = 0; i < 20; i++)
			System.out.println("\n\n\n\n");
		System.out.println("Welcome to Jedi Mage!\n");
		System.out.println("This is a card game of skill and luck.");
		System.out.println("It is licensed under the GNU General Public License v3+");
		System.out.println("At any point type <ctrl>+<c> to exit.");
		System.out.println("\nView Rules?");
		if(yes())
			System.out.println(rules());
		System.out.println("Quickstart?");
		if(yes())
			g.quickstart(2);//two players
		else
			addplayers();
		g.start();//Draw 6 cards so the 7th is drawn on first turn
		
		//Game Loop until there is a winner
		while(g.winner() == null){
			g.newturn();
			
			//Push last player's turn off screen by 100 lines
			for(int i = 0; i < 20; i++)
				System.out.println("\n\n\n\n");
			for(int i = 0; i < g.players.size();i++)
				System.out.println("\n"+g.players.get(i).name+g.players.get(i).stats());
			System.out.println("\nEnter to continue with "+ g.currentplayer.name +"'s turn");
			yes();
			
			//Check if library is drawn out
			if(!g.currentplayer.alive){
				System.out.println(g.currentplayer.name+" drew out his library and lost");
				g.remove(g.turn);//turn holds the index of the current player
				continue;
			}
			
			//Discard if hand too large
			while(g.currentplayer.handtoolarge)
				discard();
			
			//play phase
			playphase();
			//Check if play phase killed him
			if(!g.currentplayer.alive){
				System.out.println(g.currentplayer.name+" just committed suicide");
				g.remove(g.turn);//turn holds the index of the current player
			}
			
			checkfordeaths();
			
			if(g.players.size() == 0){
				System.out.println("It appears to be a tie!");
				System.exit(0);
			}
			System.out.println("Enter to end turn");
			yes();
		}
		endgame();
		
	}
	
	void checkfordeaths(){
		for(int i = 0; i < g.players.size(); i++)
			if(!g.players.get(i).alive){
				System.out.println(g.players.get(i).name+" was killed");
				g.remove(i);
			}
	}
	
	void playphase(){
		System.out.print(g.currentplayer.hand.getstring());
		if(g.currentplayer.playablecards().size()==0){
			System.out.println("\nYou have no playable cards");
			return;
		}
		System.out.println("\n\nPlayable cards:"+"\n"+g.currentplayer.playablecards().getstring());
		System.out.println("\nPlay a card?");
		if(!yes())
			return;
		int cardindex = 0;
		int target = 0;
		
		cardindex = choosecardtoplay();
		for(int i = 0; i < 20; i++)
			System.out.println("\n\n\n\n");
		System.out.println(g.currentplayer.name + " played"+g.currentplayer.hand.get(cardindex).getstring());
		if(((cardwitheffects)g.currentplayer.hand.get(cardindex)).targetsenemy())
		{
			target = choosetarget();
			g.playcard(cardindex, target);
			System.out.println("\n"+g.players.get(target).name+g.players.get(target).stats());
		}
		else
			g.playcard(cardindex);
		System.out.println("\n"+g.currentplayer.name+g.currentplayer.stats());
	}
	
	int choosetarget(){
		int choice = 0;
		while(true){
			for(int i = 0; i < g.players.size(); i++)
				System.out.println(i+") "+g.players.get(i).name);
			System.out.println("Which target do you choose?");
			
			//Test for number 
			try{choice = Integer.parseInt(cin.readLine());}
			catch(Exception e){
				System.out.println("\nPick using a number");
				continue;
				}
			
			//Test for number within range
			if(choice >= g.currentplayer.hand.size()){
				System.out.println("\nPick using an avaliable corresponding number");
				continue;
			}
			break;
		}
		return choice;
	}
	
	void discard(){
		System.out.println("You hand size has gone over your maximum. Please choose a card to discard:");
		g.currentplayer.discard(choosecard());
		for(int i = 0; i < g.players.size();i++)
			System.out.println("\n"+g.players.get(i).name+g.players.get(i).stats());
	}
	
	int choosecard(){
		int choice = 0;
		while(true){
			System.out.println(g.currentplayer.hand.getindexedstring());
			System.out.println("Which card do you choose?");
			
			//Test for number
			try{choice = Integer.parseInt(cin.readLine());}
			catch(Exception e){
				System.out.println("\nPick using a number");
				continue;
				}
			
			//Test for number within range
			if(choice >= g.currentplayer.hand.size()){
				System.out.println("\nPick using an avaliable corresponding number");
				continue;
			}
			break;
		}
		return choice;
	}
	
	int choosecardtoplay(){
		int choice = 0;
		deck playable = g.currentplayer.playablecards();
		while(true){
			for(int i = 0; i < g.players.size();i++)
				System.out.println("\n"+g.players.get(i).name+g.players.get(i).stats());
			System.out.println(playable.getindexedstring());
			System.out.println("Which card do you choose?");
			
			//Test for number
			try{choice = Integer.parseInt(cin.readLine());}
			catch(Exception e){
				System.out.println("\nPick using a number");
				continue;
				}
			
			//Test for number within range
			if(choice >= playable.size()){
				System.out.println("\nPick using an avaliable corresponding number");
				continue;
			}
			break;
		}
		return g.currentplayer.hand.search(playable.get(choice).name);
	}
	
	void endgame(){
		System.out.println(g.winner().name+" is the winner");
		System.exit(0);
	}
	
	void addplayers(){
		System.out.println("\nPicking at least 2 players...");//A little bit of white space
		
		//Pick players and decks until at least 2 are chosen
		player wizard;
		while(g.players.size() < 2){
			wizard = chooseplayer();
			wizard.usedeck(choosedeck());
			g.add(wizard);
		}
		
		System.out.println("\nNow that you have the minimum of two players,");
		System.out.println("shall we add more or start the game?");
		
//		Pick more than 2 players
		while(true){
			System.out.println("Add another player?");
			if(!yes())
				break;
			wizard = chooseplayer();
			wizard.usedeck(choosedeck());
			g.add(wizard);
		}
	}
	
	boolean yes(){
		try{return isyes(cin.readLine());}
		catch(Exception e){return false;}
	}
	
	boolean isyes(String s){
		if(s == null)
			return false;
		switch(s.charAt(0)){
		case 'y': return true;
		case 't': return true;
		case 'Y': return true;
		case 'T': return true;
		case '1': return true;
		}
		return false;
	}
	
	player chooseplayer(){
		playergenerator pg = new playergenerator();
		int choice = 0;
		while(true){
			for(int i = 0; i < pg.players.length; i++)
				System.out.println(i+") "+pg.players[i]);
			System.out.println("Which player do you choose?");
			
			//Test for number
			try{choice = Integer.parseInt(cin.readLine());}
			catch(Exception e){
				System.out.println("Pick using a number");
				continue;
				}
			
			//Test for number within range
			if(choice >= pg.players.length){
				System.out.println("Pick using an avaliable corresponding number");
				continue;
			}
			
			//Test for valid deck format
			if(!pg.open(pg.players[choice])){
				System.out.println("This is a bad player; don't use it until it is fixed!");
				continue;
			}
			break;
		}
		return pg.wizard;
	}
	
	deck choosedeck(){
		deckgenerator dg = new deckgenerator();
		int choice = 0;
		while(true){
			for(int i = 0; i < dg.decks.length; i++)
				System.out.println(i+") "+dg.decks[i]);
			System.out.println("Which deck do you choose?");
			
			//Test for number
			try{choice = Integer.parseInt(cin.readLine());}
			catch(Exception e){
				System.out.println("Pick using a number");
				continue;
				}
			
			//Test for number within range
			if(choice >= dg.decks.length){
				System.out.println("Pick using an avaliable corresponding number");
				continue;
			}
			
			//Test for valid deck format
			if(!dg.open(dg.decks[choice])){
				System.out.println("This is a bad deck; don't use it until it is fixed!");
				continue;
			}
			break;
		}
		return dg.library;
	}
	
	String rules(){
		filemanager r = new filemanager();
		ArrayList<String> als = r.read("RULES");
		String s = "";
		for(int i = 0; i < als.size(); i++)
			s += "\n"+als.get(i);
		return s;
	}

}