/*
 JAVA IM
 Kelsen Hazelwood 
 **/


import java.io.*;
import java.awt.*;
import java.net.*;
import java.security.KeyStore.TrustedCertificateEntry;
import java.awt.event.*;
import javax.swing.*;

 public class Server extends JFrame
 {

   private JTextField userText;
   private JTextArea chatWindow;
   private ObjectOutputStream output;
   private ObjectInputStream input;
   private ServerSocket server;
   private Socket connection; 

   //Constructor
   public Server ()
   {
     super ("Instant Messenger");
     userText = new JTextField ();
     userText.setEditable (false);
     userText.addActionListener ( 
       new ActionListener()
       {
         public void actionPerformed (ActionEvent event)
         {
           sendMessage (event.getActionCommand());
           userText.setText("");
         }
       }
     );
     add (userText, BorderLayout.SOUTH);
     chatWindow = new JTextArea();
     chatWindow.setEditable (false);
     add (new JScrollPane(chatWindow));
     setSize (500, 500);
     setVisible (true);
   }

   public void startRunning ()
   {
     try 
     {
       server = new ServerSocket (1234, 100);
       while (true)
       {
         try 
         {
           waitForConnection ();
           setupStreams ();
           whileChatting ();
         } catch (EOFException eofException)
         {
           showMessage ("/n Connection Terminated");
         } finally 
         {
           endProgram ();
         }
       }
     } catch (IOException ioException)
     {
       ioException.printStackTrace();
     }
   }

   private void waitForConnection () throws IOException
   {
     showMessage ("Waiting for connection . . . \n");
     connection = server.accept ();
     showMessage ("Now connected to " + connection.getInetAddress().getHostName() + "\n");
   }

   private void setupStreams () throws IOException
   {
     output = new ObjectOutputStream (connection.getOutputStream());
     output.flush();
     input = new ObjectInputStream (connection.getInputStream());
   }

   private void whileChatting () throws IOException
   {
     userText.setEditable (true);
     String message = "";
     do {
       try 
       {
         message = (String)input.readObject();
         showMessage ("\n" + message);
       } catch (ClassNotFoundException classNotFoundException)
       {
         showMessage ("\n Invalid Input");
       }
     } while (!message.equals ("CLIENT - END"));
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
       output.writeObject ("SERVER - " + message);
       output.flush ();
       showMessage ("\nSERVER - " + message);
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