import java.net.*;
import java.util.ArrayList;
import java.util.List;


public class Server {

    static ServerSocket welcomeSocket;
    public static ArrayList<Connection> clients;

    public static void main(String[] args)
    {
        try{
            StartServer();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
    public static void StartServer() throws Exception
    {
        welcomeSocket = new ServerSocket(7000);
        System.out.println("ServerStarted");
        clients = new ArrayList<Connection>();
        ContinueAccept();

    }
    private  static  void ContinueAccept() throws Exception
    {
        Connection con = new Connection(welcomeSocket.accept());
        con.start();
        clients.add(con);
        ContinueAccept();
    }





}
