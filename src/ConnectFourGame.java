import java.io.Serializable;

public class ConnectFourGame implements Serializable
{
    private int[][] board = new int[6][7];
    int status = WAITING_FOR_YELLOW;
    int turn = RED;
    public static final int WAITING_FOR_YELLOW = 0;
    public static final int WAITING_RESTART_YELLOW = 1;
    public static final int WAITING_RESTART_RED = 2;
    public static final int EMPTY = 3;
    public static final int RED = 4;
    public static final int YELLOW = 5;
    public static final int DRAW = 6;
    public static final int RED_WINS = 7;
    public static final int YELLOW_WINS = 8;
    public static final int PLAYING = 9;
    public static final int PLAYER_LEFT = 10;

    public ConnectFourGame(int status)
    {
        this.status = status;
        this.turn = RED;
       for(int r = 0; r < 6; r++)
        {
            for(int c = 0; c < 7; c++)
                setSpot(r,c,EMPTY);
        }
    }

    public void update(ConnectFourGame other)
    {
        this.status = other.getStatus();
        turn = other.getTurn();

        for(int r = 0; r < board.length; r++)
        {
            for(int c = 0; c < board[0].length; c++)
                board[r][c] = other.getSpot(r,c);
        }
    }

    public boolean dropPiece(int c, int piece)
    {
        for(int r = 5; r >= 0; r--)
        {
            if(board[r][c] == EMPTY)
            {
                board[r][c] = piece;
                changeTurns();
                updateStatus();
                return true;
            }
        }

        return false;
    }

    public boolean isWinner(int player)
    {
        for(int r = 0; r < 6;r++)
        {
            for(int c = 0; c <= 3; c++)
            {
                if(board[r][c] == player && board[r][c+1] == player && board[r][c+2] == player && board[r][c+3] == player)
                    return true;
            }
        }

        for(int r = 0; r <= 2;r++)
        {
            for(int c = 0; c < 7; c++)
            {
                if(board[r][c] == player && board[r+1][c] == player && board[r+2][c] == player && board[r+3][c] == player)
                    return true;
            }
        }

        for(int r = 0; r <= 2;r++)
        {
            for(int c = 3; c < 7; c++)
            {
                if(board[r][c] == player && board[r+1][c-1] == player && board[r+2][c-2] == player && board[r+3][c-3] == player)
                    return true;
            }
        }

        for(int r = 0; r <= 2;r++)
        {
            for(int c = 0; c <= 3; c++)
            {
                if(board[r][c] == player && board[r+1][c+1] == player && board[r+2][c+2] == player && board[r+3][c+3] == player)
                    return true;
            }
        }

        return false;
    }

    public boolean isDraw()
    {
        for(int r = 0; r < 6;r++)
        {
            for(int c = 0; c < 7; c++)
            {
                if(board[r][c] == EMPTY && !isWinner(RED) && !isWinner(YELLOW))
                    return false;
            }
        }
        return true;
    }

    public void updateStatus()
    {
        if(isWinner(RED))
        {
            setStatus(ConnectFourGame.RED_WINS);
            System.out.println("RED wins.");
        }
        else if(isWinner(YELLOW))
        {
            setStatus(ConnectFourGame.YELLOW_WINS);
            System.out.println("YELLOW wins.");
        }
        else if(isDraw())
            setStatus(ConnectFourGame.DRAW);
        else
            setStatus(ConnectFourGame.PLAYING);
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn)
    {
        this.turn = turn;
    }

    public int getSpot(int r, int c)
    {	return board[r][c];	}

    public void setSpot(int r, int c, int player)
    {	board[r][c] = player;	}

    public void changeTurns()
    {
        if(getTurn() == RED)
            setTurn(YELLOW);
        else
            setTurn(RED);
    }

    public void reset()
    {
        turn = RED;
        status = PLAYING;
        board = new int[6][7];

        for(int r = 0; r < 6;r++)
        {
            for(int c = 0; c < 7; c++)
                board[r][c] = EMPTY;
        }
    }

    public void draw()
    {
        for(int r = 0; r < 6; r++)
        {
            for(int c = 0; c < 7; c++)
            {
                if(board[r][c] == EMPTY)
                    System.out.print(" ");
                else if(board[r][c] == RED)
                    System.out.print("R");
                else if(board[r][c] == YELLOW)
                    System.out.print("Y");
            }

            System.out.println();
        }
    }
}