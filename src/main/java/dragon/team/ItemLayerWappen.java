package dragon.team;

import java.util.Collection;
import java.util.List;

import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
//import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemLayerWappen extends Item 
{
	
//	private IIcon icon_overlay;
	
	public ItemLayerWappen() 
	{
		super();
		setCreativeTab(MainTeam.tabTeam);
	}
	
//	@Override
//	public boolean requiresMultipleRenderPasses() 
//	{
//		return true;
//	}
	
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
	
//	@Override
//	public IIcon getIcon(ItemStack stack, int pass) 
//	{
//		if(pass==1 && stack.hasTagCompound())
//		{
//			if(stack.getTagCompound().hasKey("prefix"))
//			{
//				return icon_overlay;
//			}
//		}
//		return super.getIcon(stack, pass);
//	}
	
	@Override
	public int getColorFromItemStack(ItemStack stack, int par2) 
	{
		if(par2==1&&stack.hasTagCompound()&&stack.getTagCompound().hasKey("prefix"))
		{
			Collection<String> c = EnumChatFormatting.getValidValues(true, false);
			int i = 0;
			String s = stack.getTagCompound().getString("prefix");
			for(String e : c)
			{
				if(s.equals(EnumChatFormatting.getValueByName(e).toString()))
				{
					return ItemLayerSword.colorCode[i];
				}
				
				i++;
			}
		}
		return super.getColorFromItemStack(stack, par2);
	}
	
//	@Override
//	public void registerIcons(IIconRegister r) 
//	{		
//		icon_overlay = r.registerIcon(this.getIconString() +"_overlay");
//		super.registerIcons(r);
//	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List l) 
	{
		ItemStack it =  new ItemStack(item, 1, 0);
		it.setTagCompound(new NBTTagCompound());
		it.getTagCompound().setBoolean("auto", true);
		l.add(it);
		
		Scoreboard score = Minecraft.getMinecraft().theWorld.getScoreboard();
		Collection<ScorePlayerTeam> c = score.getTeams();
		for(ScorePlayerTeam t : c)
		{
			it = new ItemStack(item, 1, 0);
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
				
			l.add(it);
		}	
	}
	
	@Override
	public void onUpdate(ItemStack it, World w, Entity ent, int par4, boolean par5) 
	{
		if(it.hasTagCompound()&&it.getTagCompound().hasKey("auto"))
		{
			if(it.getTagCompound().getBoolean("auto"))
			{
				if(ent instanceof EntityPlayer && ((EntityPlayer)ent).getTeam()!=null)
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
	public boolean hitEntity(ItemStack it,	EntityLivingBase hit, EntityLivingBase holder) 
	{		
		if(hit!=null&&hit.getTeam()!=null)
		{
			ScorePlayerTeam team = (ScorePlayerTeam) hit.getTeam();
			hit.attackEntityFrom(DamageSource.causeMobDamage(holder), team.getMembershipCollection().size());
		}
		return super.hitEntity(it, hit,holder);
	}
}
