package dragon.Render;

public abstract class AnimationBase 
{
	abstract boolean isObjectValid(ObjectRenderer obj);
	
	abstract int getNeededTicks();
	
	abstract void render(ObjectRenderer obj, int tick);
	
	public boolean contains(ObjectRenderer obj, String... par1)
	{
		for(String s : par1)
		{
			if(!obj.containsObj(s))
			{
				return false;
			}
		}
		return true;
	}
	
	public float uptoSoft(float value, int starttick, int lengh, int tick)
	{
		double d = Math.PI / lengh;
		if(tick>=starttick && tick<=(starttick+lengh))
		{
			double c = (Math.cos(d * (tick - starttick) + Math.PI) + 1) / 2;
			return (float) (c * value);
		}
		
		return (starttick+lengh)<tick ? value : 0;
	}
	
	public float uptoLinear(float value, int starttick, int lengh, int tick)
	{
		double d = value/lengh;
		if(tick>=starttick && tick<=(starttick+lengh))
		{
			double c = d * (tick-starttick);
			return (float) (c * value);
		}
		
		return (starttick+lengh)<tick ? value : 0;
	}
}
