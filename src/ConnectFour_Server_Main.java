import java.io.*;
import java.net.*;

public class ConnectFour_Server_Main
{
    public static void main(String[] args)
    {
        try
        {
            ServerSocket serverSocket = new ServerSocket(8001);
            ConnectFourGame game = new ConnectFourGame(ConnectFourGame.WAITING_FOR_YELLOW);

            // RED connect Code
            Socket redConnectionToClient = serverSocket.accept();
            ObjectOutputStream redOOS = new ObjectOutputStream(redConnectionToClient.getOutputStream());
            ObjectInputStream redOIS = new ObjectInputStream(redConnectionToClient.getInputStream());

            redOOS.writeObject(new Command_From_Server(Command_From_Server.CONNECTED_AS_RED,game));
            redOOS.reset();

            Thread t = new Thread(new Server_Listener(redOOS,redOIS,game,ConnectFourGame.RED));
            t.start();
            System.out.println("RED has connected");

            // YELLOW connect Code
            Socket yellowConnectionToClient = serverSocket.accept();
            ObjectOutputStream yellowOOS = new ObjectOutputStream(yellowConnectionToClient.getOutputStream());
            ObjectInputStream yellowOIS = new ObjectInputStream(yellowConnectionToClient.getInputStream());

            yellowOOS.writeObject(new Command_From_Server(Command_From_Server.CONNECTED_AS_YELLOW,game));
            yellowOOS.reset();
            t = new Thread(new Server_Listener(yellowOOS,yellowOIS,game,ConnectFourGame.YELLOW));
            t.start();
            System.out.println("YELLOW has connected");

            game.setStatus(game.PLAYING);
            game.setTurn(ConnectFourGame.RED);

            Command_From_Server a = new Command_From_Server(Command_From_Server.UPDATE_GAME,game);

            redOOS.writeObject(a);
            redOOS.reset();
            yellowOOS.writeObject(a);
            yellowOOS.reset();
        }
        catch(Exception e)
        {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}