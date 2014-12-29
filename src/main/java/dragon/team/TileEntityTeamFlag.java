package dragon.team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.event.HoverEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;

import com.google.common.base.Predicate;

public class TileEntityTeamFlag extends TileEntity implements IUpdatePlayerListBox
{
	private String team  = "";
	String name = "";
	float rotation = 0;
	int time =0;
	int range = 10;
	
	HashMap<String,Float> fighter = new HashMap<String,Float>();
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) 
	{
		nbt.setString("team", team);
		nbt.setString("name", name);
		nbt.setFloat("rot", rotation);
		nbt.setInteger("range", range);
		NBTTagList list = new NBTTagList();
		for(String s : fighter.keySet())
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("name", s);
			tag.setFloat("value", fighter.get(s));
			list.appendTag(tag);
		}
		nbt.setTag("fighter", list);
		super.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{
		team = nbt.getString("team");
		name = nbt.getString("name");
		rotation = nbt.getFloat("rot");
		range = nbt.getInteger("range");
		NBTTagList list = nbt.getTagList("fighter", 10);
		for(int i=0;i<list.tagCount();i++)
		{
			NBTTagCompound tag = list.getCompoundTagAt(i);
			fighter.put(tag.getString("name"),tag.getFloat("value"));
		}
			
		
		super.readFromNBT(nbt);
	}
	
	@Override
	public Packet getDescriptionPacket() 
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		S35PacketUpdateTileEntity pkt = new S35PacketUpdateTileEntity(pos, getBlockMetadata(), nbt);
		return pkt;
	}
	
	public void reset()
	{
		team = "";
		fighter.clear();
	}
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) 
	{
		readFromNBT(pkt.getNbtCompound());
		super.onDataPacket(net, pkt);
	}
	
	@Override
	public void update() 
	{
		//if(!worldObj.isRemote)
		{
			int radius =10;
			AxisAlignedBB aabb = new AxisAlignedBB(new BlockPos(pos).add(0.5, 0.5, 0.5),new BlockPos(pos).add(0.5, 0.5, 0.5)).expand(radius, radius, radius);
			List<EntityPlayer> l = worldObj.func_175674_a(null, aabb, new Predicate<Entity>()//wgetEntitiesWithinAABBExcludingEntity
			{		
				@Override
				public boolean apply(Entity var1) 
				{
					return var1 instanceof EntityPlayer && (((EntityLivingBase)var1).getTeam()!=null || !MainTeam.ignoreTeamLessPlayer );
				}
			});
			List<Team> teams = new ArrayList<Team>();
			for(EntityPlayer pl : l)
			{
				if(!teams.contains(pl.getTeam()))
					teams.add(pl.getTeam());
			}
			for(EntityPlayer pl : l)
			{
				double power = 0.01D;
				double[] armorPower = new double[]{0.0075,0.011,0.009,0.0025};
				ItemStack[] it = pl.inventory.armorInventory;
				for(ItemStack is : it)
				{
					if(is!=null&&is.getItem() instanceof ItemLayerArmor)
					{
						power += armorPower[((ItemLayerArmor)is.getItem()).armorType];
					}
				}
				
				double dis = (10-MathHelper.sqrt_double(pl.getDistanceSqToCenter(pos))) * power;
				ScorePlayerTeam team = worldObj.getScoreboard().getTeam(this.team);
			
				
				for(String s : fighter.keySet())
				{
					if(pl.getTeam()==null||!pl.getTeam().getRegisteredName().equals(s))
					{
						float i = fighter.get(s);
						if(i>0)
							i -= dis;
						if(i<0)
						{
							if(s.equals(this.team))
							{
								setTeam("");	
								onColorChange();
							}
							i=0;
						}
						fighter.put(s, i);
					}
				}	
				if(team==null&&pl.getTeam()!=null)
				{
					float i=0;
					if(fighter.containsKey(pl.getTeam().getRegisteredName()))
					{
						i = fighter.get(pl.getTeam().getRegisteredName());
					}
					if(i<100)
						i+=dis*2;

					if(i>100)
					{	
						setTeam(pl.getTeam().getRegisteredName());
						try{
							pl.addStat(MainTeam.captureState, 1);
						} catch(Exception e){
							System.err.println("Cant add Socreboard State to "+pl.getName());
						}
						onColorChange();
						i= 100;
					}
						
					fighter.put(pl.getTeam().getRegisteredName(), i);
				}
				if(team==pl.getTeam()&&team!=null)
				{
					if(fighter.containsKey(this.team))
					{
						float i = fighter.get(this.team);
						if(i<100)
							i += dis*2;
						if(i>100)
						{
							if(teams.size()>1)
							{
								i = 99.9F;
							}
							else
							{
								i=100;
								onColorChange();
							}
						}
						fighter.put(this.team, i);
						
						for(String s : fighter.keySet())
						{
							if(s.equals(this.team))
								continue;
							i = fighter.get(s);
							i-=dis;
							if(i<=0)
							{
								i=0;
							}
							fighter.put(s, i);
						}
					}
					else
					{
						fighter.put(this.team, 0F);
						setTeam("");
						
					}
				}
			}
			
			for(String s : fighter.keySet())
			{
				if(!s.equals(team)&&!teams.contains(worldObj.getScoreboard().getTeam(s)))
				{
					float i = fighter.get(s);
					i-=0.1;
					if(i<=0)
						i=0;
					fighter.put(s, i);
				}
				if(teams.isEmpty()&&s.equals(team))
				{
					float i = fighter.get(s);
					i+=0.05;
					if(i>=100)
						i=100;
					fighter.put(s, i);
				}
			}	
			
			
			if(!worldObj.isRemote&&time==0)
			{
				time = 20;
				worldObj.func_175674_a(null, new AxisAlignedBB(new BlockPos(pos).add(-20, -20, -20), new BlockPos(pos).add(20, 20, 20)), new Predicate() 
				{			
					@Override
					public boolean apply(Object var1) 
					{
						if (var1 instanceof EntityPlayerMP) 
						{
							((EntityPlayerMP)var1).playerNetServerHandler.sendPacket(getDescriptionPacket());
						}
						return false;
					}

				});
			}
			time--;
		}
	}
	
	public void onColorChange()
	{

		if(!worldObj.isRemote)
		{
			List<EntityPlayer> players = worldObj.playerEntities;
			for(EntityPlayer pl: players)
			{
				ChatComponentText c = new ChatComponentText("");
				{
					ChatComponentText c1 = new ChatComponentText("[" + (name.length()>0?name:MainTeam.teamFlag.getLocalizedName()) + "]");
					{
						ChatStyle s = new ChatStyle();
						{
							ChatComponentText c2 = new ChatComponentText(pos.toString());
							HoverEvent event = new HoverEvent(HoverEvent.Action.SHOW_TEXT,c2);
							s.setChatHoverEvent(event);
							s.setColor(EnumChatFormatting.GREEN);
						}
						c1.setChatStyle(s);
					}
					c.appendSibling(c1);
					
					ChatComponentTranslation c2 = new ChatComponentTranslation(team.length()>0?"chat.team.owner":"chat.team.ownerless", getTeamName());
					c.appendSibling(c2);
				}
				pl.addChatMessage(c);
			}
		}
		
		if (!worldObj.isRemote) 
		{
			for(int x = pos.getX()-range;x < pos.getX()+range;x++)
			{
				for(int y = pos.getY()-range;y < pos.getY()+range;y++)
				{
					for(int z = pos.getZ()-range;z < pos.getZ()+range;z++)
					{
						BlockPos xyz = new BlockPos(x, y, z);
						Block b = worldObj.getBlockState(xyz).getBlock();
						//System.out.println(b);
						if(b==MainTeam.teamChest)
						{
							TileEntityTeamChest t = (TileEntityTeamChest) worldObj.getTileEntity(xyz);
							System.out.println(t);
							t.team = this.team;
						}
						if(b==MainTeam.teamBlock || b == MainTeam.teamSpawner || b == MainTeam.teamPressurePlate)
						{
							TileEntityTeamBase t = (TileEntityTeamBase) worldObj.getTileEntity(xyz);
							t.team = this.team;			
							System.out.println(t);
						}
					}
				}
			}
		}
	}
	
	public String getTeamName()
	{
		ScorePlayerTeam team = getTeam();
		if(team!=null)
		{
			return team.formatString(team.getRegisteredName());
		}
		return "";
	}
	
	public String getBestTeam()
	{
		String s1 = "";
		float f=0;
		for(String s2 : fighter.keySet())
		{	
			if(s2.equals(team))
				continue;
			if(fighter.get(s2)>f)
			{
				f = fighter.get(s2);
				s1 = s2;
			}
			
		}	
		return s1;
	}

	public void setTeam(String s)
	{
		if(!this.team.equals(s) && s.length()>0 && !worldObj.isRemote)
		{
			this.team = s;
			
			for(int i=0;i<MainTeam.GoleamSpawn;i++)
			{
				EntityTeamGolem golem = new EntityTeamGolem(worldObj);
				golem.setPosition(pos.getX()+0.5, pos.getY(), pos.getZ()+0.5);
				golem.func_175449_a(pos, range);//setHomeArea
				golem.setTeamName(s);
				worldObj.spawnEntityInWorld(golem);
			}
		}
		this.team = s;
	}
	
	public ScorePlayerTeam getTeam()
	{
		ScorePlayerTeam team = worldObj.getScoreboard().getTeam(this.team);
		return team;
	}
}
