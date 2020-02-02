import java.io.*;
import java.net.Socket;


public class Connection extends Thread {

    private Socket connection;
    private String username;
    private  int roomNum;
    DataInputStream dIn;
    DataOutputStream dOut;

    Thread readThread;



    public Connection(Socket socket) throws Exception
    {
        connection = socket;
         dIn = new DataInputStream(connection.getInputStream());
         dOut = new DataOutputStream(connection.getOutputStream());

    }

    @Override
    public void run()
    {
        System.out.println("Client Connected");
        CollectUserInfo();
    }

    private void CollectUserInfo()
    {
        try {
            dOut.writeUTF("Please submit a username");
            dOut.flush();
            String submission = dIn.readUTF();
            username = submission;
            dOut.writeUTF("username accepted your username is " + submission);

            BroadCastMsg(submission + " Has joined room 0");

            dOut.flush();

            Listen();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }


    private void ParseCommand( String command, String[] args)
    {
        if(command.toLowerCase().compareTo("bmi") == 0)
        {
            try{
                float w = Float.parseFloat(args[0]);
                float h = Float.parseFloat(args[1]);
                CmdBmi(w, h);
                System.out.println("weight : " + w + "height : " + h);
            }
            catch (Exception e)
            {
              System.out.println(e);
              BroadcastRelay("Invaldi arguments please enter <bmi> <weight KG> <height meters>");
            }

        }
        else
        {
            BroadcastRelay(  command +" is an unknown Command");
        }

    }

    private void CmdBmi(float w, float h)
    {
        float bmi = (w/(h * h));
        String output = ""; output += bmi;

        if(bmi < 18.5)
        {
            output = "underWeight";
        }
        else if (bmi < 25.00)
        {
            output = "normal";
        }
        else if(bmi < 30)
        {
            output = "overweight";
        }
        else
        {
            output = "Obsese";
        }


        BroadcastRelay(output);

    }

    private void Listen()
    {
        try{
            String data = dIn.readUTF();
            if(data.startsWith("/"))
            {
                String[] cmdInfo = data.split(" ");
                String cmd = cmdInfo[0].substring(1);
                String[] args;
                args = new String[0];

                if(cmdInfo.length > 1)
                {
                    args = new String[cmdInfo.length - 1];
                    for(int i = 0; i < args.length; i++)
                    {
                        args[i] = cmdInfo[i+1];
                    }
                }
                ParseCommand(cmd , args);
            }
            else {
                System.out.println(data);
                BroadCastMsg(username + ": " + data);
            }
            Listen();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    public void BroadcastRelay(String msg )
    {
        try {
            dOut.writeUTF(msg);
            dOut.flush();
            System.out.println(username +  " is trying to send to its client");
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void BroadCastMsg(String msg)
    {
        for (int i = 0; i < Server.clients.size(); i ++)
        {
           Connection c = Server.clients.get(i);
           if(c.username != this.username && c.username != null) {
               c.BroadcastRelay(msg);
           }
        }

    }


}
