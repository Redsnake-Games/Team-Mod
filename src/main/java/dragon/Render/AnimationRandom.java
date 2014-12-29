package dragon.Render;


public class AnimationRandom extends AnimationBase {

	@Override
	boolean isObjectValid(ObjectRenderer obj) 
	{
		return true;
	}

	@Override
	int getNeededTicks() 
	{
		return 248;
	}

	@Override
	void render(ObjectRenderer obj, int tick) 
	{
		for(int i=0; i<obj.getAllObj().size();i++)
		{
			float f = 90 - uptoSoft(90, 0, 124, tick+i);
			
			obj.getAllObj().get(i).setRotation(f, f, f);
			
			float f2 = (float) Math.sin((tick-i * 2) * 0.025);
			f2 = (tick-i * 2)>124?0:f2;
			obj.getAllObj().get(i).setPositon(0, f2, 0);			
		}		
	}

}
