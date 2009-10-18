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

public class cardwitheffects extends card {
	ArrayList<effect> effects = new ArrayList<effect>();

	public cardwitheffects(String name, valuedstat cost, ArrayList<effect> effects) {
		super(name, cost);
		this.effects = effects;
	}
	
	public cardwitheffects(String name, valuedstat cost) {
		super(name, cost);
	}
	
	public boolean addeffect(String attribute){
		effect e = effect.geteffect(attribute);
		if(e == null)
			return false;
		effects.add(e);
		return true;
	}
	
	@Override
	public String getstring(){
		String s = super.getstring();
		for(int i = 0; i < effects.size(); i++)
			s+= "\n    "+effects.get(i).getstring();
		return s;
	}
	
	public boolean targetsenemy(){
		for(int i = 0; i < effects.size(); i++)
			if(effects.get(i).who == target.enemy)
				return true;
		return false;
	}
}
