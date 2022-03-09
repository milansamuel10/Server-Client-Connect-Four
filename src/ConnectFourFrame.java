import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ObjectOutputStream;

public class ConnectFourFrame extends JFrame implements Runnable, MouseListener
{
    private int player;
    private ConnectFourGame game;
    private ObjectOutputStream os = null;
    private BufferedImage buffer;
    private long closeTimerStart = -1;

    public ConnectFourFrame(ConnectFourGame game, ObjectOutputStream os, int player)
    {
        super("Connect Four Game by Milan Saju Samuel");
        this.player = player;
        setIgnoreRepaint(true);
        setSize(700,800);
        addMouseListener(this);
        setVisible(true);
        buffer = new BufferedImage(getWidth(),getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        Thread t = new Thread(this);
        t.start();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.game = game;
        this.os	= os;
    }

    public void paint(Graphics m)
    {
        Graphics g = buffer.getGraphics();

        g.setColor(Color.BLUE);
        g.fillRect(0,0,getWidth(),getHeight());

        g.setFont(new Font("Courier New",Font.BOLD,150));
        g.setColor(Color.RED);
        g.drawString("C",5,90);
        g.setColor(Color.YELLOW);
        g.drawString("o",65,90);
        g.setColor(Color.RED);
        g.drawString("n",125,90);
        g.setColor(Color.YELLOW);
        g.drawString("n",185,90);
        g.setColor(Color.RED);
        g.drawString("e",245,90);
        g.setColor(Color.YELLOW);
        g.drawString("c",305,90);
        g.setColor(Color.RED);
        g.drawString("t",365,90);
        g.setColor(Color.YELLOW);
        g.drawString("F",425,90);
        g.setColor(Color.RED);
        g.drawString("o",485,90);
        g.setColor(Color.YELLOW);
        g.drawString("u",545,90);
        g.setColor(Color.RED);
        g.drawString("r",605,90);

        g.setFont(new Font("Courier New",Font.BOLD,15));
        g.setColor(Color.WHITE);
        g.drawString("INSTRUCTIONS: Connect four of your own discs of the same color next to each ",5,725);
        g.drawString("other vertically, horizontally, or diagonally before your opponent.",5,740);

        for(int row = 0; row < 6; row++)
        {
            for(int col = 0; col < 7; col++)
            {
                if(game.getSpot(row,col) == game.RED)
                    g.setColor(Color.RED);
                else if(game.getSpot(row,col) == game.YELLOW)
                    g.setColor(Color.YELLOW);
                else
                    g.setColor(Color.BLACK);

                g.fillOval((col*100),(row*100)+100,100,100);

                if(g.getColor() == Color.BLACK)
                {
                    g.setColor(Color.WHITE);
                    g.drawOval((col*100),(row*100)+100,100,100);
                }
            }
        }

        Font f = new Font("Times New Roman",Font.BOLD,25);
        g.setFont(f);

        int textX = 200;
        int textY = 780;

        if(game.getStatus() == game.WAITING_FOR_YELLOW)
            g.drawString("Waiting for YELLOW to connect",textX,textY);
            //1
        else if(game.getStatus() == game.PLAYING && game.getTurn() == ConnectFourGame.RED && player == ConnectFourGame.RED)
            g.drawString("Your turn.",textX,textY);
            //2
        else if(game.getStatus() == game.PLAYING && game.getTurn() == ConnectFourGame.RED && player==ConnectFourGame.YELLOW)
            g.drawString("RED's Move.",textX,textY);
            //3
        else if(game.getStatus() == game.PLAYING && game.getTurn() == ConnectFourGame.YELLOW && player==ConnectFourGame.YELLOW)
            g.drawString("Your turn.",textX,textY);
            //4
        else if(game.getStatus() == game.PLAYING && game.getTurn() == ConnectFourGame.YELLOW && player==ConnectFourGame.RED)
            g.drawString("YELLOW's Turn.",textX,textY);
            //5
        else if(game.getStatus() == game.RED_WINS && player==ConnectFourGame.RED)
            g.drawString("You Win! Right click for new Game.",textX-100,textY);
            //6
        else if(game.getStatus() == game.RED_WINS && player==ConnectFourGame.YELLOW)
            g.drawString("You Lose! Right click for new Game.",textX-100,textY);
            //7
        else if(game.getStatus() == game.YELLOW_WINS && player==ConnectFourGame.YELLOW)
            g.drawString("You Win! Right click for new Game.",textX-100,textY);
            //8
        else if(game.getStatus() == game.YELLOW_WINS && player==ConnectFourGame.RED)
            g.drawString("You Lose! Right click for new Game.",textX-100,textY);
            //9
        else if(game.getStatus() == game.DRAW)
            g.drawString("Tie Game. Right click for new Game.",textX-100,textY);
            //10
        else if(game.getStatus()==game.WAITING_RESTART_YELLOW && player==ConnectFourGame.YELLOW)
            g.drawString("RED is ready, right click for new Game.",textX-100,textY);
            //11
        else if(game.getStatus()==game.WAITING_RESTART_RED && player==ConnectFourGame.RED)
            g.drawString("YELLOW is ready, right click for new Game.",textX-100,textY);
            //12
        else if(game.getStatus()==game.WAITING_RESTART_YELLOW && player==ConnectFourGame.RED)
            g.drawString("Waiting for YELLOW to agree to a new game.",textX-100,textY);
            //13
        else if(game.getStatus()==game.WAITING_RESTART_RED && player==ConnectFourGame.YELLOW)
            g.drawString("Waiting for RED to agree to a new game.",textX-100,textY);
            //14
        else if(game.getStatus()==game.PLAYER_LEFT && player==ConnectFourGame.YELLOW)
            g.drawString("RED quit. Shutting down in: " +(5-(System.nanoTime()-closeTimerStart)/1000000000L),textX-150,textY);
            //15
        else if(game.getStatus()==game.PLAYER_LEFT && player==ConnectFourGame.RED)
            g.drawString("YELLOW quit. Shutting down in: " +(5-(System.nanoTime()-closeTimerStart)/1000000000L),textX-150,textY );
            // WRONG
        else
            g.drawString("Program Fails!",textX,textY);

        m.drawImage(buffer,0,0,null);
    }

    public void mousePressed(MouseEvent e)
    {

    }

    public void mouseReleased(MouseEvent e)
    {
        try
        {
            int clickedAt = e.getX();

            if(game.getStatus() == game.PLAYING && e.getButton() == e.BUTTON1)
            {
                os.writeObject(new Command_To_Server(Command_To_Server.MOVE,(int)(clickedAt / 100)));
                os.reset();
            }
            if(game.getStatus() != game.PLAYING && e.getButton() == e.BUTTON3)
            {
                os.writeObject(new Command_To_Server(Command_To_Server.NEW_GAME));
                os.reset();
            }

            repaint();
        }
        catch(Exception ex)
        {
            System.out.println("Error in Frame-KeyTyped: "+ex.getMessage());
            ex.printStackTrace();
        }
    }


    public void mouseClicked(MouseEvent e)
    {

    }

    public void mouseEntered(MouseEvent e)
    {

    }

    public void mouseExited(MouseEvent e)
    {

    }

    public void run()
    {
        try
        {
            while(true)
            {
                Thread.sleep(50);
                paint(this.getGraphics());

                if(game.getStatus() == ConnectFourGame.PLAYER_LEFT && closeTimerStart == -1)
                {
                    closeTimerStart = System.nanoTime();
                }
                else if(game.getStatus() == ConnectFourGame.PLAYER_LEFT && (5-(System.nanoTime()-closeTimerStart)/1000000000L) <= 0)
                {
                    System.exit(4);
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("Error in Frame run: "+e.getMessage());
            e.printStackTrace();
        }
    }
}