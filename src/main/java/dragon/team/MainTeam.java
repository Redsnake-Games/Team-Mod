package dragon.team;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.stats.StatBase;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
//import net.minecraftforge.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.base.Predicate;

import dragon.Render.ObjectReader;
import dragon.Render.ObjectRenderer;
//import net.minecraft.client.renderer.RenderBlocks;

@Mod(modid=MainTeam.modID,name=MainTeam.modName,version=MainTeam.modVersion)
public class MainTeam 
{
	public static final String modID = "team";
	public static final String modName = "TeamItems";
	public static final String modVersion = "4.0";
	@Mod.Instance(MainTeam.modID)
	public static MainTeam instance;
//	private static int armorID=0;// = RenderingRegistry.addNewArmourRendererPrefix("Team");
	public static ObjectRenderer obj;
	public static boolean trollOP = false;
	public static int GoleamSpawn = 3;
	public static boolean craftingFlag = false;
	public static boolean ignoreTeamLessPlayer = true;
	
//	public static int renderID;
	
//	static
//	{
//		try {
//		initClient();
//		} catch(Throwable e)
//		{}
//	}
	
//	@SideOnly(Side.CLIENT)
//	private static void initClient()
//	{
//		armorID = RenderingRegistry.addNewArmourRendererPrefix("Team");
//	}
//	
	public static final CreativeTabs tabTeam = new CreativeTabs("tabTeam") 
	{	
		@Override
		public Item getTabIconItem() 
		{
			return MainTeam.teamSword;
		}
	};
	
	public static final StatBase captureState = new StatBase("stat.captureFlag", new ChatComponentTranslation("stat.captureFlag", new Object[0]), StatBase.simpleStatType).initIndependentStat().registerStat();
	
	@Register
	public static final Item teamSword = new ItemLayerSword(ToolMaterial.STONE).setUnlocalizedName("teamSword");//.setTextureName("team:TeamSchwert");
	@Register
	public static final Item teamWappen = new ItemLayerWappen().setUnlocalizedName("teamWappen");//.setTextureName("team:TeamWappen");
	@Register
	public static final Item seggel = new ItemSeggel().setUnlocalizedName("seggel").setCreativeTab(tabTeam);//.setTextureName("team:Segeltuch");
	
	@Register
	public static final Item teamHelmet = (new ItemLayerArmor(ItemArmor.ArmorMaterial.LEATHER, 0)).setUnlocalizedName("teamHelmet");//.setTextureName("team:team_helmet");
	@Register
	public static final Item teamChestplate = (new ItemLayerArmor(ItemArmor.ArmorMaterial.LEATHER, 1)).setUnlocalizedName("teamChestplate");//.setTextureName("team:team_chestplate");
	@Register
	public static final Item teamLeggings = (new ItemLayerArmor(ItemArmor.ArmorMaterial.LEATHER, 2)).setUnlocalizedName("teamLeggings");//.setTextureName("team:team_leggings");
	@Register 
	public static final Item teamBoots = (new ItemLayerArmor(ItemArmor.ArmorMaterial.LEATHER, 3)).setUnlocalizedName("teamBoots");//.setTextureName("team:team_boots");
	
	@Register 
	public static final Block teamChest = new BlockTeamChest(0).setHardness(2.5F).setResistance(6000.0F).setStepSound(Block.soundTypeWood).setUnlocalizedName("teamChest");//.setBlockTextureName("planks_oak");
	@Register 
	public static final Block teamFlag = new BlockTeamFlag(Material.anvil).setHardness(2.5F).setResistance(6000.0F).setUnlocalizedName("teamFlag");//.setBlockTextureName("team:flagge");
	@Register(8)
	public static final Block teamBlock = new BlockTeamBlock(Material.rock).setUnlocalizedName("teamBlock").setHardness(2.5F).setResistance(6000.0F);//.setBlockTextureName("team:Block");
	@Register 
	public static final Block teamSpawner = new BlockTeamSpawner(Material.iron).setUnlocalizedName("teamSpawner").setCreativeTab(tabTeam);//.setBlockTextureName("team:spawner");
	@Register 
	public static final Block teamPressurePlate = new BlockTeamPressurePlate(Material.rock).setHardness(2.5F).setResistance(6000.0F).setUnlocalizedName("teamPressurePlate");
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		GameRegistry.registerItem(teamSword, "teamSword", modID);
		GameRegistry.registerItem(teamHelmet, "teamHelmet", modID);
		GameRegistry.registerItem(teamChestplate, "teamChestplate", modID);
		GameRegistry.registerItem(teamLeggings, "teamLeggings", modID);
		GameRegistry.registerItem(teamBoots, "teamBoots", modID);
		GameRegistry.registerItem(teamWappen, "teamWappen", modID);
		GameRegistry.registerItem(seggel, "seggel", modID);
		GameRegistry.registerBlock(teamChest, "teamChest");
		GameRegistry.registerBlock(teamFlag, "teamFlag");
		GameRegistry.registerBlock(teamBlock, ItemBlockMeta.class, "teamBlock");
		GameRegistry.registerBlock(teamSpawner, "teamSpawner");
		GameRegistry.registerBlock(teamPressurePlate, "teamPressurePlate");
		
		try {
			obj = ObjectReader.readObj(MainTeam.class.getResource("teamFlag.obj"));
		
			File f = event.getSuggestedConfigurationFile();
			if(!f.exists())
			{
				Properties p = new Properties();
				p.setProperty("DontLikeFlyingOps", "" + trollOP);
				p.setProperty("SpawnGolemAtFlag", "" + GoleamSpawn);
				p.setProperty("CraftingableFlag", "" + craftingFlag);
				p.setProperty("ignoreTeamlessPlayer", "" + ignoreTeamLessPlayer);
				p.store(new FileOutputStream(f), modName + " config File");
			}
			else
			{
				Properties p = new Properties();
				p.load(new FileInputStream(f));
				this.trollOP = Boolean.valueOf(p.getProperty("DontLikeFlyingOps"));
				this.GoleamSpawn = Integer.valueOf(p.getProperty("SpawnGolemAtFlag"));
				this.craftingFlag = Boolean.valueOf(p.getProperty("CraftingableFlag"));
				this.ignoreTeamLessPlayer = Boolean.valueOf(p.getProperty("ignoreTeamlessPlayer"));
			}
		} catch (Exception e) 
		{
			e.printStackTrace();
			event.getSuggestedConfigurationFile().delete();
		}
		
	}
	
	@Mod.EventHandler
	public void Init(FMLInitializationEvent event)
	{
		addCrafting();
		
		GameRegistry.registerTileEntity(TileEntityTeamChest.class, "teamChest");
		GameRegistry.registerTileEntity(TileEntityTeamFlag.class, "teamFlag");
		GameRegistry.registerTileEntity(TileEntityTeamBase.class, "teamBlock");
		GameRegistry.registerTileEntity(TileEntityTeamSpawner.class, "teamSpawner");
		
		//EntityRegistry.registerModEntity(EntityTeamGolem.class, "teamGolem", 1, instance, 50, 120, true);
		EntityRegistry.registerGlobalEntityID(EntityTeamGolem.class, "teamGolem", 112, 0xffff55, 0x55ffff);
		
		
		MinecraftForge.EVENT_BUS.register(instance);
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		
	}
	
	@Mod.EventHandler
	public void startServer(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CommandRefresh());
	}
	
	@SideOnly(Side.CLIENT)
	@Mod.EventHandler
	public void InitClient(FMLInitializationEvent event)
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTeamChest.class, new RenderTeamChest());	
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTeamFlag.class, new RenderTeamFlag());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTeamSpawner.class, new RenderTeamSpawner());
		RenderingRegistry.registerEntityRenderingHandler(EntityTeamGolem.class, new RenderTeamGolem());
//		BlockModelShapes shapes = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getBlockModelShapes();
//		shapes.registerBlockWithStateMapper(teamBlock, new StateTeamMap());
//		
		//shapes.registerBlockWithStateMapper(teamChest, new StateMap.Builder().addPropertiesToIgnore(BlockChest.FACING).build());
		
		try {
			registerRender(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		renderID = RenderingRegistry.getNextAvailableRenderId();
//		RenderingRegistry.registerBlockHandler(renderID, new ISimpleBlockRenderingHandler() {
//			
//			@Override
//			public boolean shouldRender3DInInventory(int modelId) 
//			{
//				return true;
//			}
//			
//			@Override
//			public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) 
//			{
//				return false;
//			}
//			
//			@Override
//			public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) 
//			{
//				GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
//                GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
//				TileEntitySpecialRenderer sp = (TileEntitySpecialRenderer) TileEntityRendererDispatcher.instance.mapSpecialRenderers.get(TileEntityTeamChest.class);
//				TileEntityTeamChest t = new TileEntityTeamChest();
//				t.blockMetadata = 1;
//				sp.renderTileEntityAt(t, 0.0D, 0.0D, 0.0D, 0.0F);	
//				GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//			}
//			
//			@Override
//			public int getRenderId() 
//			{
//				return renderID;
//			}
//		});
	}
	
	
	@SubscribeEvent
	public void onDeath(LivingDeathEvent event)
	{
		if(event.source.getEntity() instanceof EntityPlayer && event.entityLiving.getTeam()!=null)
		{
			ScorePlayerTeam team = (ScorePlayerTeam) event.entityLiving.getTeam();
			ItemStack it = new ItemStack(this.teamWappen);
			it.setTagCompound(new NBTTagCompound());
			it.getTagCompound().setString("team", team.getRegisteredName());
			it.getTagCompound().setString("prefix", team.getColorPrefix());
			it.getTagCompound().setString("suffix", team.getColorSuffix());
			event.entityLiving.entityDropItem(it, 0.0F);
		}
	}
	
	@SubscribeEvent
	public void onHurt(LivingHurtEvent event)
	{
		if(event.source.getEntity() instanceof EntityPlayer)
		{
			final EntityPlayer pl = ((EntityPlayer)event.source.getEntity());
			if(pl.getHeldItem()!=null&&pl.getHeldItem().getItem()==this.teamSword)
			{
				List<EntityLivingBase> list = pl.worldObj.func_175674_a(pl, pl.getEntityBoundingBox().expand(10, 10, 10), new Predicate<Entity>()//getEntitiesWithinAABBExcludingEntity
				{
					
					@Override
					public boolean apply(Entity var1) 
					{
						if(var1 instanceof EntityLivingBase)
						{
							return pl.isOnTeam(((EntityLivingBase) var1).getTeam());
						}
						
						return false;
					}
				});
				EntityXPOrb xp = new EntityXPOrb(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, list.size());
				event.entityLiving.worldObj.spawnEntityInWorld(xp);
			}
			
		}
	}
	
	public void addCrafting()
	{
		GameRegistry.addShapelessRecipe(addAuto(new ItemStack(teamSword)), Items.iron_sword, this.teamWappen);
		
		GameRegistry.addShapelessRecipe(addAuto(new ItemStack(teamHelmet)), Items.leather_helmet, this.teamWappen);
		GameRegistry.addShapelessRecipe(addAuto(new ItemStack(teamChestplate)), Items.leather_chestplate, this.teamWappen);
		GameRegistry.addShapelessRecipe(addAuto(new ItemStack(teamLeggings)), Items.leather_leggings, this.teamWappen);
		GameRegistry.addShapelessRecipe(addAuto(new ItemStack(teamBoots)), Items.leather_boots, this.teamWappen);
		
		GameRegistry.addShapelessRecipe(new ItemStack(teamChest), Blocks.chest, this.teamWappen);
		GameRegistry.addShapelessRecipe(new ItemStack(teamBlock), Blocks.stone, this.teamWappen);
		
		for(int i=0;i<8;i++)
		{
			GameRegistry.addShapelessRecipe(new ItemStack(teamBlock,1,(i+1)%8), new ItemStack(this.teamBlock,1,i));
		}
		
		GameRegistry.addShapedRecipe(new ItemStack(seggel), "SSS","SSS","SSS",'S',Items.string);
		if(craftingFlag)
			GameRegistry.addShapedRecipe(new ItemStack(teamFlag), "W","T","S",'W',this.teamWappen,'T',this.seggel,'S',Items.stick);
		
		GameRegistry.addShapedRecipe(new ItemStack(this.teamPressurePlate), "XX", 'X', new ItemStack(this.teamBlock,1,1));
		GameRegistry.addShapedRecipe(new ItemStack(teamSpawner), "GGG","GWG", "GGG", 'G', Items.gold_ingot, 'W', this.teamWappen);
	}
	
	private ItemStack addAuto(ItemStack it)
	{
		it.setTagCompound(new NBTTagCompound());
		it.getTagCompound().setBoolean("auto", true);
		return it;
	}
	
	@SideOnly(Side.CLIENT)
	private static void registerRender(MainTeam mod) throws Exception
	{
		Field[] fields = mod.getClass().getFields();
		for(Field f : fields)
		{
			if(f.isAnnotationPresent(Register.class))
			{
				Item item=null;
				String name="";
				
				if(f.getType() == Item.class)
				{
					item = (Item) f.get(mod);
				}
				else if(f.getType() == Block.class)
				{
					Block block = (Block) f.get(mod);
					item = Item.getItemFromBlock(block);
				}
				int val = f.getAnnotation(Register.class).value();
				if(val>0)
				{
					for(int i=0;i<val;i++)
					{
						ItemStack it = new ItemStack(item,1,i);
						name = it.getUnlocalizedName();
						name = item instanceof ItemBlock ? name.replaceFirst("tile.", "") : name.replaceFirst("item.", "");
						ModelBakery.addVariantName(item, mod.modID+":"+name);
						register(it, new ModelResourceLocation(mod.modID+":"+name, "inventory"));
					}
				}
				else
				{
					ItemStack it = new ItemStack(item,1,0);
					name = it.getUnlocalizedName();
					name = item instanceof ItemBlock ? name.replaceFirst("tile.", "") : name.replaceFirst("item.", "");
					register(it, new ModelResourceLocation(mod.modID+":"+name, "inventory"));
				}
//				new ModelMap(item);
				
				//Minecraft.getMinecraft().getRenderItem().getItemModelMesher().rebuildCache();
			}
		}
		FontRenderer render;
	}
	
	@SideOnly(Side.CLIENT)
	private static void register(ItemStack it, ModelResourceLocation res)
	{
		System.out.println(String.format("Register %s in %s",it,res));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(it.getItem(),it.getItemDamage(), res);
	}
	
	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	private static @interface Register 
	{
		public int value() default 0;
	}
}
