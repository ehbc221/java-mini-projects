import java.io.*;
//import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;
import javax.swing.*;
//import org.omg.CosNaming.*;
//import org.omg.CosNaming.NamingContextExtPackage.*;
//import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
//import java.lang.Integer;

public class FileClient
{
    //- Components and windows -------------------------------------
    private Label lblTo = new Label("To :");
    private Label lblFile = new Label("File :");
    private TextField txtTo = new TextField("");
    private TextField txtFile = new TextField("");
    private Button btnSend = new Button("Send");
    private Button btnAbout = new Button("About ...");
    private Button btnTo = new Button("To ...");
    private Button btnBrowse = new Button("Browse ...");
    private static Frame f = new Frame("File Transfert Application Client");
    
    private List list = new List();
    private Button btnOk = new Button("OK");
    private Button btnCancel = new Button("Cancel");
    private Frame flist = new Frame("Users List");

	static Connection myConnection=new Connection();
	private FileInterfaceServer objDistant = null;

	private String hostName = null;
	private String userString = null;
	private static FileClientImpl fci = null;
	private static FileInterfaceClient fic = null;
	private static FileClient fc = null;
	private POA poa = null;

    public FileClient()
    {
        f.setLayout(new BorderLayout());
        flist.setLayout(new BorderLayout());
        f.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent evt)
            {
            	unsubscription();
            }
        });

		JPanel left = new JPanel();
		JPanel center = new JPanel();
		JPanel right = new JPanel();
		left.setLayout(new BorderLayout());
		center.setLayout(new BorderLayout());
		right.setLayout(new BorderLayout());
		left.add(lblTo, BorderLayout.NORTH);
		left.add(lblFile, BorderLayout.CENTER);
		left.add(btnAbout, BorderLayout.SOUTH);
		center.add(txtTo, BorderLayout.NORTH);
		center.add(txtFile, BorderLayout.CENTER);
		TextField txtHide = new TextField("");
		txtHide.setEditable(false);
		txtHide.setBackground(center.getBackground());
		center.add(txtHide, BorderLayout.SOUTH);
		right.add(btnTo, BorderLayout.NORTH);
		right.add(btnBrowse, BorderLayout.CENTER);
		right.add(btnSend, BorderLayout.SOUTH);
		f.add(left, BorderLayout.WEST);
		f.add(center, BorderLayout.CENTER);
		f.add(right, BorderLayout.EAST);
		

		JPanel bottomlist = new JPanel();
		flist.add(list);
		bottomlist.add(btnOk, BorderLayout.WEST);
		bottomlist.add(btnCancel, BorderLayout.EAST);
		flist.add(bottomlist, BorderLayout.SOUTH);		

        btnSend.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
            	sendFile();
            }
        });

		btnTo.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				String[] userlist=objDistant.getUserList();
				flist.setVisible(true);
				f.setVisible(false);
				System.out.println("User List :");
				list.clear();
				for(int ind=0; ind<userlist.length; ind++)
				{
					list.add(userlist[ind]);
				} 
			}
		});
		
		btnOk.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				flist.setVisible(false);
				f.setVisible(true);
				txtTo.setText(list.getSelectedItem());
			}
		});
		
		btnCancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				flist.setVisible(false);
				f.setVisible(true);
			}
		});

		btnBrowse.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				JFileChooser chooser = new JFileChooser();
				int returnVal = chooser.showOpenDialog(f);
				if(returnVal == JFileChooser.APPROVE_OPTION) 
				{
				   System.out.println("You chose to open this file: " + chooser.getSelectedFile().getPath());
				   txtFile.setText(chooser.getSelectedFile().getPath());
				}
			}
		});

		btnAbout.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				JOptionPane.showMessageDialog(f, "File Transfert Application\nThis program is for files transfert between machines.\nWritten by V. Vinz\nE-Mail : vinvay@caramail.com");
			}
		});

        f.setSize(400,100);
        flist.setSize(200, 300);
        f.setResizable(false);
        flist.setResizable(false);
        f.setBounds(100, 100, 400, 100);
        f.setVisible(true);
        flist.setVisible(false);
        txtTo.setEditable(false);
        txtFile.setEditable(false);
    }

   public void subscription(String[] argv)
   {
          try
          {
				fci=new FileClientImpl(f);
						
				objDistant=myConnection.OpenConnection(argv);
				if(objDistant==null)
				{
					JOptionPane.showMessageDialog(f,"Connection to the server 'FileTransfertServer' failed !!");
					System.exit(0);
				}
				else JOptionPane.showMessageDialog(f,"Connected to the server 'FileTransfertServer' !!");

				poa=POAHelper.narrow(myConnection.orb.resolve_initial_references("RootPOA"));
				poa.the_POAManager().activate();
				org.omg.CORBA.Object ref=poa.servant_to_reference(fci);
				userString=myConnection.orb.object_to_string(ref);

			   	hostName=new String(InetAddress.getLocalHost().toString());
			   	
              	objDistant.subscribe(userString, hostName);
          }
          catch(Exception exc)
          {
				JOptionPane.showMessageDialog(f,exc.getMessage());
				System.exit(0);
          }
   }
   
   public void unsubscription()
   {
		objDistant.unsubscribe(hostName);
		System.exit(0);
   }
   
	FileInterfaceClient getUser(String userStringp)
	{
		FileInterfaceClient ficl=null;
		org.omg.CORBA.Object obj=myConnection.orb.string_to_object(userStringp);
		ficl=FileInterfaceClientHelper.narrow(obj);
		return ficl;
	}
	
	void sendFile()
	{
		String strTo = new String(txtTo.getText());
		String strFile = new String(txtFile.getText());
		File file = new File(strFile);
		if((strTo.compareTo("")==0)||(strFile.compareTo("")==0))
		{
			JOptionPane.showMessageDialog(f, "Fields 'To' and 'File' must be completed !!");
			return;
		}
		String userStringl=objDistant.getUserString(strTo);
		FileInterfaceClient user=getUser(userStringl);
		String fileName = new String(file.getName());

		boolean ok=user.askReceiveFile(hostName, fileName, (int)file.length());
		if(ok) 
		{
			if(file.length()<=fci.partMaxSize)
			{
				byte fileData[] = new byte[(int)file.length()];
				try
				{
					BufferedInputStream input = new
					   BufferedInputStream(new FileInputStream(strFile));
					input.read(fileData,0,fileData.length);
					input.close();
				}
				catch(Exception e)
				{
					System.out.println("Error when sending the file !!"+e.getMessage());
					e.printStackTrace();
				}
				user.receiveFile(hostName, fileName, fileData);
				System.out.println("File received !!");
			}
			else
			{
				int totalParts=(int)file.length()/fci.partMaxSize;
				totalParts=((totalParts*fci.partMaxSize)<(int)file.length())?(totalParts+1):totalParts;
				try
				{
					BufferedInputStream input = new
						BufferedInputStream(new FileInputStream(strFile));
					for(int partNumber=1; partNumber<=totalParts; partNumber++)
					{
						byte fileData[]=new byte[(partNumber*fci.partMaxSize>(int)file.length())?((int)file.length()-((partNumber-1)*fci.partMaxSize)):fci.partMaxSize];
						input.read(fileData, 0, fileData.length);
						System.out.println("Part #"+partNumber+"/"+totalParts);
						System.out.println("fileData.length="+fileData.length);
						user.receiveFilePart(hostName, fileName, (partNumber>1), fileData);
					}
					input.close();
				}
				catch(Exception e)
				{
					System.out.println("Error when sending the file !!"+e.getMessage());
					e.printStackTrace();
				}
				System.out.println("File received !!");
			}
		}
		else System.out.println("File refused !!");
	}
  
   public static void main(String[] argv) throws Exception
   {
         fc=new FileClient();
		//fci=new FileClientImpl(f);
         fc.subscription(argv);
   }
}