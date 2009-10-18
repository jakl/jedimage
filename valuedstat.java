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
public class valuedstat {
	public int value;//quantity of damage or buff
	public stat status;//which stat is affected?
	
	public valuedstat(int value, stat status){
		this.value = value;
		this.status = status;
	}
	
	public valuedstat(valuedstat vs){
		value = vs.value;
		status = vs.status;
	}
	
	public static valuedstat getvaluedstat(String cost){
		String[] saValuedstat = cost.split(" ");
		valuedstat vs;
		switch(saValuedstat.length){
		case 1: vs = new valuedstat(0, stat.nothing);//assume cost is zero if no stat is mentioned
			break;
		case 2: 
			try{vs = new valuedstat(Integer.parseInt(saValuedstat[0]), stat.getstat(saValuedstat[1]));}
			catch(Exception e){e.printStackTrace(); return null;}
			break;
		default: return null;
		}
		return vs;
	}
	
	public String getstring(){
		return (value + " " + status.getstring());
	}
}
