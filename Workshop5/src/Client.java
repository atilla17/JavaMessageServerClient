import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class Client {
    static Socket connection;
    static DataOutputStream outStream;
    static  DataInputStream inStream;
    static  String userInput;
    public  static void main(String[] args)
    {
        try {
            Boot();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
    private static void Boot() throws Exception
    {
        connection = new Socket("localhost", 7000);
        outStream = new DataOutputStream(connection.getOutputStream());
        inStream = new DataInputStream(connection.getInputStream());

        Thread listenThread = new Thread(){
            public void run(){
                try {
                    Listen();
                }
                catch (Exception e)
                {
                    System.out.println(e);
                }
            }
        };
        listenThread.start();
        ReadInput();


    }

    private  static  void Listen()throws  Exception
    {
        System.out.println(inStream.readUTF());
        Listen();
    }

    private static  void ReadInput() throws Exception
    {
        Scanner in = new Scanner(System.in);
        String msg = in.nextLine();
        outStream.writeUTF(msg);
        outStream.flush();
        ReadInput();
    }

}
