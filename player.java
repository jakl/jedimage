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

public class player {
	public deck library;
	public String name;
	public ArrayList<effect> winconditions;
	public int magic, quarry, zoo, gem, brick, beast, tower, wall, handsizemax;
	public deck hand;
	public boolean alive, handtoolarge;
	
	void init(){
		magic = 1;
		quarry = 1;
		zoo = 1;
		gem = 10;
		brick = 10;
		beast = 10;
		tower = 30;
		wall = 15;
		handsizemax = 7;
		alive = true;
		winconditions = new ArrayList<effect>();
		handtoolarge = false;
	}
	
	public player(String name){
		this.name = name;
		hand = new deck(name+"'s Hand");
		init();
	}
	
	public player(player wizard){
		name = wizard.name;
		magic = wizard.magic;
		quarry = wizard.quarry;
		zoo = wizard.zoo;
		gem = wizard.gem;
		brick = wizard.brick;
		beast = wizard.beast;
		tower = wizard.tower;
		wall = wizard.wall;
		handsizemax = wizard.handsizemax;
		alive = wizard.alive;
		winconditions = new ArrayList<effect>(wizard.winconditions);
		handtoolarge = wizard.handtoolarge;
		hand = new deck(wizard.hand);
		library = new deck(wizard.library);
	}
	
	public void usedeck(deck library){
		this.library = new deck(library);
	}
	
	public void addwincondition(effect condition){
		winconditions.add(condition);
	}
	
	public void setstat(valuedstat startingstat){
		switch(startingstat.status){
			case magic: magic = startingstat.value; break;
			case quarry: quarry = startingstat.value; break;
			case zoo: zoo = startingstat.value; break;
			case gem: gem = startingstat.value; break;
			case tower: tower = startingstat.value; break;
			case wall: wall = startingstat.value; break;
			case beast: beast = startingstat.value; break;
			case brick: brick = startingstat.value; break;
			case handsizemax: handsizemax = startingstat.value; break;
		}
	}
	
	public boolean canplay(card c){
		switch(c.cost.status){
			case magic:if(magic-c.cost.value <= 0)return false;
			break;
			case quarry:if(quarry-c.cost.value <= 0)return false;
			break;
			case zoo:if(zoo-c.cost.value <= 0)return false;
			break;
			case gem:if(gem-c.cost.value < 0)return false;
			break;
			case wall:if(wall-c.cost.value < 0)return false;
			break;
			case beast:if(beast-c.cost.value < 0)return false;
			break;
			case brick:if(brick-c.cost.value < 0)return false;
			break;
			case handsizemax:if(handsizemax-c.cost.value <= 0)return false;
			break;
			case tower: if(tower-c.cost.value <= 0) return false;
			break;
			case nothing: break;
			default: return false;
		}
		return true;
	}
	
	public deck playablecards(){
		deck d = new deck("Playable");
		for(int i = 0; i < this.hand.size();i++)
			if(this.canplay(this.hand.get(i)))
				d.add(this.hand.get(i));
		return d;
	}
	
	/**
	 * Assumed: cost can be payed
	 * @param cost
	 */
	public void pay(valuedstat cost){
		valuedstat vs = new valuedstat(cost);
		vs.value *= -1;
		add(vs);
	}
	
	public void add(valuedstat vs){
		switch(vs.status){
		case magic: magic += vs.value;
		if(magic <= 0)
			magic = 1;
		break;
		case quarry: quarry += vs.value;
		if(quarry <= 0)
			quarry = 1;
		break;
		case zoo: zoo += vs.value;
		if(zoo <= 0)
			zoo = 1;
		break;
		case gem: gem += vs.value;
		if(gem < 0)
			gem = 0;
		break;
		case tower: tower += vs.value;
		if(tower < 0)
			alive = false;
		break;
		case wall: wall += vs.value;
		if(wall < 0)
			wall = 0;
		break;
		case beast: beast += vs.value;
		if(beast < 0)
			beast = 0;
		break;
		case brick: brick += vs.value;
		if(brick < 0)
			brick = 0;
		break;
		case handsizemax: handsizemax += vs.value;
		if(handsizemax <= 0)
			handsizemax = 1;
		break;
		case damage: takedamage(vs.value);
		if(tower < 0)
			alive = false;
		break;
	}
	}
	
	/**
	 * Draws/Discards until hand is full
	 */
	public void updatehand(){
		if(hand.size() < handsizemax)
			for(int i = hand.size(); i < handsizemax; i++)
				draw();
		else if(hand.size() == handsizemax)
			handtoolarge = false;
		else
			handtoolarge = true;
	}
	
	public void discard(int i){
		hand.remove(i);
		if(hand.size() <= handsizemax)
			handtoolarge = false;
	}
	
	/**
	 * Draw one random card if alive
	 */
	public void draw(){
		if(!alive)
			return;
		card draw;
		if((draw = library.draw())==null){
			alive = false;
			return;
		}
		hand.add(draw);
		if(hand.size() > handsizemax)
			handtoolarge = true;
	}
	
	public String showhand(){
		return hand.getstring();
	}
	
	public String stats(){
		return "\nMagic: "+magic+ " Quarry: "+quarry+ "  Zoo: "+zoo+ "\n  Gem: "+gem+ " Brick: "+brick+ " Beast: "+beast+ "\nTower: "+tower+ "  Wall: "+ wall;
	}
	/**
	 * Beginning of turn upkeep
	 * @return false:out of cards in library;  true:upkeep succeeded
	 */
	public void upkeep(){
		gem += magic;
		brick += quarry;
		beast += zoo;
		draw();
	}
	
	public void checkhandsize(){
		if(hand.size() <= handsizemax)
			handtoolarge = false;
		else
			handtoolarge = true;
	}
	
	/**
	 * Player takes damage hitting first the wall, then the tower
	 * @param damage
	 */
	public void takedamage(int damage){
		wall -= damage;
		if(wall < 0){
			tower -= Math.abs(wall);
			wall = 0;
			if(tower <= 0)
				alive = false;
		}
	}
}
