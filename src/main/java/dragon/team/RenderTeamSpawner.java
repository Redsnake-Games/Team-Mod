package dragon.team;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

public class RenderTeamSpawner extends TileEntitySpecialRenderer 
{
	
	public void doRender(TileEntityTeamSpawner var1, double var2, double var4,double var6, float var8) 
	{
		
		ItemStack it = new ItemStack(MainTeam.teamWappen);
		it.setTagCompound(new NBTTagCompound());
		ScorePlayerTeam team = var1.getWorld().getScoreboard().getTeam(var1.getTeam());
		if(team!=null)
			it.getTagCompound().setString("prefix", team.getColorPrefix());
		EntityItem item = new EntityItem(var1.getWorld(), 0, 0, 0, it);
		item.hoverStart = (float) (((double)var1.spawnTime)/(double)var1.maxtime * 180F);
		
		
		GL11.glTranslated(var2+0.5, var4+0.0, var6+0.5);
		//GL11.glRotatef(var1.time/var1.maxtime * 360F, 0, 1, 0);
		//GL11.glTranslated(-0.25, -0.25, -0.25);
		double d = var1.engine/64D * 1.3  + 0.2;
		GL11.glScaled(d, d, d);
		
		//RenderItem.renderInFrame = true;
		Minecraft.getMinecraft().getRenderManager().renderEntityWithPosYaw(item, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
		//RenderItem.renderInFrame = false;
		
	}
	
	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4,double var6, float var8, int var) 
	{
		GL11.glPushMatrix();
		doRender((TileEntityTeamSpawner) var1, var2, var4, var6, var8);
		GL11.glPopMatrix();
	}

}
