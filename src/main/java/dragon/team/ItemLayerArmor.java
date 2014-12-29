package dragon.team;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
//import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemLayerArmor extends ItemArmor 
{

//	private IIcon overlay;
	
	public ItemLayerArmor(ArmorMaterial material, int type)
	{
		super(material, 0, type);
		setCreativeTab(MainTeam.tabTeam);
		
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack it) 
	{
		if(it.hasTagCompound()&&it.getTagCompound().hasKey("team"))
		{
			 return super.getItemStackDisplayName(it) + " " +getTeamName(it);
		}
		return super.getItemStackDisplayName(it);
	}
	
	private String getTeamName(ItemStack it)
	{
		String prefix = ""; 
		if(it.getTagCompound().hasKey("prefix"))
		{
			prefix = it.getTagCompound().getString("prefix");
		}
		String suffix = ""; 
		if(it.getTagCompound().hasKey("suffix"))
		{
			suffix = it.getTagCompound().getString("suffix");
		}
		return prefix + it.getTagCompound().getString("team") + suffix;
	}
	
	
	@Override
	public int getColor(ItemStack stack) 
	{
		if(stack.hasTagCompound()&&stack.getTagCompound().hasKey("prefix"))
		{
			Collection<String> c = EnumChatFormatting.getValidValues(true, false);
			int i = 0;
			String s = stack.getTagCompound().getString("prefix");
			for(String e : c)
			{
				if(s.equals(EnumChatFormatting.getValueByName(e).toString()))
				{
					setColor(stack,ItemLayerSword.colorCode[i]);
				}
				
				i++;
			}
		}
		else
		{
			removeColor(stack);
		}
		return super.getColor(stack);
	}
	
//	@Override
//	public void registerIcons(IIconRegister par1IconRegister) 
//	{
//		overlay = par1IconRegister.registerIcon(getIconString() + "_overlay");
//		super.registerIcons(par1IconRegister);
//		
//	}
//	
//	@Override
//	public IIcon getIconFromDamageForRenderPass(int par1, int par2)
//    {
//        return par2 == 1 ? this.overlay : super.getIconFromDamageForRenderPass(par1, par2);
//    }
	
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List p_150895_3_) 
	{
		ItemStack it =  new ItemStack(p_150895_1_, 1, 0);
		it.setTagCompound(new NBTTagCompound());
		it.getTagCompound().setBoolean("auto", true);
		p_150895_3_.add(it);
		
		Scoreboard score = Minecraft.getMinecraft().theWorld.getScoreboard();
		Collection<ScorePlayerTeam> c = score.getTeams();
		for(ScorePlayerTeam t : c)
		{
			it = new ItemStack(p_150895_1_, 1, 0);
			it.setTagCompound(new NBTTagCompound());
			it.getTagCompound().setString("team", t.getRegisteredName());
			if(t.getColorPrefix().length()>0)
			{
				it.getTagCompound().setString("prefix", t.getColorPrefix());
			}
			if(t.getColorSuffix().length()>0)
			{
				it.getTagCompound().setString("suffix", t.getColorSuffix());
			}
				
			p_150895_3_.add(it);
		}	
	}
	@Override
	public void onUpdate(ItemStack it, World w, Entity ent, int par4, boolean par5) 
	{
		if(it.hasTagCompound()&&it.getTagCompound().hasKey("auto"))
		{
			if(it.getTagCompound().getBoolean("auto"))
			{
				if(ent instanceof EntityPlayer) 
				{
					if(((EntityPlayer)ent).getTeam()!=null)
					{
						ScorePlayerTeam t = (ScorePlayerTeam) ((EntityPlayer)ent).getTeam();
						it.getTagCompound().setString("team", t.getRegisteredName());
						if(t.getColorPrefix().length()>0)
						{
							it.getTagCompound().setString("prefix", t.getColorPrefix());
						}
						if(t.getColorSuffix().length()>0)
						{
							it.getTagCompound().setString("suffix", t.getColorSuffix());
						}
						//it.getTagCompound().setBoolean("auto", false);
					}
					else				
					{				
						if(it.getTagCompound().hasKey("team"))
							it.getTagCompound().removeTag("team");
						if(it.getTagCompound().hasKey("prefix"))
							it.getTagCompound().removeTag("prefix");
						if(it.getTagCompound().hasKey("suffix"))
							it.getTagCompound().removeTag("suffix");
					}
				}
			}
		}
	}
	
	@Override
	public void addInformation(ItemStack it, EntityPlayer par2EntityPlayer, List par3List, boolean par4) 
	{
		if(it.hasTagCompound()&&it.getTagCompound().hasKey("auto"))
		{
			if(it.getTagCompound().getBoolean("auto"))
			{
				par3List.add("automatic Teamsearch enabled");
			}
		}
		
		super.addInformation(it, par2EntityPlayer, par3List, par4);
	}
	
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) 
	{
		super.onArmorTick(world, player, itemStack);
		onUpdate(itemStack, world, player, 0, false);
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) 
	{
	    ItemArmor armor = (ItemArmor)stack.getItem();
	    if(type!=null && type.equals("overlay"))
	    {
	    	switch(armor.armorType)
		    {
		    case 0: return "team:textures/models/armor/Team_layer_1_overlay.png";
		    case 1: return "team:textures/models/armor/Team_layer_1_overlay.png";
		    case 2: return "team:textures/models/armor/Team_layer_2_overlay.png";                                        
		    case 3: return "team:textures/models/armor/Team_layer_1_overlay.png";
		    }
	    }
	    switch(armor.armorType)
	    {
	    case 0: return "team:textures/models/armor/Team_layer_1.png";
	    case 1: return "team:textures/models/armor/Team_layer_1.png";
	    case 2: return "team:textures/models/armor/Team_layer_2.png";                                        
	    case 3: return "team:textures/models/armor/Team_layer_1.png";
	    }
		return "Team";
	}
}
