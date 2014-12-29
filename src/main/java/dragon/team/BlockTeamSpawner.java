package dragon.team;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTeamSpawner extends BlockContainer 
{

	protected BlockTeamSpawner(Material p_i45386_1_) 
	{
		super(p_i45386_1_);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) 
	{
		return new TileEntityTeamSpawner();
	}
	
	@Override
	public boolean isOpaqueCube() 
	{
		return false;
	}
	
//	@Override
//	public boolean renderAsNormalBlock() 
//	{
//		return false;
//	}
	
	@SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT;
    }
	
	public int getRenderType()
	{
		return 3;
	}
	
	@Override
	public boolean onBlockActivated(World w, BlockPos pos, IBlockState state, EntityPlayer pl, EnumFacing side, float p_149727_7_, float p_149727_8_, float p_149727_9_) 
	{
		if(w.getTileEntity(pos)!=null && !w.isRemote)
		{
			TileEntityTeamSpawner tile = (TileEntityTeamSpawner) w.getTileEntity(pos);
			if(pl.getHeldItem()!=null)
			{
				if(pl.getHeldItem().getItem()==MainTeam.teamWappen && tile.engine<64)
				{
					pl.getHeldItem().stackSize--;
					tile.engine++;
					return true;
				}
			}
		}
		
		return super.onBlockActivated(w, pos, state, pl, side, p_149727_7_, p_149727_8_, p_149727_9_);
	}
	
	@Override
	public void onBlockPlacedBy(World w, BlockPos pos, IBlockState state, EntityLivingBase pl, ItemStack it) 
	{
		if(pl.getTeam()!=null)
		{
			TileEntityTeamBase t = (TileEntityTeamBase) w.getTileEntity(pos);
			t.team = pl.getTeam().getRegisteredName();	
		}
		w.setBlockState(pos, state, 2);
	}
}
