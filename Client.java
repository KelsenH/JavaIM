/*
 JAVA IM
 Kelsen Hazelwood 
 **/


import java.io.*;
import java.awt.*;
import java.net.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame
{
  private JTextField userText;
  private JTextArea chatWindow;
  private ObjectOutputStream output;
  private ObjectInputStream input;
  private String message = "";
  private String serverIP;
  private Socket connection;

  public Client (String host)
  {
    super ("Instant Messenger");
    serverIP = host;
    userText = new JTextField();
    userText.setEditable(false);
    userText.addActionListener(
      new ActionListener ()
      {
        public void actionPerformed (ActionEvent event)
        {
          sendMessage (event.getActionCommand());
          userText.setText ("");
        }
      }
    );
    add (userText, BorderLayout.SOUTH);
    chatWindow = new JTextArea();
    add (new JScrollPane (chatWindow), BorderLayout.CENTER);
    setSize (500,500);
    setVisible (true);  
  }

  public void startRunning ()
  {
    try
    {
      connectToServer ();
      setupStreams ();
      whileChatting ();
    } catch (EOFException eofException)
    {
      showMessage ("\n Client Terminated the Connection");
    } catch (IOException ioException)
    {
      ioException.printStackTrace();
    } finally 
    {
      endProgram ();
    }
  }

  private void connectToServer () throws IOException
  {
    showMessage ("Attempting connection . . .");
    connection = new Socket (InetAddress.getByName(serverIP), 1234);
    showMessage ("\n Connected to " + connection.getInetAddress().getHostName() + "\n");
  }

  private void setupStreams () throws IOException
  {
    output = new ObjectOutputStream (connection.getOutputStream());
    output.flush();
    input = new ObjectInputStream (connection.getInputStream());
  } 

  private void whileChatting () throws IOException
  {
    userText.setEditable(true);
    do 
    {
      try
      {
        message = (String) input.readObject();
        showMessage ("\n" + message);
      }
      catch (ClassNotFoundException classNotFoundException)
      {
        showMessage ("\n Invalid Input");
      }
    } while (!message.equals ("SERVER - END"));
  }

  private void endProgram ()
  {
    chatWindow.setEditable (false);
     try
     {
       output.close();
       input.close();
       connection.close();
     } catch (IOException ioException)
     {
       ioException.printStackTrace();
     }
  }

  private void sendMessage (String message)
  {
    try 
     {
       output.writeObject ("CLIENT - " + message);
       output.flush ();
       showMessage ("\nCLIENT - " + message);
     } catch (IOException ioException)
     {
       chatWindow.append ("\n Error sending message");
     }
  }

  private void showMessage (String text)
   {
     SwingUtilities.invokeLater(
       new Runnable () 
       {
         public void run () 
         {
           chatWindow.append (text);
         }
       }
     );
   }

}