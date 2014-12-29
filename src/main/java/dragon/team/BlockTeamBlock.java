package dragon.team;

import java.util.Collection;
import java.util.List;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
//import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
//import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTeamBlock extends BlockContainer
{
	
//	IIcon[] icons = new IIcon[8];
	public static final PropertyInteger META = PropertyInteger.create("metadata", 0, 15);
	
	protected BlockTeamBlock(Material p_i45394_1_) {
		super(p_i45394_1_);
		setCreativeTab(MainTeam.tabTeam);
		setHarvestLevel("pickaxe", 1);
		this.setDefaultState(this.blockState.getBaseState().withProperty(META, 0));
	}
	
	@Override
	public int colorMultiplier(IBlockAccess w, BlockPos pos,int renderPass)
	{
		String prefix = ((TileEntityTeamBase)w.getTileEntity(pos)).prefix;
		return getColor(prefix);	
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) 
	{
		return new TileEntityTeamBase();
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
	
	public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(META, meta);
    }

    public int getMetaFromState(IBlockState state)
    {
        return (Integer) state.getValue(META);
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {META});
    }
	
//	@Override
//	public void registerBlockIcons(IIconRegister r) 
//	{
//		for(int i=0;i<icons.length;i++)
//		{
//			icons[i] = r.registerIcon(getTextureName() + (i+1));
//		}			
//	}
//	
//	@Override
//	public IIcon getIcon(int side, int meta) 
//	{
//		if(meta<icons.length)
//		{
//			return icons[meta];
//		}
//			
//		return super.getIcon(side, meta);
//	}
	
	@Override
	public void getSubBlocks(Item it, CreativeTabs t, List l) 
	{
		for(int i=0;i<8;i++)
		{
			l.add(new ItemStack(it,1,i));
		}
	}
	@Override
	public void onBlockPlacedBy(World w, BlockPos pos, IBlockState state, EntityLivingBase pl, ItemStack it)
	{
		if(pl.getTeam()!=null)
		{
			TileEntityTeamBase t = (TileEntityTeamBase) w.getTileEntity(pos);
			t.team = pl.getTeam().getRegisteredName();	
		}
		IBlockState newState = state.withProperty(META, it.getItemDamage());
		w.setBlockState(pos, newState, 2);
	}
	

	
	@Override
	public int damageDropped(IBlockState i) 
	{
		return (Integer) i.getValue(META);
	}
	
	@Override
	public float getPlayerRelativeBlockHardness(EntityPlayer pl, World w, BlockPos pos) 
	{
		TileEntityTeamBase tile = (TileEntityTeamBase) w.getTileEntity(pos);
		if(tile!=null&&tile.team!=null&&pl.getTeam()!=null)
		{
			if(tile.team.equals(pl.getTeam().getRegisteredName()))
			{
				return super.getPlayerRelativeBlockHardness(pl, w, pos);
			}
		}
		return -1;
	}
	
	public int getRenderType()
    {
        return 3;
    }
	
}
