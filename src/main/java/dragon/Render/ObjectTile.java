package dragon.Render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.opengl.GL11;

public class ObjectTile
{
	String name;
	HashMap<Integer, String> matmap;
	List<double[]> vertexPoint;
	List<int[]> vertexData;
	float texXSize;
	float texYSize;
	HashMap<Integer, int[]> uvMapping;
	float xRot,yRot,zRot;
	double xPos,yPos,zPos;
	double[] d = new double[]{1.0,1.0,1.0,1.0};
	double[] ofset = null;
	ObjectRenderer source;
	List<ObjectTile> childs = new ArrayList<ObjectTile>();
	
	
	public ObjectTile(ObjectRenderer parent, String name,List<double[]> vPoint,List<int[]> vData, HashMap<Integer, String> mat, double[] ofset)
	{
		this.name=name;
		vertexPoint = new ArrayList<double[]>(vPoint);
		vertexData = new ArrayList<int[]>(vData);
		matmap = mat;
		this.source = parent;
		this.ofset = ofset;
		
	}
	
	public void addMapping(int x, int y, HashMap<Integer,int[]> uvMap)
	{
		uvMapping = uvMap;
		texXSize = x;
		texYSize = y;
		System.out.println("x"+x);
		System.out.println("y"+y);
	}
	
	public void render()
	{			
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);		
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		/*if(parent.materials.get(material).texture!=null)
		{
			
		}*/
		{
			if(ofset==null)
			{
				ofset = getPoint(this); 
			}
    		GL11.glTranslated(xPos, yPos, zPos);	
    		GL11.glTranslated(ofset[0], ofset[1], ofset[2]);    		
			GL11.glRotatef(xRot, 1, 0, 0);
    		GL11.glRotatef(yRot, 0, 1, 0);
    		GL11.glRotatef(zRot, 0, 0, 1);
    		GL11.glTranslated(-ofset[0], -ofset[1], -ofset[2]);    		
    		
    		for(int i=0;i<vertexData.size();i++)
    		{
    			float c = (float)( i/(double)vertexData.size()*0.5);
    			
    			String material = matmap.get(i);
    			
    			if(material!=null && material.length()>0)
    				d = source.materials.get(material).rgba;		
    			
    			GL11.glColor4d(d[0]+c, d[1]+c, d[2]+c, d[3]);
    			
    			int[] data = vertexData.get(i);
    			int cap = data.length==2?GL11.GL_LINES:(data.length==3?GL11.GL_TRIANGLES:(data.length==4?GL11.GL_QUADS:GL11.GL_POLYGON));
    			GL11.glBegin(cap);
    			for(int j=0;j<data.length;j++)
    			{
    				if(uvMapping!=null)
    				{
    					int[] uv = uvMapping.get(data[j]);
    					GL11.glTexCoord2f(uv[0]/texXSize, uv[1]/texYSize);
    				}
    				double[] vertex = vertexPoint.get(data[j]-1);
    				GL11.glVertex3d(vertex[0], vertex[1], vertex[2]);
    			}
    			GL11.glEnd();
    		}
    		for(ObjectTile t: childs)
    		{
    			t.render();
    		}
    	}
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}
	
	public void setRotation(float x, float y, float z)
	{
		xRot = x;
		yRot = y;
		zRot = z;
	}
	
	public void addRotation(float x, float y, float z)
	{
		xRot = x!=0?x:xRot;
		yRot = y!=0?y:yRot;
		zRot = z!=0?z:zRot;
	}
	
	public void setPositon(double x, double y, double z)
	{
		xPos = x;
		yPos = y;
		zPos = z;
	}
		
	public void setOfset(double[] d)
	{
		ofset = d;
	}
	
	public void addChild(ObjectTile t)
	{
		childs.add(t);
		System.out.println("Added "+ t.name + " to "+this.name);
	}

	public static double[] getPoint(ObjectTile obj)
	{
		double minX, minY, minZ;
		double maxX, maxY, maxZ;
		double[] d1 = obj.vertexPoint.get(0);
		minX=maxX=d1[0];
		minY=maxY=d1[1];
		minZ=maxZ=d1[2];
		
		for(double[] d : obj.vertexPoint)
		{
			minX = Math.min(d[0], minX);
			minY = Math.min(d[1], minY);
			minZ = Math.min(d[2], minZ);
			
			maxX = Math.max(d[0], maxX);
			maxY = Math.max(d[1], maxY);
			maxZ = Math.max(d[2], maxZ);
		}
		
		double x = minX+((maxX-minX)/2);
		double y = minY+((maxY-minY)/2);
		double z = minZ+((maxZ-minZ)/2);
		
		return new double[]{x,y,z};	
	}

}
