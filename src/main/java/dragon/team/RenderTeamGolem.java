package dragon.team;

import java.util.Collection;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class RenderTeamGolem extends RenderLiving 
{
	private static final ResourceLocation ironGolemTextures = new ResourceLocation("team","textures/entity/team_golem.png");
	private static final ResourceLocation ironGolemTextures_overlay = new ResourceLocation("team","textures/entity/team_golem_overlay.png");
	private final ModelTeamGolem ironGolemModel;
	private boolean overlay = false;    
	
	public RenderTeamGolem()
	{
		super(Minecraft.getMinecraft().getRenderManager(),new ModelTeamGolem(), 0.5F);
		this.ironGolemModel = (ModelTeamGolem)this.mainModel;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity var1) 
	{
		return overlay ? ironGolemTextures_overlay : ironGolemTextures;
	}
	
	public void doRender(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef((float)par2, (float)par4, (float)par6);
		overlay = false;
		float f = (float) (1.8D/2.9D);
		GL11.glScalef(f, f, f);
		GL11.glColor3f(1F, 1F, 1F);
		super.doRender(par1EntityLiving, 0, 0, 0, par8, par9);
		
		if(par1EntityLiving.getTeam()!=null)
		{
			int index = getColor(((ScorePlayerTeam) par1EntityLiving.getTeam()).getColorPrefix());
			int r = (index >> 16 ) % 256;
			int g = (index >> 8 ) % 256;
			int b = (index  ) % 256;
			GL11.glColor3f(r/256F, g/256F, b/256F);
			overlay = true;
			super.doRender(par1EntityLiving, 0, 0, 0, par8, par9);
		}
		GL11.glPopMatrix();
		GL11.glColor3f(1F, 1F, 1F);
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
	
	protected void rotateCorpse(EntityLivingBase par1EntityLivingBase, float par2, float par3, float par4)
	{
		this.rotateCorpse((EntityTeamGolem)par1EntityLivingBase, par2, par3, par4);
	}
	 
	protected void rotateCorpse(EntityTeamGolem par1EntityIronGolem, float par2, float par3, float par4)
	{
		super.rotateCorpse(par1EntityIronGolem, par2, par3, par4);
		
		if ((double)par1EntityIronGolem.limbSwingAmount >= 0.01D)
		{
			float f3 = 13.0F;
			float f4 = par1EntityIronGolem.limbSwing - par1EntityIronGolem.limbSwingAmount * (1.0F - par4) + 6.0F;
			float f5 = (Math.abs(f4 % f3 - f3 * 0.5F) - f3 * 0.25F) / (f3 * 0.25F);
			GL11.glRotatef(6.5F * f5, 0.0F, 0.0F, 1.0F);
		}
	}
}
