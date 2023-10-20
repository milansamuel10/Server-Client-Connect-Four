import java.io.Serializable;

public class Command_From_Server implements Serializable
{
    private int command = 0;
    private ConnectFourGame game =null;
    public static final int UPDATE_GAME 	= 1;
    public static final int CONNECTED_AS_RED 	= 2;
    public static final int CONNECTED_AS_YELLOW 	= 3;

    public Command_From_Server(int command, ConnectFourGame game)
    {
        this.command	= command;
        this.game		= game;
    }

    public int getCommand()
    {
        return command;
    }

    public ConnectFourGame getGame()
    {
        return game;
    }
}