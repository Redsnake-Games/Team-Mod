package dragon.team;

import java.util.Collection;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class RenderTeamFlag extends TileEntitySpecialRenderer 
{
	ResourceLocation tex = new ResourceLocation("team", "textures/entity/flag/teamFlag.png");
	ResourceLocation over = new ResourceLocation("team", "textures/entity/flag/teamFlag_overlay.png");
	ResourceLocation pro = new ResourceLocation("team", "textures/entity/flag/progress.png");
	ResourceLocation proo = new ResourceLocation("team", "textures/entity/flag/progress_overlay.png");
	
	public void renderTile(TileEntityTeamFlag var1, double var2, double var4, double var6, float var8) 
	{
		GL11.glPushMatrix();
		
		
		bindTexture(tex);
		ScorePlayerTeam team = var1.getTeam();
		double[] col = new double[]{1,1,1,1};
		if(team!=null)
		{
			int index = getColor(team.getColorPrefix());
			int r = (index >> 16 ) % 256;
			int g = (index >> 8 ) % 256;
			int b = (index  ) % 256;
			col[0] = r/256D;
			col[1] = g/256D;
			col[2] = b/256D;
		}
		MainTeam.obj.materials.get("Material").rgba = col;
		MainTeam.obj.renderWithRotAt(var2+0.5, var4, var6+0.5, 0, var1.rotation, 0);
		
		//GL11.glDisable(GL11.GL_DEPTH_TEST);
		bindTexture(over);
		GL11.glScaled(0.99, 0.99, 0.99);
		MainTeam.obj.materials.get("Material").rgba = new double[]{1,1,1,1};
		MainTeam.obj.renderWithRotAt(var2+0.5, var4, var6+0.5, 0, var1.rotation, 0);
		//GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		GL11.glTranslated(var2+0.5, var4+2.5, var6+0.5);
		GL11.glRotatef(180.0F - Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-Minecraft.getMinecraft().getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
	
		ScorePlayerTeam team2 = Minecraft.getMinecraft().theWorld.getScoreboard().getTeam(var1.getBestTeam());
		//double[] col2 = new double[]{1,1,1,1};
		if(team2!=null)
		{
			int index = getColor(team2.getColorPrefix());
			int r = (index >> 16 ) % 256;
			int g = (index >> 8 ) % 256;
			int b = (index  ) % 256;
			col[0] = r/256D;
			col[1] = g/256D;
			col[2] = b/256D;
		}
		GL11.glColor3d(1,1,1);
		double s = 0.375;
		bindTexture(pro);
		GL11.glBegin(GL11.GL_QUADS);		
		GL11.glTexCoord2f(0,1);
		GL11.glVertex3d(-s, s, -0.005);
		GL11.glTexCoord2f(0,0);
		GL11.glVertex3d(-s, -s, -0.005);
		GL11.glTexCoord2f(1,0);
		GL11.glVertex3d(s, -s, -0.005);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex3d(s, s, -0.005);
		GL11.glEnd();
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		FontRenderer fontrenderer = this.getFontRenderer();
		float f=0;
		
		GL11.glColor4d(col[0], col[1], col[2], col[3]);
		if(team!=null)
		{	
			if(var1.fighter.containsKey(team.getRegisteredName()))
			{
				f = var1.fighter.get(team.getRegisteredName());
			}
			if(f>100)f=100;
			for(int i=0;i<f;i++)
			{
				GL11.glColor4d(col[0], col[1], col[2], col[3]);
				renderCirkelTile(i,0.35F);
			}
		}
		else if(team2!=null)
		{
			f = var1.fighter.get(team2.getRegisteredName());
			if(f>100)f=100;
			for(int i=0;i<(int)f;i++)
			{
				GL11.glColor4d(col[0], col[1], col[2], col[3]);
				renderCirkelTile(i,0.35F);
			}
			
		}
		GL11.glColor3d(0.4,0.4,0.4);
		String s1 = ((int)(f*10)/10F)+"%";
		float f3 = 0.016666668F * 0.66666666F;
		
		GL11.glBegin(GL11.GL_QUADS);		
		GL11.glVertex3d((-fontrenderer.getStringWidth(s1) / 2-1) * f3, -9* f3, 0.0075);
		GL11.glVertex3d((-fontrenderer.getStringWidth(s1) / 2-1)* f3, -19* f3, 0.0075);
		GL11.glVertex3d(fontrenderer.getStringWidth(s1) / 2* f3, -19* f3, 0.0075);
		GL11.glVertex3d(fontrenderer.getStringWidth(s1) / 2* f3, -9* f3, 0.0075);
		GL11.glEnd();
		
		GL11.glPushMatrix();
		{	
			GL11.glTranslated(0, 0, 0.01);
			
	        GL11.glScalef(f3, -f3, f3);
	        GL11.glNormal3f(0.0F, 0.0F, -1.0F * f3);
	        	     
	        GL11.glDepthMask(false);

			GL11.glEnable(GL11.GL_TEXTURE_2D);
			
			fontrenderer.drawString(s1, -fontrenderer.getStringWidth(s1) / 2, 10, 0);
			GL11.glDepthMask(true);
		}
		GL11.glPopMatrix();
		
		
		GL11.glColor3d(1,1,1);
		bindTexture(proo);
		GL11.glBegin(GL11.GL_QUADS);		
		GL11.glTexCoord2f(0,1);
		GL11.glVertex3d(-s, s, 0.005);
		GL11.glTexCoord2f(0,0);
		GL11.glVertex3d(-s, -s, 0.005);
		GL11.glTexCoord2f(1,0);
		GL11.glVertex3d(s, -s, 0.005);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex3d(s, s, 0.005);
		GL11.glEnd();
		
		GL11.glPopMatrix();
	}
	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8, int var) 
	{
		renderTile((TileEntityTeamFlag) var1, var2, var4, var6, var8);
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
	
	private void renderCirkelTile(int i,float s)
	{
		double x1 = Math.sin(Math.PI*(i/50D))*s;
		double x2 = Math.sin(Math.PI*((i+1)/50D))*s;
		double y1 = Math.cos(Math.PI*(i/50D))*s;
		double y2 = Math.cos(Math.PI*((i+1)/50D))*s;
		
		GL11.glBegin(GL11.GL_TRIANGLES);		
		GL11.glVertex3d(x1, y1, 0);
		GL11.glVertex3d(0, 0, 0);
		GL11.glVertex3d(x2, y2, 0);
		GL11.glEnd();
	}
	
}
