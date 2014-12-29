package dragon.commands;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.INetHandlerPlayServer;
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

public class NetHandlerDCBase implements INetHandlerPlayServer 
{
	protected MinecraftServer server;
	protected EntityPlayerMP playerEntity;
	
	public NetHandlerDCBase(MinecraftServer par1, EntityPlayerMP par2) 
	{
		server = par1;
		playerEntity = par2;
	}
	
	public boolean isCancled()
	{
		return false;
	}

	@Override
	public void onDisconnect(IChatComponent var1) {}

//	@Override
//	public void onConnectionStateTransition(EnumConnectionState var1,EnumConnectionState var2) {}
//
//	@Override
//	public void onNetworkTick() {}

	@Override
	public void handleAnimation(C0APacketAnimation var1) {}

	@Override
	public void processChatMessage(C01PacketChatMessage var1) {}

	@Override
	public void processTabComplete(C14PacketTabComplete var1) {}

	@Override
	public void processClientStatus(C16PacketClientStatus var1) {}

	@Override
	public void processClientSettings(C15PacketClientSettings var1) {}

	@Override
	public void processConfirmTransaction(C0FPacketConfirmTransaction var1) {}

	@Override
	public void processEnchantItem(C11PacketEnchantItem var1) {}

	@Override
	public void processClickWindow(C0EPacketClickWindow var1) {}

	@Override
	public void processCloseWindow(C0DPacketCloseWindow var1) {}

	@Override
	public void processVanilla250Packet(C17PacketCustomPayload var1) {}

	@Override
	public void processUseEntity(C02PacketUseEntity var1) {}

	@Override
	public void processKeepAlive(C00PacketKeepAlive var1) {}

	@Override
	public void processPlayer(C03PacketPlayer var1) {}

	@Override
	public void processPlayerAbilities(C13PacketPlayerAbilities var1) {}

	@Override
	public void processPlayerDigging(C07PacketPlayerDigging var1) {}

	@Override
	public void processEntityAction(C0BPacketEntityAction var1) {}

	@Override
	public void processInput(C0CPacketInput var1) {}

	@Override
	public void processHeldItemChange(C09PacketHeldItemChange var1) {}

	@Override
	public void processCreativeInventoryAction(C10PacketCreativeInventoryAction var1) {}

	@Override
	public void processUpdateSign(C12PacketUpdateSign var1) {}

	@Override
	public void processPlayerBlockPlacement(C08PacketPlayerBlockPlacement var1) {}


	@Override
	public void handleSpectate(C18PacketSpectate packetIn) {}

	@Override
	public void handleResourcePackStatus(C19PacketResourcePackStatus packetIn) {} 

	
}
