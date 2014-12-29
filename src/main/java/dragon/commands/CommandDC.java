package dragon.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

public class CommandDC extends CommandBase 
{
	
	@Override
	public int getRequiredPermissionLevel() 
	{
		return 0;
	}
	
	@Override
	public String getName() 
	{
		return "dc";
	}

	@Override
	public String getCommandUsage(ICommandSender var1) 
	{
		return getName() + " - to show informations from Commands";
	}

	@Override
	public void execute(ICommandSender var1, String[] var2) 
	{
		if(var2.length > 1)
		{
			
		}
		else
		{
				HashMap<String, ICommandDragon> coms = getCommand(var1, var2.length==1?var2[0]:"");
				for(String s : coms.keySet())
				{
					var1.addChatMessage(new ChatComponentText(s + " - " + coms.get(s).getShortDiscription()));
				}
		}
	}
	
	public HashMap<String, ICommandDragon> getCommand(ICommandSender var1, String var2)
	{
		HashMap<String, ICommandDragon> c = new HashMap();
		List<ICommand> coms = MinecraftServer.getServer().getCommandManager().getPossibleCommands(var1);
		
		for(ICommand o : coms)
		{
			if(o instanceof ICommandDragon)
			{
				if(var2.length()==0 || o.getName().startsWith(var2))
				c.put(o.getName(), (ICommandDragon)o);
			}
		}
		
		return c;
	}
	
	@Override
	public List addTabCompletionOptions(ICommandSender var1, String[] var2, BlockPos pos) 
	{
		if(var2.length == 1)
		{
			return  new ArrayList(getCommand(var1, var2.length==1?var2[0]:"").keySet());
		}
		return super.addTabCompletionOptions(var1, var2, pos);
	}

	@Override
	public int compareTo(Object arg0)
	{
		return 0;
	}

}
