import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Clients_Listener implements Runnable
{
    private ObjectInputStream is;
    private ObjectOutputStream os;
    private ConnectFourGame game;

    public Clients_Listener(ObjectOutputStream os, ObjectInputStream is, ConnectFourGame game)
    {
        this.is = is;
        this.os	= os;
        this.game = game;
    }

    public ObjectInputStream getIS()
    {	return is;	}

    public ObjectOutputStream getOS()
    {	return os;	}

    public ConnectFourGame getGame()
    {	return game;	}

    public void run()
    {
        try
        {
            while(true)
            {
                Command_From_Server red =(Command_From_Server)is.readObject();

                if(red.getCommand() == red.UPDATE_GAME)
                {
                    game.update(red.getGame());
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("Error in Clients' Listener: " + e.getMessage());
            System.out.println("Lost connection to the server.");
            System.out.println("Disconnecting.");
            e.printStackTrace();
            System.exit(0);
        }
    }
}