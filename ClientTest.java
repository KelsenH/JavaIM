import javax.swing.JFrame;

public class ClientTest 
{
  public static void main (String [] args)
  {
    Client test = new Client ("192.168.122.1");
    test.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
    test.startRunning ();
  }
}//
