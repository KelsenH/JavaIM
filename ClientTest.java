import javax.swing.JFrame;

public class ClientTest 
:{
  public static void main (String [] args)
  {
    Client test = new Client ("134.68.51.14");
    test.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
    test.startRunning ();
  }
}//
