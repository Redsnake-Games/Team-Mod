package dragon.team;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import com.google.common.base.Predicate;

public class TileEntityTeamChest extends TileEntityChest implements ISidedInventory, ITeamAble, IUpdatePlayerListBox
{
	private String team = null;
	private String prefix = null;
	int time=0;
	
	@Override
	public void writeToNBT(NBTTagCompound p_145841_1_) 
	{
		super.writeToNBT(p_145841_1_);
		if(team!=null)
			p_145841_1_.setString("team", team);
		if(getPrefix()!=null)
			p_145841_1_.setString("prefix", getPrefix());
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
			prefix = (p_145839_1_.getString("prefix"));
		}
	}
	
	private boolean func_174912_b(BlockPos pos)
	{
		Block block = this.worldObj.getBlockState(pos).getBlock();
		return block instanceof BlockTeamChest;
	}
	 
	@Override
	public Packet getDescriptionPacket() 
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		S35PacketUpdateTileEntity pkt = new S35PacketUpdateTileEntity(this.pos, getBlockMetadata(), nbt);
		return pkt;
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
	}
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) 
	{
		if(worldObj.isRemote && pkt.func_179823_a().equals(pos))
		{
			readFromNBT(pkt.getNbtCompound());
			worldObj.markBlockForUpdate(this.pos);
			super.onDataPacket(net, pkt);
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

	@Override
	public String getTeam() 
	{
		return this.team;
	}

	@Override
	public void setTeam(String s) 
	{
		this.team = s;
		ScorePlayerTeam t = worldObj.getScoreboard().getTeam(team);
		if(t!=null)
		{
			prefix = t.getColorPrefix();
		}
		else
		{
			prefix = "";
		}
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

	public String getPrefix()
	{
		return prefix;
	}
}
