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
public enum stat {
	magic, quarry, zoo, gem, brick, beast, tower, wall, damage, nothing, handsizemax;
	/**
	 * Search a string to find a value from the enum stat
	 * @param s
	 * @return
	 */
	static stat getstat(String s){
		switch(s.charAt(0)){
			case 'n': return stat.nothing;
			case 'm': return stat.magic;
			case 'q': return stat.quarry;
			case 'z': return stat.zoo;
			case 'g': return stat.gem;
			case 't': return stat.tower;
			case 'w': return stat.wall;
			case 'h': return stat.handsizemax;
			case 'b': switch(s.charAt(1)){
							case 'E': return stat.beast;
							case 'R': return stat.brick;
							case 'e': return stat.beast;
							case 'r': return stat.brick;
						}
			case 'N': return stat.nothing;
			case 'M': return stat.magic;
			case 'Q': return stat.quarry;
			case 'Z': return stat.zoo;
			case 'G': return stat.gem;
			case 'T': return stat.tower;
			case 'W': return stat.wall;
			case 'H': return stat.handsizemax;
			case 'B': switch(s.charAt(1)){
							case 'E': return stat.beast;
							case 'R': return stat.brick;
							case 'e': return stat.beast;
							case 'r': return stat.brick;
						}
			default: return null;
		}
	}
	
	public String getstring(){
		switch(this){
			case magic: return "magic";
			case quarry: return "quarry";
			case zoo: return "zoo";
			case gem: return "gem";
			case tower: return "tower";
			case wall: return "wall";
			case beast: return "beast";
			case brick: return "brick";
			case nothing: return "nothing";
			case damage: return "damage";
			case handsizemax: return "handsizemax";
			default: return null;
		}
	}
}
