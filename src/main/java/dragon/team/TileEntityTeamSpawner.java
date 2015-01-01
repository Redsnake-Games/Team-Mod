package dragon.team;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class TileEntityTeamSpawner extends TileEntityTeamBase implements IUpdatePlayerListBox
{
	public int engine = 0;
	public int spawnTime = 0;
	public int maxtime = 800;
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) 
	{
		super.writeToNBT(nbt);
		nbt.setInteger("engine", engine);
		nbt.setInteger("spawnTime", spawnTime);
		nbt.setInteger("maxtime", maxtime);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{
		super.readFromNBT(nbt);
		engine = nbt.getInteger("engine");
		spawnTime = nbt.getInteger("spawnTime");
		maxtime = nbt.getInteger("maxtime");
	}
	
	@Override
	public void update() 
	{
		super.update();		
		if(spawnTime <= 0 && engine >0 && !worldObj.isRemote)
		{		
			ScorePlayerTeam team = worldObj.getScoreboard().getTeam(this.getTeam());
			if(team!=null)
			{
				EntityTeamGolem golem = new EntityTeamGolem(worldObj);
				int x = pos.getX() + worldObj.rand.nextInt(2) - worldObj.rand.nextInt(2);
				int z = pos.getY() + worldObj.rand.nextInt(2) - worldObj.rand.nextInt(2);
				int y = pos.getZ();
				if(worldObj.getBlockState(new BlockPos(x, y, z))==Blocks.air && worldObj.getBlockState(new BlockPos(x, y+1, z))==Blocks.air && worldObj.getEntitiesWithinAABB(EntityTeamGolem.class,  new AxisAlignedBB(pos, pos).offset(0.5, 0.5, 0.5).expand(10, 10, 10)).size()<10)
				{
					golem.setPosition(x, y, z);
					golem.setTeamName(getTeam());
					golem.setAlternateHome(x, y, z, 30);
					worldObj.spawnEntityInWorld(golem);
					engine--;
					spawnTime = maxtime;
					worldObj.markBlockForUpdate(pos);
				}				
			}
		}
		if(engine>0)
			spawnTime--;
	}
}
