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
public class effect{
	public valuedstat vs;
	public target who;//true:target is self;  false:target is enemy
	
	public effect(valuedstat vs, target who){
		this.vs = vs;
		this.who = who;
	}
	
	public static effect geteffect(String s){
		String[] saAttribute = s.split(" ");
		effect attribute;
		switch(saAttribute.length){
		case 2:
			try{attribute = new effect( new valuedstat(Math.abs(Integer.parseInt(saAttribute[0])), stat.damage), target.gettarget(saAttribute[1]));}
			catch(Exception e){e.printStackTrace(); return null;}
			break;
		case 3: 
			try{attribute = new effect(valuedstat.getvaluedstat(saAttribute[0].concat(' '+saAttribute[1])), target.gettarget(saAttribute[2]));}
			catch(Exception e){e.printStackTrace(); return null;}
			break;
		default: return null;
		}
		return attribute;
	}
	
	public String getstring(){
		return vs.getstring() + " to " + who.getstring();
	}
}
