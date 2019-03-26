package fr.istic.prg1.tp4;

import java.util.*;

/**
 * @author Maxime Hervé
 */
public class SmallSet {
	
	private boolean [] tab = new boolean[256];
	
	public SmallSet()
	{
		for(int i = 0; i <= 255; i++)
		{
			tab[i] = false;
		}
	}
	
	public SmallSet (boolean[] t)
	{
		for(int i = 0; i <= 255; i++)
		{
			tab[i] = t[i];
		}
	}
	
	public int size()
	{
		int size = 0;
		
		for(int i = 0; i <= 255; i++)
		{
			if(tab[i] == true)
			{
				size++;
			}
		}
		
		return size;
	}
	
	public boolean contains(int x)
	{
		if(tab[x] == true)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean isEmpty()
	{
		boolean isEmpty = true;
		
		for(int i = 0; i <= 255; i++)
		{
			if(tab[i] == true)
			{
				return false;
			}
		}
		
		return isEmpty;
	}
	
	public void add(int x)
	{
		if(!tab[x])
		{
			tab[x] = true;
		}
	}
	
	public void remove(int x)
	{
		if(tab[x] == true)
		{
			tab[x] = false;
		}
	}
	
	public void addInterval(int deb, int fin)
	{
		// assert deb < 0 && deb > fin && fin > 255 : "Probleme d'interval !";
		
		for(int i = deb; i <= fin; i++)
		{
			add(i);
		}
	}
	
	public void removeInterval(int deb, int fin)
	{
		// assert deb < 0 || deb > fin || fin > 255 : "Probleme d'interval !";
		
		for(int i = deb; i <= fin; i++)
		{
			remove(i);
		}
	}
	
	public void union(SmallSet set2)
	{
		for(int i = 0; i <= 255; i++)
		{
			if(set2.tab[i])
			{
				add(i);
			}
		}
	}
	
	public void intersection(SmallSet set2)
	{
		for(int i = 0; i <= 255; i++)
		{
			if(set2.tab[i] != this.tab[i])
			{
				remove(i);
			}
		}
	}
	
	public void difference(SmallSet set2)
	{
		for(int i = 0; i <= 255; i++)
		{
			if(set2.tab[i] == this.tab[i])
			{
				remove(i);
			}
		}
	}
	
	public void symmetricDifference(SmallSet set2)
	{
		for(int i = 0; i <= 255; i++)
		{
			if(set2.tab[i] == this.tab[i])
			{
				remove(i);
			}
			else if(set2.tab[i])
			{
				add(i);
			}
		}
	}
	
	public void complement()
	{
		for(int i = 0; i <= 255; i++)
		{
			if(tab[i] == true)
			{
				remove(i);
			}
			else
			{
				add(i);
			}
		}
	}
	
	public void clear()
	{
		removeInterval(0,255);
	}
	
	public boolean isIncludedIn(SmallSet set2)
	{
		for(int i = 0; i <= 255; i++)
		{
			if(tab[i] && !set2.tab[i])
			{
				return false;
			}
		}
		
		return true;
	}
	
	public SmallSet clone()
	{
		return new SmallSet(this.tab);
	}
	
	public boolean equals(SmallSet set2)
	{
		for(int i = 0; i <= 255; i++)
		{
			if(tab[i] != set2.tab[i])
			{
				return false;
			}
		}
		
		return true;
	}
	
	public String toString()
	{
		String s = "Elements presents : ";
		for(int i = 0; i <= 255; i++)
		{
			if(tab[i])
			{
				s += i + " ";
			}
		}
		
		return s;
	}

}
