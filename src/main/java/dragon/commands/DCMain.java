package dragon.commands;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@Mod(modid = DCMain.MODID, version=DCMain.VERSION, name="Dragon Commands")
public class DCMain 
{
	public static final String MODID = "dcmain";
	public static final String VERSION = "1.0";
	@Instance(DCMain.MODID)
	public static DCMain instance = new DCMain();
	
	public static ArrayList<Class<? extends NetHandlerDCBase>> listeners = new ArrayList<Class<? extends NetHandlerDCBase>>();
	
	@EventHandler
	public void Init(FMLInitializationEvent event)
	{
		FMLCommonHandler.instance().bus().register(instance);
	}
	
	@EventHandler
	public void onServerStarting(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CommandDC());
	}
	
	@SubscribeEvent
	public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event)
	{
		if(event.player instanceof EntityPlayerMP)
		{
			EntityPlayerMP mp = (EntityPlayerMP) event.player;
			mp.playerNetServerHandler = new NetHandlerDCServer(mp.playerNetServerHandler,listeners);
		}
	}

}
