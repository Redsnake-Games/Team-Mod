package dragon.commands;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.network.play.client.C11PacketEnchantItem;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IChatComponent;

public class NetHandlerDCServer extends NetHandlerPlayServer
{
	private MinecraftServer server;
	private ArrayList<NetHandlerDCBase> listeners = new ArrayList();
	
	private NetHandlerDCServer(MinecraftServer par1MinecraftServer, NetworkManager par2iNetworkManager, EntityPlayerMP par3EntityPlayerMP) 
	{
		super(par1MinecraftServer, par2iNetworkManager, par3EntityPlayerMP);
	}
	
	public NetHandlerDCServer(NetHandlerPlayServer par1, ArrayList<Class<? extends NetHandlerDCBase>> par2) 
	{
		this(getServer(par1), par1.netManager, par1.playerEntity);
		server = getServer(par1);
		for(Class<? extends NetHandlerDCBase> c : par2)
		{
			try 
			{
				Constructor<? extends NetHandlerDCBase> constructor = c.getConstructor(MinecraftServer.class, EntityPlayerMP.class);
				NetHandlerDCBase g = constructor.newInstance(server, par1.playerEntity);
				listeners.add(g);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static MinecraftServer getServer(NetHandlerPlayServer par1)
	{
		Field[] fi = NetHandlerPlayServer.class.getDeclaredFields();
		for(Field f : fi)
		{
			if(f.getType() == MinecraftServer.class)
			{
				f.setAccessible(true);
				try 
				{
					return (MinecraftServer) f.get(par1);
				}
				catch (IllegalArgumentException e) 
				{
					e.printStackTrace();
				}
				catch (IllegalAccessException e) 
				{
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	@Override
	public void onDisconnect(IChatComponent var1) 
	{
		boolean b = true;
		for(NetHandlerDCBase base : listeners)
		{
			base.onDisconnect(var1);
			b = b && !base.isCancled();
		}
		if(b)
		{
			super.onDisconnect(var1);
		}
	}

//	@Override
//	public void onConnectionStateTransition(EnumConnectionState var1,EnumConnectionState var2) 
//	{
//		boolean b = true;
//		for(NetHandlerDCBase base : listeners)
//		{
//			base.onConnectionStateTransition(var1, var2);
//			b = b && !base.isCancled();
//		}
//		if(b)
//		{
//			super.onConnectionStateTransition(var1, var2);
//		}
//		
//	}

//	@Override
//	public void update() 
//	{
//		boolean b = true;
//		for(NetHandlerDCBase base : listeners)
//		{
//			base.update();
//			b = b && !base.isCancled();
//		}
//		if(b)
//		{
//			super.update();
//		}
//	}

	@Override
	public void handleAnimation(C0APacketAnimation var1) 
	{
		boolean b = true;
		for(NetHandlerDCBase base : listeners)
		{
			base.handleAnimation(var1);
			b = b && !base.isCancled();
		}
		if(b)
		{
			super.handleAnimation(var1);
		}
	}

	@Override
	public void processChatMessage(C01PacketChatMessage var1) 
	{
		boolean b = true;
		for(NetHandlerDCBase base : listeners)
		{
			base.processChatMessage(var1);
			b = b && !base.isCancled();
		}
		if(b)
		{
			super.processChatMessage(var1);
		}
	}

	@Override
	public void processTabComplete(C14PacketTabComplete var1) 
	{
		boolean b = true;
		for(NetHandlerDCBase base : listeners)
		{
			base.processTabComplete(var1);
			b = b && !base.isCancled();
		}
		if(b)
		{
			super.processTabComplete(var1);
		}
	}

	@Override
	public void processClientStatus(C16PacketClientStatus var1) 
	{
		boolean b = true;
		for(NetHandlerDCBase base : listeners)
		{
			base.processClientStatus(var1);
			b = b && !base.isCancled();
		}
		if(b)
		{
			super.processClientStatus(var1);
		}
	}

	@Override
	public void processClientSettings(C15PacketClientSettings var1) 
	{
		boolean b = true;
		for(NetHandlerDCBase base : listeners)
		{
			base.processClientSettings(var1);
			b = b && !base.isCancled();
		}
		if(b)
		{
			super.processClientSettings(var1);
		}
	}

	@Override
	public void processConfirmTransaction(C0FPacketConfirmTransaction var1) 
	{
		boolean b = true;
		for(NetHandlerDCBase base : listeners)
		{
			base.processConfirmTransaction(var1);
			b = b && !base.isCancled();
		}
		if(b)
		{
			super.processConfirmTransaction(var1);
		}
	}

	@Override
	public void processEnchantItem(C11PacketEnchantItem var1) 
	{
		boolean b = true;
		for(NetHandlerDCBase base : listeners)
		{
			base.processEnchantItem(var1);
			b = b && !base.isCancled();
		}
		if(b)
		{
			super.processEnchantItem(var1);
		}
	}

	@Override
	public void processClickWindow(C0EPacketClickWindow var1) 
	{
		boolean b = true;
		for(NetHandlerDCBase base : listeners)
		{
			base.processClickWindow(var1);
			b = b && !base.isCancled();
		}
		if(b)
		{
			super.processClickWindow(var1);
		}
	}

	@Override
	public void processCloseWindow(C0DPacketCloseWindow var1) 
	{
		boolean b = true;
		for(NetHandlerDCBase base : listeners)
		{
			base.processCloseWindow(var1);
			b = b && !base.isCancled();
		}
		if(b)
		{
			super.processCloseWindow(var1);
		}
	}

	@Override
	public void processVanilla250Packet(C17PacketCustomPayload var1) 
	{
		boolean b = true;
		for(NetHandlerDCBase base : listeners)
		{
			base.processVanilla250Packet(var1);
			b = b && !base.isCancled();
		}
		if(b)
		{
			super.processVanilla250Packet(var1);
		}
	}

	@Override
	public void processUseEntity(C02PacketUseEntity var1) 
	{
		boolean b = true;
		for(NetHandlerDCBase base : listeners)
		{
			base.processUseEntity(var1);
			b = b && !base.isCancled();
		}
		if(b)
		{
			super.processUseEntity(var1);
		}
	}

	@Override
	public void processKeepAlive(C00PacketKeepAlive var1) 
	{
		boolean b = true;
		for(NetHandlerDCBase base : listeners)
		{
			base.processKeepAlive(var1);
			b = b && !base.isCancled();
		}
		if(b)
		{
			super.processKeepAlive(var1);
		}
	}

	@Override
	public void processPlayer(C03PacketPlayer var1) 
	{
		boolean b = true;
		for(NetHandlerDCBase base : listeners)
		{
			base.processPlayer(var1);
			b = b && !base.isCancled();
		}
		if(b)
		{
			super.processPlayer(var1);
		}
	}

	@Override
	public void processPlayerAbilities(C13PacketPlayerAbilities var1) 
	{
		boolean b = true;
		for(NetHandlerDCBase base : listeners)
		{
			base.processPlayerAbilities(var1);
			b = b && !base.isCancled();
		}
		if(b)
		{
			super.processPlayerAbilities(var1);
		}
	}

	@Override
	public void processPlayerDigging(C07PacketPlayerDigging var1) 
	{
		boolean b = true;
		for(NetHandlerDCBase base : listeners)
		{
			base.processPlayerDigging(var1);
			b = b && !base.isCancled();
		}
		if(b)
		{
			super.processPlayerDigging(var1);
		}
	}

	@Override
	public void processEntityAction(C0BPacketEntityAction var1) 
	{
		boolean b = true;
		for(NetHandlerDCBase base : listeners)
		{
			base.processEntityAction(var1);
			b = b && !base.isCancled();
		}
		if(b)
		{
			super.processEntityAction(var1);
		}
	}

	@Override
	public void processInput(C0CPacketInput var1) 
	{
		boolean b = true;
		for(NetHandlerDCBase base : listeners)
		{
			base.processInput(var1);
			b = b && !base.isCancled();
		}
		if(b)
		{
			super.processInput(var1);
		}
	}

	@Override
	public void processHeldItemChange(C09PacketHeldItemChange var1) 
	{
		boolean b = true;
		for(NetHandlerDCBase base : listeners)
		{
			base.processHeldItemChange(var1);
			b = b && !base.isCancled();
		}
		if(b)
		{
			super.processHeldItemChange(var1);
		}
	}

	@Override
	public void processCreativeInventoryAction(C10PacketCreativeInventoryAction var1) 
	{
		boolean b = true;
		for(NetHandlerDCBase base : listeners)
		{
			base.processCreativeInventoryAction(var1);
			b = b && !base.isCancled();
		}
		if(b)
		{
			super.processCreativeInventoryAction(var1);
		}
	}

	@Override
	public void processUpdateSign(C12PacketUpdateSign var1) 
	{
		boolean b = true;
		for(NetHandlerDCBase base : listeners)
		{
			base.processUpdateSign(var1);
			b = b && !base.isCancled();
		}
		if(b)
		{
			super.processUpdateSign(var1);
		}
	}

	@Override
	public void processPlayerBlockPlacement(C08PacketPlayerBlockPlacement var1) 
	{
		boolean b = true;
		for(NetHandlerDCBase base : listeners)
		{
			base.processPlayerBlockPlacement(var1);
			b = b && !base.isCancled();
		}
		if(b)
		{
			super.processPlayerBlockPlacement(var1);
		}
	}
	
	@Override
	public void handleSpectate(C18PacketSpectate packetIn) 
	{
		boolean b = true;
		for(NetHandlerDCBase base : listeners)
		{
			base.handleSpectate(packetIn);
			b = b && !base.isCancled();
		}
		if(b)
		{
			super.handleSpectate(packetIn);
		}	
	}

	@Override
	public void handleResourcePackStatus(C19PacketResourcePackStatus packetIn) 
	{
		boolean b = true;
		for(NetHandlerDCBase base : listeners)
		{
			base.handleResourcePackStatus(packetIn);
			b = b && !base.isCancled();
		}
		if(b)
		{
			super.handleResourcePackStatus(packetIn);
		}	
	} 

}
