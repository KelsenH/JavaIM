/*
 JAVA IM
 Kelsen Hazelwood 
 **/


import java.io.*;
import java.net.*;
import java.security.KeyStore.TrustedCertificateEntry;
import java.io.BufferedReader;

 public class Server
 {
   private String userText;
   //private JTextField userText;
   //private JTextArea chatWindow;
   private ObjectOutputStream output;
   private ObjectInputStream input;
   private ServerSocket server;
   private Socket connection; 

   //Constructor
   public Server ()
   {
     
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
     String ip = InetAddress.getLocalHost().getHostAddress();
     showMessage ("Waiting for connection . . . \n" + ip + "\n" + server.getLocalPort() + "\n");
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
     String message = "";
     String myText = "";
     BufferedReader reader = new BufferedReader(new InputStreamReader (System.in));

     do {
       try 
       {
         message = (String)input.readObject();
         showMessage ("\n" + message);
         myText = reader.readLine();
         System.in.skip(100);
         if (myText != "")
         {
           sendMessage(myText);
         }
       } catch (ClassNotFoundException classNotFoundException)
       {
         showMessage ("\n Invalid Input");
       }
     } while (!message.equals ("CLIENT - END"));
   }

   private void endProgram () 
   {
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
       showMessage ("\n Error sending message");
     }
   }

   private void showMessage (String text)
   {
     System.out.println(text);
   } 
 }