package dragon.team;

import java.util.Collection;

import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.tileentity.TileEntityChestRenderer;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderTeamChest extends TileEntityChestRenderer
{
	
	private final ModelChest model = new ModelChest();
    //private static final ResourceLocation field_147505_d = new ResourceLocation("team","textures/model/normal_double.png");
    //private static final ResourceLocation field_147504_g = new ResourceLocation("team","textures/model/normal.png");
    private static final ResourceLocation tex = new ResourceLocation("team","textures/entity/chest/TeamChest.png");
    private static final ResourceLocation overlay = new ResourceLocation("team","textures/entity/chest/TeamChest_overlay.png");
    //private final ModelChest field_147510_h = new ModelChest();
    //private final ModelChest field_147511_i = new ModelLargeChest();
    //private boolean field_147509_j;
	
    
	public void renderTileEntity(TileEntityTeamChest var1, double x, double y, double z, float var8) 
	{
		//var1.updateContainingBlockInfo();
		int meta = var1.getBlockMetadata();
		
		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glTranslatef((float)x, (float)y + 1.0F, (float)z + 1.0F);
		GL11.glScalef(1.0F, -1.0F, -1.0F);
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		short short1 = 0;
		
		if (meta == 2)
		{
			short1 = 180;
		}

		if (meta == 3)
		{
			short1 = 0;
		}
		
		if (meta == 4)
		{
			short1 = 90;
		}
		
		if (meta == 5)
		{
			short1 = -90;
		}
		/*
		if (meta == 2 && var1.adjacentChestXPos != null)
		{
			GL11.glTranslatef(1.0F, 0.0F, 0.0F);
		}
		
		if (meta == 5 && var1.adjacentChestZPos != null)
		{
			GL11.glTranslatef(0.0F, 0.0F, -1.0F);
		}
		*/
		GL11.glRotatef(short1, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		
		float f1 = var1.prevLidAngle + (var1.lidAngle - var1.prevLidAngle) * var8;
		float f2;

		f1 = 1.0F - f1;
		f1 = 1.0F - f1 * f1 * f1;
         
		//if(var1.prefix!=null&&var1.prefix.length()>0)
		{
			model.chestLid.rotateAngleX = -(f1 * (float)Math.PI / 2.0F);
			
			if(var1.prefix!=null&&var1.prefix.length()>0)
			{
				GL11.glPushMatrix();
				bindTexture(overlay);
				int col = getColor(var1.prefix);
				float j = col >> 16 & 255;
				float k = col >> 8 & 255;
				float l = col & 255;
				GL11.glColor3f(j/255F, k/255F, l/255F);
				double d = 0.01;
				GL11.glTranslated(d/-2D, d/-2D, d/-2D);
				GL11.glScaled(1+d, 1+d, 1+d);	 
				model.renderAll();
				
				GL11.glPopMatrix();
			}
			GL11.glColor3f(1,1,1);
			bindTexture(tex);	
			model.renderAll();
		}
		
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}
	
	@Override
	public void func_180538_a(TileEntityChest var1, double var2, double var4, double var6, float var8, int var) 
	{
		//super.renderTileEntityAt(var1, var2, var4, var6, var8);
		
		if(var1!=null)
			renderTileEntity((TileEntityTeamChest) var1, var2, var4, var6, var8);
	}
	
	private int getColor(String s)
	{
		Collection<String> c = EnumChatFormatting.getValidValues(true, false);
		int i = 0;
		for(String e : c)
		{
			if(s.equals(EnumChatFormatting.getValueByName(e).toString()))
			{
				return ItemLayerSword.colorCode[i];
			}
			
			i++;
		}
		return 0xffffff;
	}
}
