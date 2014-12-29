package dragon.team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class CommandRefresh extends CommandBase 
{

	@Override
	public int getRequiredPermissionLevel() 
	{
		return 1;
	}
	
	@Override
	public String getName() 
	{
		return "flags";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_)
	{
		return " [reset / refresh]";
	}

	@Override
	public void execute(ICommandSender send, String[] string) throws WrongUsageException 
	{
		if(string.length==1)
		{
			World w = send.getEntityWorld();
			ArrayList<TileEntityTeamFlag> tiles =  new ArrayList();
			for(Object tile : w.loadedTileEntityList)
			{
				if(tile instanceof TileEntityTeamFlag)
				{
					tiles.add((TileEntityTeamFlag) tile);
				}
			}
			if(string[0].equals("reset"))
			{
				for(TileEntityTeamFlag t : tiles)
				{
					t.reset();
					t.onColorChange();
				}
			}
			else if(string[0].equals("refresh"))
			{
				for(TileEntityTeamFlag t : tiles)
				{
					t.onColorChange();
				}
			}
			else
			{
				throw new WrongUsageException("/"+getName() + getCommandUsage(send));
			}
		}
		else
		{
			throw new WrongUsageException("/" + getName() + getCommandUsage(send));
		}
		
	}
	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] st, BlockPos pos)
	{
		if(st!=null && st.length==1)
		{
			return Arrays.asList("reset","refresh");
		}
			
		return super.addTabCompletionOptions(sender, st, pos);
	}

}
