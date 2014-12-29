package dragon.Render;

public class AnimationWalking extends AnimationBase
{

	@Override
	boolean isObjectValid(ObjectRenderer obj) 
	{
		return contains(obj, "head", "body", "leftarm", "rightarm", "leftleg", "rightleg");			
	}

	@Override
	int getNeededTicks() 
	{
		return 62;
	}

	@Override
	void render(ObjectRenderer obj, int tick) 
	{
		float rot = (float) Math.sin(tick * 0.1);
		obj.getObject("rightarm").addRotation( rot * 50, 0, 0);
		obj.getObject("leftarm").addRotation( rot * -50, 0, 0);
		
		obj.getObject("rightleg").addRotation( rot * -30, 0, 0);
		obj.getObject("leftleg").addRotation( rot * 30, 0, 0);
		
	}

}
