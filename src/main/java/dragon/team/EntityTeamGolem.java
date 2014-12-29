package dragon.team;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.base.Predicate;

public class EntityTeamGolem extends EntityGolem 
{
	private int attackTimer;
	private int homeChecker=20;
	private BlockPos coords; 
	private int area = -1;
	
	public EntityTeamGolem(World par1World) 
	{
		super(par1World);
		float f = (float) (1.8D/2.9D);
		//this.setSize(f*1.4F, f*2.9F);
		this.setSize(0.6F, 1.8F);
		//this.getNavigator().setAvoidsWater(true);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIAttackOnCollide(this, 1.0D, true));
		this.tasks.addTask(2, new EntityAIMoveTowardsTarget(this, 1.0D, 32.0F));
		this.tasks.addTask(3, new EntityAIMoveTowardsRestriction(this, 1.0D));
		this.tasks.addTask(5, new EntityAIWander(this, 0.6D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		this.tasks.addTask(7, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityLivingBase.class, 0, true, false, new Predicate<Entity>() 
		{
			@Override
			public boolean apply(Entity var1) 
			{
				if(MainTeam.trollOP && var1 instanceof EntityPlayer)
				{					
					EntityPlayer pl = (EntityPlayer) var1;
					if(pl.capabilities.isCreativeMode && MinecraftServer.getServer().getConfigurationManager().canSendCommands(pl.getGameProfile()))
						pl.setGameType(GameType.SURVIVAL);
					
					pl.addChatMessage(new ChatComponentText("Dislike Flying Ops"));
					return true;
				}
				return (((EntityLivingBase)var1).getTeam()!=null || !MainTeam.ignoreTeamLessPlayer) && ((EntityLivingBase)var1).getTeam() != EntityTeamGolem.this.getTeam();
			}
		}));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, false, true, IMob.mobSelector));
		/*this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityTeamGolem.class, 0, false, true, new IEntitySelector()
		{			
			@Override
			public boolean isEntityApplicable(Entity var1) 
			{
				return ((EntityLivingBase) var1).getTeam() != EntityTeamGolem.this.getTeam();
			}
		}));*/
	}
	
	public boolean isAIEnabled()
    {
        return true;
    }
	
	@Override
	protected void entityInit() 
	{
		super.entityInit();
		this.dataWatcher.addObject(16, "");
	}
	
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3D);
	}
	
	public void setTeamName(String s)
	{
		this.dataWatcher.updateObject(16, s);
	}
	
	public String getTeamName()
	{
		return this.dataWatcher.getWatchableObjectString(16);
	}
	
	@Override
	public Team getTeam() 
	{
		ScorePlayerTeam team = worldObj.getScoreboard().getTeam(getTeamName());
		return team;
	}
	
	protected int decreaseAirSupply(int par1)
    {
        return par1;
    }
	
	protected void collideWithEntity(Entity par1Entity)
    {
        if(this.getTeam()!=null)
        {
			if(par1Entity instanceof EntityPlayer && !((EntityPlayer) par1Entity).capabilities.disableDamage && (((EntityPlayer) par1Entity).getTeam()!=null || !MainTeam.ignoreTeamLessPlayer) && ((EntityPlayer) par1Entity).getTeam()!=this.getTeam())
	        {
	        	this.setAttackTarget((EntityLivingBase)par1Entity);
	        }
	        else if (par1Entity instanceof EntityTeamGolem && ((EntityTeamGolem)par1Entity).getTeam()!=null && ((EntityTeamGolem)par1Entity).getTeam() != this.getTeam())
	        {
	        	this.setAttackTarget((EntityLivingBase)par1Entity);
	        }
        }
        else if (par1Entity instanceof IMob && this.getRNG().nextInt(10) == 0)
        {
            this.setAttackTarget((EntityLivingBase)par1Entity);
        }
        
        super.collideWithEntity(par1Entity);
    }
	
	public boolean canAttackClass(Class par1Class)
    {
        return EntityGhast.class != par1Class;
    }
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) 
	{
		super.writeEntityToNBT(nbt);
		nbt.setString("team", getTeamName());
		if(hasHome())
		{
			NBTTagCompound tag = new NBTTagCompound();
			BlockPos c = func_180486_cf();//getHomePosition();
			tag.setInteger("posX", c.getX());
			tag.setInteger("posY", c.getY());
			tag.setInteger("posZ", c.getZ());
			tag.setFloat("area", getMaximumHomeDistance());
			nbt.setTag("home", tag);
		}
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) 
	{
		super.readEntityFromNBT(nbt);
		setTeamName(nbt.getString("team"));
		if(nbt.hasKey("home"))
		{
			NBTTagCompound tag = nbt.getCompoundTag("home");
			func_175449_a(new BlockPos(tag.getInteger("posX"), tag.getInteger("posY"), tag.getInteger("posZ")), (int)tag.getFloat("area"));//setHomeArea
		}
	}
	
	protected String getHurtSound()
    {
        return "mob.irongolem.hit";
    }

    protected String getDeathSound()
    {
        return "mob.irongolem.death";
    }

    protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_)
    {
        this.playSound("mob.irongolem.walk", 1.0F, 1.0F);
    }
    
    public boolean attackEntityAsMob(Entity par1Entity)
    {
        this.attackTimer = 10;
        this.worldObj.setEntityState(this, (byte)4);
        boolean flag = par1Entity instanceof EntityCreeper || par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float)(7 + this.rand.nextInt(8)));

        if (flag)
        {
            par1Entity.motionY += 0.4000000059604645D + this.rand.nextDouble();
        }
        
        this.playSound("mob.irongolem.throw", 1.0F, 1.0F);
        return flag;
    }

    @SideOnly(Side.CLIENT)
    public void handleHealthUpdate(byte par1)
    {
        if (par1 == 4)
        {
            this.attackTimer = 10;
            this.playSound("mob.irongolem.throw", 1.0F, 1.0F);
        }
        else
        {
            super.handleHealthUpdate(par1);
        }
    }
    
    public int getAttackTimer() 
    {
		return attackTimer;
	}
    
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
       
        if (this.attackTimer > 0)
        {
            --this.attackTimer;
        }
        
        if (this.motionX * this.motionX + this.motionZ * this.motionZ > 2.500000277905201E-7D && this.rand.nextInt(5) == 0)
        {
        	int i = MathHelper.floor_double(this.posX);
            int j = MathHelper.floor_double(this.posY - 0.20000000298023224D);
            int k = MathHelper.floor_double(this.posZ);
            IBlockState iblockstate = this.worldObj.getBlockState(new BlockPos(i, j, k));
            Block block = iblockstate.getBlock();

            if (block.getMaterial() != Material.air)
            {
                this.worldObj.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + ((double)this.rand.nextFloat() - 0.5D) * (double)this.width, this.getEntityBoundingBox().minY + 0.1D, this.posZ + ((double)this.rand.nextFloat() - 0.5D) * (double)this.width, 4.0D * ((double)this.rand.nextFloat() - 0.5D), 0.5D, ((double)this.rand.nextFloat() - 0.5D) * 4.0D, new int[] {Block.getStateId(iblockstate)});
            }
        }
    }
    
    @Override
    protected void updateAITick() 
    {
    	 if(this.getAttackTarget() instanceof EntityTeamGolem && isOnTeam(this.getAttackTarget().getTeam()))
         {
         	setAttackTarget(null);
         }
         
         if(homeChecker<=0 && !worldObj.isRemote)
         {
         	homeChecker = 20;
         	if(!this.hasHome())
         	{
         		TileEntityTeamFlag flag = getNearestFlag(true);
         		if(flag!=null)
         		{
         			this.func_175449_a(flag.getPos(), flag.range);//setHomeArea
         			this.setTeamName(flag.getTeam().getRegisteredName());
         		}
         		else
         		{
         			if(this.area>0)
         			{
         				this.func_175449_a(this.coords, area);//setHomeArea
         			}
         			
         			flag = getNearestFlag(false);
             		if(flag!=null)
             		{
             			this.func_175449_a(flag.getPos(), flag.range);//setHomeArea
             			this.setTeamName(flag.getTeam().getRegisteredName());
             		}
         		}
         	}
         }
         this.homeChecker--;

    	super.updateAITick();
    }
    
    public boolean allowLeashing()
    {
        return false;
    }
    
    private TileEntityTeamFlag getNearestFlag(boolean team)
    {
    	TileEntityTeamFlag flag = null;
    	for(TileEntity t : (List<TileEntity>)worldObj.loadedTileEntityList)
    	{
    		if(t instanceof TileEntityTeamFlag && (!team || ((TileEntityTeamFlag) t).getTeam()==this.getTeam()))
    		{
    			if(((TileEntityTeamFlag)t).getTeam()==null)
    			{
    				continue;
    			}
    			if(flag==null||this.getDistanceSq(flag.getPos())>this.getDistanceSq(t.getPos()))
    			{
    				flag = (TileEntityTeamFlag) t;
    			}
    		}
    	}
    	if(area>0 && flag!=null)
    	{
    		if(this.getDistanceSq(flag.getPos())>20*20)
    		{
    			return null;
    		}
    	}
    	return flag;
    }
    
    public void setAlternateHome(int x, int y, int z, int area)
    {
    	coords = new BlockPos(x,y,z);
    	this.area = area;
    }
}
