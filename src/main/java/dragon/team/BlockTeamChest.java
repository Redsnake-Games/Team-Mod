package dragon.team;

import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockTeamChest extends BlockContainer {

	protected BlockTeamChest(int p_i45397_1_) 
	{
		super(Material.iron);
		setCreativeTab(MainTeam.tabTeam);
	}
	
	public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }
	
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) 
	{
		return new TileEntityTeamChest();
	}

	@Override
	public void onBlockPlacedBy(World w, BlockPos pos, IBlockState state, EntityLivingBase ent, ItemStack it) 
	{
		EnumFacing enumfacing = EnumFacing.getHorizontal(MathHelper.floor_double((double)(ent.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3).getOpposite();
        state = state.withProperty(BlockChest.FACING, enumfacing);
        BlockPos blockpos1 = pos.north();
        BlockPos blockpos2 = pos.south();
        BlockPos blockpos3 = pos.west();
        BlockPos blockpos4 = pos.east();
        boolean flag = this == w.getBlockState(blockpos1).getBlock();
        boolean flag1 = this == w.getBlockState(blockpos2).getBlock();
        boolean flag2 = this == w.getBlockState(blockpos3).getBlock();
        boolean flag3 = this == w.getBlockState(blockpos4).getBlock();

        if (!flag && !flag1 && !flag2 && !flag3)
        {
            w.setBlockState(pos, state, 3);
        }
        else if (enumfacing.getAxis() == EnumFacing.Axis.X && (flag || flag1))
        {
            if (flag)
            {
                w.setBlockState(blockpos1, state, 3);
            }
            else
            {
                w.setBlockState(blockpos2, state, 3);
            }

            w.setBlockState(pos, state, 3);
        }
        else if (enumfacing.getAxis() == EnumFacing.Axis.Z && (flag2 || flag3))
        {
            if (flag2)
            {
                w.setBlockState(blockpos3, state, 3);
            }
            else
            {
                w.setBlockState(blockpos4, state, 3);
            }

            w.setBlockState(pos, state, 3);
        }

        if (it.hasDisplayName())
        {
            TileEntity tileentity = w.getTileEntity(pos);

            if (tileentity instanceof TileEntityChest)
            {
                ((TileEntityChest)tileentity).setCustomName(it.getDisplayName());
            }
        }
		
		if(ent.getTeam()!=null)
		{
			TileEntityTeamChest tile = (TileEntityTeamChest) w.getTileEntity(pos);
			tile.setTeam(ent.getTeam().getRegisteredName());
		}
	}
	
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(BlockChest.FACING, placer.getHorizontalFacing());
    }
	
	@Override
	public float getPlayerRelativeBlockHardness(EntityPlayer pl, World w, BlockPos pos) 
	{
		TileEntityTeamChest tile = (TileEntityTeamChest) w.getTileEntity(pos);
		if(tile!=null&&tile.getTeam()!=null&&pl.getTeam()!=null)
		{
			if(tile.getTeam().equals(pl.getTeam().getRegisteredName()))
			{
				return super.getPlayerRelativeBlockHardness(pl, w, pos);
			}
		}
		return -1;
	}
	
	public boolean onBlockActivated(World w, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (w.isRemote)
        {
            return true;
        }
        else
        {
        	TileEntityTeamChest tile = (TileEntityTeamChest) w.getTileEntity(pos);
			if(tile!=null&&tile.getTeam()!=null&&playerIn.getTeam()!=null)
			{
				if(tile.getTeam().equals(playerIn.getTeam().getRegisteredName()))
				{
					playerIn.displayGUIChest(tile);
				}
			}
            return true;
        }
    }
	
	@Override
	public boolean canPlaceBlockAt(World p_149742_1_, BlockPos pos)
    {
		return true;
    }
	
	@Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y)
        {
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(BlockChest.FACING, enumfacing);
    }

	@Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing)state.getValue(BlockChest.FACING)).getIndex();
    }

	@Override
    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {BlockChest.FACING});
    }
	
//	@Override
//	public int getRenderType() 
//	{
//		return 0;
//	}
//	
	
}
