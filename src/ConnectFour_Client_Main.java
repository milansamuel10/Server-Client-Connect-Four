import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ConnectFour_Client_Main
{
    public static void main(String[] args)
    {
        try
        {
            Socket connectionToServer = new Socket("127.0.0.1",8001);

            ObjectInputStream is = new ObjectInputStream(connectionToServer.getInputStream());

            ObjectOutputStream os = new ObjectOutputStream(connectionToServer.getOutputStream());

            Command_From_Server cfs = (Command_From_Server)is.readObject();

            if(cfs.getCommand() == cfs.CONNECTED_AS_RED)
            {
                System.out.println("Logging in as RED.");
                Clients_Listener cl = new Clients_Listener(os,is,cfs.getGame());
                Thread t = new Thread(cl);
                t.start();
                new ConnectFourFrame(cfs.getGame(),os,ConnectFourGame.RED);
            }
            else
            {
                System.out.println("Logging in as YELLOW.");
                Clients_Listener cl = new Clients_Listener(os,is,cfs.getGame());
                Thread t = new Thread(cl);
                t.start();
                new ConnectFourFrame(cfs.getGame(),os,ConnectFourGame.YELLOW);
            }
        }
        catch(Exception e)
        {
            System.out.println("Error in main: " + e.getMessage());
            e.printStackTrace();
        }
    }
}