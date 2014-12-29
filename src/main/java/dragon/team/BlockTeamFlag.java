package dragon.team;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockTeamFlag extends BlockContainer 
{

	protected BlockTeamFlag(Material p_i45386_1_) 
	{
		super(p_i45386_1_);
		setCreativeTab(MainTeam.tabTeam);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(World w, BlockPos pos, IBlockState state) 
	{
		return null;
	}

	@Override
	public void onBlockPlacedBy(World w, BlockPos pos, IBlockState state, EntityLivingBase pl,ItemStack it) 
	{
		TileEntity tile = w.getTileEntity(pos);
		if(tile instanceof TileEntityTeamFlag)
		{
			((TileEntityTeamFlag) tile).rotation = pl.rotationYaw+180;
			
			if (it.getTagCompound() != null && it.getTagCompound().hasKey("display", 10))
	        {
	            NBTTagCompound nbttagcompound = it.getTagCompound().getCompoundTag("display");

	            if (nbttagcompound.hasKey("Name", 8))
	            {
	                ((TileEntityTeamFlag) tile).name = nbttagcompound.getString("Name");
	            }
	        }
			if (it.getTagCompound() != null && it.getTagCompound().hasKey("range"))
	        {
				((TileEntityTeamFlag) tile).range = it.getTagCompound().getInteger("range");
	        }
		}
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) 
	{
		return new TileEntityTeamFlag();
	}
	
	@Override
	public boolean isNormalCube() 
	{
		return false;
	}
	
	@Override
	public boolean isOpaqueCube() 
	{
		return false;
	}
	
	@Override
	public int getRenderType() 
	{
		return -1;
	}
	
	@Override
	public float getPlayerRelativeBlockHardness(EntityPlayer pl, World w, BlockPos pos) 
	{
		return -1;
	}
}
