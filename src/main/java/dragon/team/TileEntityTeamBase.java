package dragon.team;


import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

import com.google.common.base.Predicate;

public class TileEntityTeamBase extends TileEntity implements IUpdatePlayerListBox, ITeamAble
{
	private String team = "";
	private String prefix = "";
//	int time=0;
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) 
	{
		nbt.setString("team", team);
		nbt.setString("prefix", prefix);
		super.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{
		team = nbt.getString("team");	
		prefix = nbt.getString("prefix");
		super.readFromNBT(nbt);
	}
	
	@Override
	public Packet getDescriptionPacket() 
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		S35PacketUpdateTileEntity pkt = new S35PacketUpdateTileEntity(this.pos, getBlockMetadata(), nbt);
		return pkt;
	}
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) 
	{
		readFromNBT(pkt.getNbtCompound());
		worldObj.markBlockForUpdate(this.pos);
		super.onDataPacket(net, pkt);
	}
	
	@Override
	public void update() 
	{
//		if(team.length()>0)
//		{
//			ScorePlayerTeam t = worldObj.getScoreboard().getTeam(team);
//			if(t!=null)
//			{
//				prefix = t.getColorPrefix();
//			}
//			else
//			{
//				prefix="";
//			}
//		}
//		else
//		{
//			prefix="";
//		}
//		if(!worldObj.isRemote&&time==0)
//		{
//			//System.out.println(team + " at " + pos);
//			time = 20;
//			worldObj.func_175674_a(null, new AxisAlignedBB(new BlockPos(pos).add(-20, -20, -20), new BlockPos(pos).add(20, 20, 20)), new Predicate() 
//			{			
//				@Override
//				public boolean apply(Object var1) 
//				{
//					if (var1 instanceof EntityPlayerMP) 
//					{
//						((EntityPlayerMP)var1).playerNetServerHandler.sendPacket(getDescriptionPacket());
//					}
//					return false;
//				}
//
//			});
//		}
//		time--;
	}

	@Override
	public String getTeam() 
	{
		return team;
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
			prefix="";
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
