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
public enum target {
	you, enemy, all;
	/**
	 * Search a string to find a target from the enum target
	 * @param s
	 * @return
	 */
	static target gettarget(String s){
		switch(s.charAt(0)){
			case 'y': return target.you;
			case 'e': return target.enemy;
			case 'a': return target.all;
			case 'Y': return target.you;
			case 'E': return target.enemy;
			case 'A': return target.all;
			default: return null;
		}
	}
	
	public String getstring(){
		switch(this){
		case you: return "you";
		case enemy: return "enemy";
		case all: return "all";
		default: return "null";
	}
	}
}
