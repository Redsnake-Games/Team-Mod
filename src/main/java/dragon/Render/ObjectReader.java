package dragon.Render;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ObjectReader 
{
	private static HashMap<URL,ObjectRenderer> modal = new HashMap<URL,ObjectRenderer>();
	private static HashMap<URL, HashMap<String,ObjectMaterial>> materialLibs = new HashMap<URL, HashMap<String,ObjectMaterial>>();
	
	public static ObjectRenderer readObj(URL url) throws IOException
	{
		if(modal.containsKey(url))
			return modal.get(url);
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		ObjectRenderer robj=new ObjectRenderer();
		
		//ObjectTile t;
		String m = "";
		String n = "";
		int texX=0,texY=0;
		List<double[]> vp = null;
		List<int[]> vd = null;
		HashMap<Integer, int[]> uv = null;
		HashMap<Integer, String> matmap = new HashMap<Integer, String>();
		double[] ofset = null;
		int ll = 0;
		
		while(in.ready())
		{
			String[] s = in.readLine().split(" ");
			
			if(s[0].equals("mtllib"))
			{
				System.out.println(url);
				String[] s1 = url.toString().split("/");
				System.out.println(s1[s1.length-1]);
				URL url1 = new URL(url.toString().replace(s1[s1.length-1], s[1]));
				System.out.println(url1);
				robj.materials = readMatirial(robj,url1);
			}
			if(s[0].equals("texsize"))
			{
				texX = Integer.valueOf(s[1]);
				texY = Integer.valueOf(s[2]);
			}
			else if(s[0].equals("o") && robj!=null)
			{
				if(vp!=null&vd!=null)
				{
					robj.addObject(n, vp, vd, matmap, ofset);
					if(uv.size()>0)
					{
						robj.getObject(n).addMapping(texX, texY, uv);
					}
						
					ll += vp.size();
				}
				vp = new ArrayList<double[]>();
				vd = new ArrayList<int[]>();
				uv = new HashMap<Integer, int[]>();
				matmap = new HashMap<Integer, String>();
				ofset = null;
				n=s[1];
			}
			else if(s[0].equals("v") && robj!=null)
			{
				double[] d = new double[s.length-1];
				for(int i=1;i<s.length;i++)
				{
					d[i-1] = Double.valueOf(s[i]);
				}
				vp.add(d);
			}
			else if(s[0].equals("f") && robj!=null)
			{
				int[] d = new int[s.length-1];
				for(int i=1;i<s.length;i++)
				{
					if(!s[i].contains("/"))
					d[i-1] = Integer.valueOf(s[i]);
					else
						d[i-1] = Integer.valueOf(s[i].split("/")[0]);
					
					d[i-1] -= ll;
				}
				matmap.put(vd.size(), m);
				System.out.println(vd.size()+";"+m);
				vd.add(d);
			}
			else if(s[0].equals("u") && robj!=null)
			{
				int[] d = new int[s.length-1];
				for(int i=1;i<s.length;i++)
				{
					d[i-1] = Integer.valueOf(s[i]);		
					if(i==1)
						d[i-1] -= ll;
				}
				uv.put(d[0], Arrays.copyOfRange(d, 1, d.length));
				
			}
			else if(s[0].equals("usemtl") && robj!=null)
			{
				m= (s.length>1?s[1]:"");
			}
			else if(s[0].equals("r") && robj!=null)
			{
				ofset = new double[s.length-1];
				for(int i=1;i<s.length;i++)
				{
					ofset[i-1] = Double.valueOf(s[i]);
				}
			}
		}
		if(vp!=null&vd!=null)
		{
			robj.addObject(n, vp, vd, matmap, ofset);
			if(uv.size()>0)
			{
				robj.getObject(n).addMapping(texX, texY, uv);
			}
		}
		vp.clear();
		vd.clear();
		in.close();
		
		if(!modal.containsKey(url))
			modal.put(url, robj);
		
		setParents(robj);
		setRotations(robj);
		return robj;
	}
	
	public static HashMap<String,ObjectMaterial> readMatirial(ObjectRenderer gh,URL url) throws IOException
	{
		if(materialLibs.containsKey(url))
			return materialLibs.get(url);
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		//ObjectMaterial mat = new ObjectMaterial();
		HashMap<String,ObjectMaterial> mats = new HashMap<String,ObjectMaterial>();
		
		double[] d = new double[4];
		ObjectMaterial m = null;
		while(in.ready())
		{
			String[] s = in.readLine().split(" ");
			
			if(s[0].equals("newmtl"))
			{
				if(m!=null)
					mats.put(m.name, m.copie());
				
				m = new ObjectMaterial(s.length<2? "" : s[1]);
			}
			else if(s[0].equals("Kd"))
			{			
				for(int i=1;i<s.length;i++)
				{
					d[i-1] = Double.valueOf(s[i]);
				}			
			}
			else if(s[0].equals("d"))
			{
				d[3] = Double.valueOf(s[1]);
				m.rgba = d;
			}
		}
		in.close();
		
		if(m!=null)
			mats.put(m.name, m.copie());
		if(!materialLibs.containsKey(url))
			materialLibs.put(url, mats);
		return mats;
	}
	
	public static String[] getObjectToRotate(String s1)
	{
		s1 = s1.replaceFirst("@", "");
		if(s1.contains("@"))
		{
			String[] s2 = s1.split("@");
			return s2;
		}
		return new String[]{s1};
	}
	
	public static void setRotations(ObjectRenderer obj)
	{
		List<ObjectTile> remove = new ArrayList<ObjectTile>();
		for(ObjectTile t : obj.obj)
		{
			if(t.name.startsWith("@"))
			{
				for(String s : getObjectToRotate(t.name))
				{
					ObjectTile tile = obj.getObject(s);
					if(tile!=null)
					{
						tile.setOfset(ObjectTile.getPoint(t));
						System.out.println("Found Ofset for "+tile.name);
					}
				}
				remove.add(t);
			}
		}
		obj.obj.removeAll(remove);	
	}
	
	public static String[] getRealName(ObjectTile obj)
	{
		if(obj.name.contains("#"))
		{
			String realname = obj.name.substring(0, obj.name.indexOf("#"));			
			String parent = obj.name.substring(obj.name.lastIndexOf("#")+1, obj.name.length());
			
			return new String[]{realname,parent};
		}
		return null;
	}
	
	public static void setParents(ObjectRenderer obj)
	{
		List<String[]> childs = new ArrayList<String[]>();
		
		for(ObjectTile t : obj.obj)
		{
			String[] s = getRealName(t);
			if(s!=null)
			{
				t.name = s[0];
				childs.add(s);
			}
		}
		
		for(String[] s : childs)
		{
			ObjectTile child = obj.getObject(s[0]);
			ObjectTile parent = obj.getObject(s[1]);
			
			parent.addChild(child);
			
			obj.removeObj(child);
			
		}
		
	}
}
