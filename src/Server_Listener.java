import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.ArrayList;

public class Server_Listener implements Runnable
{
    private ObjectInputStream is;
    private static ArrayList<ObjectOutputStream> osList = new ArrayList<ObjectOutputStream>();
    private ObjectOutputStream os;
    private ConnectFourGame game;
    private int player;

    public Server_Listener(ObjectOutputStream os, ObjectInputStream is, ConnectFourGame game, int player)
    {
        this.is	= is;
        this.os	= os;
        this.game = game;
        this.player = player;
        osList.add(os);
    }

    public ObjectInputStream getIS()
    {	return is;	}

    public static ArrayList<ObjectOutputStream> getOSList()
    {	return osList;	}

    public ConnectFourGame getGame()
    {	return game;	}

    public void run()
    {
        try
        {
            while(true)
            {
                Command_To_Server x = (Command_To_Server)is.readObject();

                if(game.getStatus() == ConnectFourGame.PLAYING)
                {
                    if(x.getCommand() == Command_To_Server.MOVE && player == game.getTurn())
                    {
                        game.dropPiece(x.getCol(), player);
                    }
                }
                else
                {
                    if(x.getCommand() == Command_To_Server.NEW_GAME)
                    {
                        if(game.getStatus() == ConnectFourGame.RED_WINS || game.getStatus() == ConnectFourGame.YELLOW_WINS || game.getStatus() == ConnectFourGame.DRAW)
                        {
                            if(player == ConnectFourGame.RED)
                                game.setStatus(ConnectFourGame.WAITING_RESTART_YELLOW);
                            else
                                game.setStatus(ConnectFourGame.WAITING_RESTART_RED);
                        }
                        else if(game.getStatus() == ConnectFourGame.WAITING_RESTART_RED && player == ConnectFourGame.RED)
                            game.reset();
                        else if(game.getStatus() == ConnectFourGame.WAITING_RESTART_YELLOW && player == ConnectFourGame.YELLOW)
                            game.reset();
                    }
                }

                for(ObjectOutputStream tempOS:osList)
                {
                    Command_From_Server a = new Command_From_Server(Command_From_Server.UPDATE_GAME,game);
                    tempOS.writeObject(a);
                    tempOS.reset();
                }

            }
        }
        catch(Exception e)
        {
            System.out.println("Error in Server Listener: "+ e.getMessage());
            game.setStatus(ConnectFourGame.PLAYER_LEFT);
            for(ObjectOutputStream tempOS:osList)
            {
                try
                {
                    Command_From_Server a = new Command_From_Server(Command_From_Server.UPDATE_GAME,game);
                    tempOS.writeObject(a);
                }
                catch(Exception ex)
                {

                }
            }
            e.printStackTrace();
        }
    }
}