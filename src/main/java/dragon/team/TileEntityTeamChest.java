package dragon.team;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import com.google.common.base.Predicate;

public class TileEntityTeamChest extends TileEntityChest implements ISidedInventory
{
	String team = null;
	String prefix = null;
	int time=0;
	
	@Override
	public void writeToNBT(NBTTagCompound p_145841_1_) 
	{
		super.writeToNBT(p_145841_1_);
		if(team!=null)
			p_145841_1_.setString("team", team);
		if(prefix!=null)
			p_145841_1_.setString("prefix", prefix);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) 
	{
		super.readFromNBT(p_145839_1_);
		if(p_145839_1_.hasKey("team"))
		{
			team = p_145839_1_.getString("team");
		}
		if(p_145839_1_.hasKey("prefix"))
		{
			prefix = p_145839_1_.getString("prefix");
		}
	}
	
	private boolean func_174912_b(BlockPos pos)
	{
		Block block = this.worldObj.getBlockState(pos).getBlock();
		return block instanceof BlockTeamChest;
	}
	 
	public void closeInventory()
	{
		if (this.getBlockType() instanceof BlockTeamChest)
		{
			--this.numPlayersUsing;
			this.worldObj.addBlockEvent(pos, this.getBlockType(), 1, this.numPlayersUsing);
			this.worldObj.notifyBlockOfStateChange(pos, this.getBlockType());
			this.worldObj.notifyBlockOfStateChange(new BlockPos(pos).add(0, -1, 0), this.getBlockType());
		}
	}
	 
	@Override
	public int getChestType() 
	{
		return 30;
	}
	
	@Override
	public void update() 
	{

		super.update();
		
		if(team !=null)
		{
			ScorePlayerTeam t = worldObj.getScoreboard().getTeam(team);
			if(t!=null)
			{
				prefix = t.getColorPrefix();
			}
			else
			{
				prefix=null;
			}
		}
		else
		{
			prefix=null;
		}
		
		if(!worldObj.isRemote && time <= 0)
		{
			time = 20;
			
			NBTTagCompound nbt = new NBTTagCompound();
			writeToNBT(nbt);
			final S35PacketUpdateTileEntity pack = new S35PacketUpdateTileEntity(pos, getBlockMetadata(), nbt);
			worldObj.func_175674_a(null, new AxisAlignedBB(new BlockPos(pos).add(-20, -20, -20), new BlockPos(pos).add(20, 20, 20)), new Predicate() 
			{			
				@Override
				public boolean apply(Object var1) 
				{
					if (var1 instanceof EntityPlayerMP) 
					{
						((EntityPlayerMP)var1).playerNetServerHandler.sendPacket(pack);
					}
					return false;
				}

			});
		}
		time--;
	}
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) 
	{
		if(worldObj.isRemote && pkt.func_179823_a().equals(pos))
		{
			readFromNBT(pkt.getNbtCompound());
		}
	}
	
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer pl) 
	{
		if(team==null||team.length()<=0)
		{
			return super.isUseableByPlayer(pl);
		}
		if(pl.getTeam()!=null)
		{
			Team t = pl.getTeam();
			return team.equals(t.getRegisteredName());
		}
		if(team!=null&&team.length()>0)
			return false;
		
		return super.isUseableByPlayer(pl);
	}

	@Override
	public int[] getSlotsForFace(EnumFacing var1) 
	{
		int[] ints = new int[getSizeInventory()];
		for(int i=0;i<ints.length;i++)
		{
			ints[i] = i;
		}
		return ints;
	}


	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
	{
		return true;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) 
	{
		return false;
	}
}
