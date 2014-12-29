package dragon.Render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.opengl.GL11;

public class ObjectRenderer 
{
	
	protected List<ObjectTile> obj = new ArrayList<ObjectTile>(); 
	//protected ObjectMaterial mat;
	public HashMap<String,ObjectMaterial> materials = new HashMap<String,ObjectMaterial>();
	
	public void renderWithRotAt(double x,double y, double z, float j, float k, float l)
	{
		GL11.glPushMatrix();		
    	GL11.glTranslated(x, y, z);
    	GL11.glRotatef(j, 1, 0, 0);
    	GL11.glRotatef(k, 0, 1, 0);
    	GL11.glRotatef(l, 0, 0, 1);
    	render();
    	GL11.glPopMatrix();
	}
	public void render()
	{
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		//GL11.glPushMatrix();		
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
    	for(int n = 0;n<obj.size();n++)
    	{
    		ObjectTile o = obj.get(n);
    		o.render();
    	}	
		GL11.glEnable(GL11.GL_CULL_FACE);
	}
	public void addObject(String name,List<double[]> vPoint,List<int[]> vData, HashMap<Integer, String> mat, double[] ofset)
	{
		obj.add(new ObjectTile(this, name, vPoint, vData, mat,ofset));
	}
	
	public ObjectTile getObject(String n)
	{
		for(ObjectTile t : getAllObj())
		{
			if(t.name.equals(n))
			{
				return t;
			}
		}
		return null;
	}
	
	public List<ObjectTile> getAllObj()
	{
		List<ObjectTile> l = new ArrayList<ObjectTile>(obj);
		for(ObjectTile t : obj)
		{
			l.addAll(addChilds(t));
		}
		return l;
	}
	
	private List<ObjectTile> addChilds(ObjectTile obj)
	{
		List<ObjectTile> l = new ArrayList<ObjectTile>(obj.childs);
		for(ObjectTile t : obj.childs)
		{
			l.addAll(addChilds(t));
		}
		return l;
	}
	
	public boolean containsObj(String s)
	{
		return getObject(s)!=null;
	}
	
	public void removeObj(ObjectTile obj)
	{
		this.obj.remove(obj);
		System.out.println("Removed " + obj.name);
	}
	
	
}

