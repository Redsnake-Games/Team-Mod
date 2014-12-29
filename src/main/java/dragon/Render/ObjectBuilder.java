package dragon.Render;

import java.util.ArrayList;
import java.util.List;

public class ObjectBuilder 
{
	public static List[] addQube(double x1, double y1, double z1, double x2, double y2, double z2)
	{
		List<double[]> vPoint = new ArrayList();
		vPoint.add(new double[]{ x2, y1, z1});
		vPoint.add(new double[]{ x2, y1, z2});
		vPoint.add(new double[]{ x1, y1, z2});
		vPoint.add(new double[]{ x1, y1, z1});
		vPoint.add(new double[]{ x2, y2, z1});
		vPoint.add(new double[]{ x2, y2, z2});
		vPoint.add(new double[]{ x1, y2, z2});
		vPoint.add(new double[]{ x1, y2, z1});
		
		List<int[]> vData = new ArrayList();
		vData.add( new int[]{ 1, 2, 3, 4});
		vData.add( new int[]{ 5, 8, 7, 6});
		vData.add( new int[]{ 1, 5, 6, 2});
		vData.add( new int[]{ 2, 6, 7, 3});
		vData.add( new int[]{ 3, 7, 8, 4});
		vData.add( new int[]{ 5, 1, 4, 8});
		
		return new List[]{vPoint,vData};
	}
}
