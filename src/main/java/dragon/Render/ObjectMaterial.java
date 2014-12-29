package dragon.Render;


public class ObjectMaterial 
{
	//public HashMap<String,double[]> materials = new HashMap<>();
	
	public String name;
	public double[] rgba;
	public String texture = null;
	
	public ObjectMaterial(String par1)
	{
		name = par1;  
	}
	
	public ObjectMaterial copie()
	{
		ObjectMaterial m = new ObjectMaterial(name);
		m.rgba = rgba.clone();
		m.texture = texture;
		return m;
	}
	
	
}
