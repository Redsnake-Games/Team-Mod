package dragon.team;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.google.common.base.Predicate;

public class BlockTeamPressurePlate extends BlockBasePressurePlate implements ITileEntityProvider
{

	protected BlockTeamPressurePlate(Material p_i45387_2_) 
	{
		super(p_i45387_2_);
		setCreativeTab(MainTeam.tabTeam);
		setHarvestLevel("pickaxe", 1);
	}
//	@Override
//	protected void updateState(World worldIn, BlockPos pos, IBlockState state, int oldRedstoneStrength) 
//	{
//		TileEntity tile = worldIn.getTileEntity(pos);
//		NBTTagCompound nbt = new NBTTagCompound();
//		tile.writeToNBT(nbt);
//		super.updateState(worldIn, pos, state, oldRedstoneStrength);
//		TileEntity tile2 = createNewTileEntity(worldIn, 0);
//		tile2.setWorldObj(worldIn);
//		tile2.readFromNBT(nbt);
//		worldIn.setTileEntity(pos, tile2);
//	}

	public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam)
    {
        super.onBlockEventReceived(worldIn, pos, state, eventID, eventParam);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity == null ? false : tileentity.receiveClientEvent(eventID, eventParam);
    }
	
    @Override
    protected int computeRedstoneStrength(final World w, final BlockPos pos)
    {
        List<EntityPlayer>  list = w.func_175674_a(null, this.getSensitiveAABB(pos), new Predicate<Entity>() //getEntitiesWithinAABBExcludingEntity
        {	
			@Override
			public boolean apply(Entity var1) 
			{
				if(var1 instanceof EntityPlayer)
				{
					TileEntityTeamBase base = (TileEntityTeamBase) w.getTileEntity(pos);
					if(base.getTeam().length()>0)
					{
						if(((EntityPlayer) var1).getTeam()!=null && base.getTeam().equals(((EntityPlayer) var1).getTeam().getRegisteredName()))
						{
							return true;
						}
						return false;
					}
					else
					{
						return true;
					}
				}
				return false;
			}
		});

        if (list != null && !list.isEmpty())
        {
            Iterator<EntityPlayer> iterator = list.iterator();

            while (iterator.hasNext())
            {
            	EntityPlayer entity = iterator.next();

                if (!entity.doesEntityNotTriggerPressurePlate())
                {
                    return 15;
                }
            }
        }

        return 0;
    }

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) 
	{
		return new TileEntityTeamBase();
	}

	@Override
	public int colorMultiplier(IBlockAccess w, BlockPos pos, int r) 
	{		
		TileEntityTeamBase tile = ((TileEntityTeamBase)w.getTileEntity(pos));
		if(tile!=null)
		{
			return getColor(tile.getPrefix());	
		}
		return super.colorMultiplier(w, pos, r);
	}
	
	private int getColor(String s)
	{
		Collection<String> c = EnumChatFormatting.getValidValues(true, false);
		int i = 0;
		for(String e : c)
		{
			if(s.equals(EnumChatFormatting.getValueByName(e).toString()))
			{
				return ItemLayerSword.colorCode[i];
			}
			
			i++;
		}
		return 0xffffff;
	}
	
	@Override
	public void onBlockPlacedBy(World w, BlockPos pos, IBlockState state, EntityLivingBase pl, ItemStack it) 
	{
		if(pl.getTeam()!=null)
		{
			TileEntityTeamBase t = (TileEntityTeamBase) w.getTileEntity(pos);
			t.setTeam(pl.getTeam().getRegisteredName());	
		}
		//w.setBlockState(pos, state);
	}
	
	@Override
	public float getPlayerRelativeBlockHardness(EntityPlayer pl, World w, BlockPos pos) 
	{
		TileEntityTeamBase tile = (TileEntityTeamBase) w.getTileEntity(pos);
		if(tile!=null&&tile.getTeam()!=null&&pl.getTeam()!=null)
		{
			if(tile.getTeam().equals(pl.getTeam().getRegisteredName()))
			{
				return super.getPlayerRelativeBlockHardness(pl, w, pos);
			}
		}
		return -1;
	}


	protected int getRedstoneStrength(IBlockState p_176576_1_)
    {
        return ((Boolean)p_176576_1_.getValue(BlockPressurePlate.POWERED)).booleanValue() ? 15 : 0;
    }

    protected IBlockState setRedstoneStrength(IBlockState p_176575_1_, int p_176575_2_)
    {
        return p_176575_1_.withProperty(BlockPressurePlate.POWERED, Boolean.valueOf(p_176575_2_ > 0));
    }
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(BlockPressurePlate.POWERED, Boolean.valueOf(meta == 1));
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((Boolean)state.getValue(BlockPressurePlate.POWERED)).booleanValue() ? 1 : 0;
	}
	
	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[] {BlockPressurePlate.POWERED});
	}
	    
}
