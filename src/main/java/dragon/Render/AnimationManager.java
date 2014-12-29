package dragon.Render;

import java.util.ArrayList;
import java.util.List;

public class AnimationManager 
{
	static List<Object[]> animation = new ArrayList<Object[]>();
	
	public static void addAnimation(AnimationBase ani, ObjectRenderer obj)
	{
		if(ani.isObjectValid(obj))
		{
			animation.add(new Object[]{ani, obj, 0});
		}
	}
	
	public static void tick()
	{
		for(Object[] o : animation)
		{
			AnimationBase ani = (AnimationBase) o[0];
			ObjectRenderer obj = (ObjectRenderer) o[1];
			int i = (Integer) o[2];
			if(i>=0)
				ani.render(obj, i);
			i++ ;
			i = i > ani.getNeededTicks() ? 0 : i;
			o[2] = i;
		}
	}
	
	
}
