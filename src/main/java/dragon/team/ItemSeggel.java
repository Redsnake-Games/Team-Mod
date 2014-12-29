package dragon.team;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemSeggel extends Item 
{
	@Override
	public boolean itemInteractionForEntity(ItemStack it, EntityPlayer pl, EntityLivingBase entity) 
	{
		if(pl.isOnSameTeam(entity))
		{
			entity.heal(2F);
			it.stackSize--;
		}
		
		return super.itemInteractionForEntity(it, pl, entity);
	}
}
