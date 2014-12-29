package dragon.team;

import net.minecraft.client.model.ModelIronGolem;
import net.minecraft.entity.EntityLivingBase;

public class ModelTeamGolem extends ModelIronGolem 
{
	public void setLivingAnimations(EntityLivingBase par1EntityLivingBase, float par2, float par3, float par4)
	{
		EntityTeamGolem entityirongolem = (EntityTeamGolem )par1EntityLivingBase;
		int i = entityirongolem.getAttackTimer();

		if (i > 0)
		{
			this.ironGolemRightArm.rotateAngleX = -2.0F + 1.5F * this.func_78172_a((float)i - par4, 10.0F);
			this.ironGolemLeftArm.rotateAngleX = -2.0F + 1.5F * this.func_78172_a((float)i - par4, 10.0F);
		}
		else
		{
			this.ironGolemRightArm.rotateAngleX = (-0.2F + 1.5F * this.func_78172_a(par2, 13.0F)) * par3;
            this.ironGolemLeftArm.rotateAngleX = (-0.2F - 1.5F * this.func_78172_a(par2, 13.0F)) * par3;
		}
	}
	
	private float func_78172_a(float par1, float par2)
    {
        return (Math.abs(par1 % par2 - par2 * 0.5F) - par2 * 0.25F) / (par2 * 0.25F);
    }
}
