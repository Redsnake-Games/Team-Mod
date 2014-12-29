package dragon.Render;

public class AnimationWing extends AnimationBase {

	@Override
	boolean isObjectValid(ObjectRenderer obj) 
	{
		return contains(obj, "head", "body", "leftarm", "rightarm", "leftleg", "rightleg");			
	}

	@Override
	int getNeededTicks() 
	{
		return 158;
	}

	@Override
	void render(ObjectRenderer obj, int tick) 
	{
		float rot = (float) Math.sin(tick * 0.1);
		
		obj.getObject("leftarm").addRotation(0, 0, rot * 180);
		if(tick > 15 && tick < 139)
		{
			obj.getObject("leftarm").addRotation(0, 0, 150 + rot * 30);
		}
		if(tick==getNeededTicks())
		{
			obj.getObject("leftarm").addRotation(0, 0, 0.00001F);
		}
		
		float f0 = uptoSoft(-180, 0, 15, tick);
		float f1 = -180 + uptoSoft(60, 16, 31, tick);
		float f2 = -120 - uptoSoft(60, 47, 31, tick);
		float f3 = -180 + uptoSoft(60, 78, 31, tick);
		float f4 = -120 - uptoSoft(60, 109, 31, tick);
		float f5 = -180.00001F + uptoSoft(180, 141, 15, tick);
		
		obj.getObject("rightarm").addRotation(0, 0, f0);
		if(tick>15)
			obj.getObject("rightarm").addRotation(0, 0, f1);
		if(tick>46)
			obj.getObject("rightarm").addRotation(0, 0, f2);
		if(tick>77)
			obj.getObject("rightarm").addRotation(0, 0, f3);
		if(tick>108)
			obj.getObject("rightarm").addRotation(0, 0, f4);
		if(tick>140)
			obj.getObject("rightarm").addRotation(0, 0, f5);
		
	}

}
