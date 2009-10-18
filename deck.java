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


public class deck {
	ArrayList<card> cards;
	public String name;
	
	public deck(deck library){
		name = library.name;
		cards = new ArrayList<card>(library.cards);
	}
	
	public deck(String name){
		this.name = name;
		cards = new ArrayList<card>();
	}
	
	public card draw(){
		if(cards.size() == 0)
			return null;
		int index = (int)(Math.ceil(Math.random()*cards.size())-1);
		card spell = cards.get(index);
		cards.remove(index);
		return spell;
	}
	
	/**
	 * Removes the card at param:index from the deck and gives it as the return
	 * @param index
	 * @return
	 */
	public card play(int index){
		card spell = cards.get(index);
		cards.remove(index);
		return spell;
	}
	
	public void remove(int index){
		cards.remove(index);
	}
	
	public void add(card spell){
		cards.add(spell);
	}
	
	public card get(int i){
		return cards.get(i);
	}
	
	public int size(){
		return cards.size();
	}
	
	public String getstring(){
		String s = "";
		for(int i = 0; i < cards.size(); i++)
			s += "\n"+cards.get(i).getstring();
		return s;
	}
	
	public String getindexedstring(){
		String s = "";
		for(int i = 0; i < cards.size(); i++)
			s += "\n"+i+")\n"+cards.get(i).getstring();
		return s;
	}
	
	public int search(String name){
		for(int i = 0; i < this.size(); i++)
			if(this.get(i).name.equalsIgnoreCase(name))
				return i;
		return -1;
	}
}
