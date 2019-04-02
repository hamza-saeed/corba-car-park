import CarPark.*;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HQ extends JFrame {

    public static HQImplementation hqImpl = new HQImplementation();
    static NamingContextExt nameService;
    ScheduledExecutorService tableUpdater;

    public HQ() {

        //Load GUI
        initComponents();

        // Add listeners to JTables and initialise the DefaultTableModels
        addListenersAndInitialiseModels();
    }

    public static void main(String args[]) {

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HQ().setVisible(true);
            }
        });

        //set up client/server interaction
        setupClientServerConnections(args);
    }

    private void btnRefreshServersActionPerformed(java.awt.event.ActionEvent evt) {
        //clear previous entries
        serverModel.setRowCount(0);
        //load table will list of local servers
        for (Machine machine : hqImpl.listOfLocalServers) {
            serverModel.addRow(new String[]{machine.name});
        }

        //this updates the tables every 3 seconds
        tableUpdater = Executors.newScheduledThreadPool(1);
        tableUpdater.scheduleAtFixedRate(updateTables, 0, 3, TimeUnit.SECONDS);
    }

    private void btnToggleEntryActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            //if a value is selected in the JTable
            if (tableEntryGates.getSelectedRow() != -1) {

                //get the selected entry gate name
                String serverName = tableEntryGates.getValueAt(tableEntryGates.getSelectedRow(), 0).toString();

                //resolve the Entry Gate object reference in the naming service
                EntryGate entryRef = EntryGateHelper.narrow(nameService.resolve_str(serverName));

                //call the toggle enabled method
                entryRef.toggleEnabled();

                //call method to update all tables
                updateTables();

            } else {
                JOptionPane.showMessageDialog(null, "No selected entry gate");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Connection error", "Error", 0);
            e.printStackTrace();
        }
    }

    private void btnTogglePaystationActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            //if a value is selected in the JTable
            if (tablePayStations.getSelectedRow() != -1) {

                //get the selected paystation name
                String serverName = tablePayStations.getValueAt(tablePayStations.getSelectedRow(), 0).toString();

                //resolve the paystation object reference in the naming service
                PayStation payStationRef = PayStationHelper.narrow(nameService.resolve_str(serverName));

                //call the toggle enabled method
                payStationRef.toggleEnabled();

                //call method to update all tables
                updateTables();


            } else {
                JOptionPane.showMessageDialog(null, "No selected paystation");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Connection error", "Error", 0);
        }
    }

    private void btnToggleExitActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            //if a value is selected in the JTable
            if (tableExitGates.getSelectedRow() != -1) {

                //get the selected exit gate name
                String serverName = tableExitGates.getValueAt(tableExitGates.getSelectedRow(), 0).toString();

                //resolve the exit gate object reference in the naming service
                ExitGate exitRef = ExitGateHelper.narrow(nameService.resolve_str(serverName));

                //call the toggle enabled method
                exitRef.toggleEnabled();

                //call method to update all tables
                updateTables();
            } else {
                JOptionPane.showMessageDialog(null, "No selected exit gate");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Connection error", "Error", 0);
        }
    }


    private void btnResetEntryActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            //if a value is selected in the JTable
            if (tableEntryGates.getSelectedRow() != -1) {

                //get the selected entry gate name
                String serverName = tableEntryGates.getValueAt(tableEntryGates.getSelectedRow(), 0).toString();

                //resolve the entry gate object reference in the naming service
                EntryGate entryRef = EntryGateHelper.narrow(nameService.resolve_str(serverName));

                //call the toggle enabled method
                entryRef.reset();
                JOptionPane.showMessageDialog(null, "Entry gate has been reset");

                //call method to update all tables
                updateTables();
            } else {
                JOptionPane.showMessageDialog(null, "No selected entry gate");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Connection error", "Error", 0);
        }
    }

    private void btnResetPaystationActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            //if a value is selected in the JTable
            if (tablePayStations.getSelectedRow() != -1) {

                //get the selected paystation name
                String serverName = tablePayStations.getValueAt(tablePayStations.getSelectedRow(), 0).toString();

                //resolve the paystation object reference in the naming service
                PayStation payRef = PayStationHelper.narrow(nameService.resolve_str(serverName));

                //call the toggle enabled method
                payRef.reset();
                JOptionPane.showMessageDialog(null, "Pay station has been reset");

                //call method to update all tables
                updateTables();
            } else {
                JOptionPane.showMessageDialog(null, "No selected pay station gate");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Connection error", "Error", 0);
        }
    }

    private void btnResetExitActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            //if a value is selected in the JTable
            if (tableExitGates.getSelectedRow() != -1) {

                //get the selected exit gate name
                String serverName = tableExitGates.getValueAt(tableExitGates.getSelectedRow(), 0).toString();

                //resolve the exit gate object reference in the naming service
                ExitGate exitRef = ExitGateHelper.narrow(nameService.resolve_str(serverName));

                //call the toggle enabled method
                exitRef.reset();
                JOptionPane.showMessageDialog(null, "Exit gate has been reset");

                //call method to update all tables
                updateTables();
            } else {
                JOptionPane.showMessageDialog(null, "No selected exit gate");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Connection error", "Error", 0);
        }
    }


    private void tableServersSelectionChange(javax.swing.event.ListSelectionEvent evt) {
        if (evt.getValueIsAdjusting()) {
            //reset cash total
            lblPaystationCashTotal.setText("Cash Total Today: £--");
            //update all tables method
            updateTables();
        }
    }

    private void tablePaystationsSelectionChange(javax.swing.event.ListSelectionEvent evt) {
        try {
            if (evt.getValueIsAdjusting()) {
                //if value is selected in table
                if (tablePayStations.getSelectedRow() != -1) {

                    //get the selected paystation name
                    String serverName = tablePayStations.getValueAt(tablePayStations.getSelectedRow(), 0).toString();

                    //resolve the paystation object reference in the naming service
                    PayStation payStationRef = PayStationHelper.narrow(nameService.resolve_str(serverName));

                    //Show total cash today from selected paystation
                    lblPaystationCashTotal.setText("Cash Total Today: £" + payStationRef.return_cash_total());
                } else {
                    lblPaystationCashTotal.setText("Cash Total Today: £--");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Connection error", "Error", 0);
        }
    }

    private void btnChangePriceActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            //if value is selected in table
            if (tableServers.getSelectedRow() != -1) {

                //get the selected localserver name
                String serverName = tableServers.getValueAt(tableServers.getSelectedRow(), 0).toString();

                //resolve the localserver object reference in the naming service
                LocalServer lServerRef = LocalServerHelper.narrow(nameService.resolve_str(serverName));
                try {
                    //parse the new price input
                    double newPrice = Double.parseDouble(txtNewPrice.getText());
                    //update cost
                    lServerRef.setCost(newPrice);
                    //update the table
                    updateTables();
                    JOptionPane.showMessageDialog(null, "Price updated.");
                } catch (Exception e2) {
                    //if input was not a double
                    JOptionPane.showMessageDialog(null, "Error. Input price is invalid.");
                    return;
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Connection error", "Error", 0);
        }
    }


    public void updateTables() {
        try {
            //if value is selected
            if (tableServers.getSelectedRow() != -1) {

                //get the selected localserver name
                String serverName = tableServers.getValueAt(tableServers.getSelectedRow(), 0).toString();

                //resolve the localserver object reference in the naming service
                LocalServer lServerRef = LocalServerHelper.narrow(nameService.resolve_str(serverName));

                //clear previous entries in table
                entryModel.setRowCount(0);
                //add all entry models
                for (Machine machine : lServerRef.listOfEntryGates()) {
                    entryModel.addRow(new String[]{machine.name, machine.enabled ? "Yes" : "No"});
                }

                payStationModel.setRowCount(0);
                //add all paystations
                for (Machine machine : lServerRef.listOfPayStations()) {
                    payStationModel.addRow(new String[]{machine.name, machine.enabled ? "Yes" : "No"});
                }

                exitModel.setRowCount(0);
                //add all exit gates
                for (Machine machine : lServerRef.listOfExitGates()) {
                    exitModel.addRow(new String[]{machine.name, machine.enabled ? "Yes" : "No"});
                }

                carModel.setRowCount(0);
                //add all cars currently in selected carpark to the car table
                for (ParkingTransaction parkingTrans : lServerRef.log()) {
                    if (parkingTrans.event != EventType.Exited) {
                        carModel.addRow(new String[]{parkingTrans.registration_number, parkingTrans.event == EventType.Paid ? "Paid" : "Entered"});
                    }
                }

                alertModel.setRowCount(0);
                //add all alerts to the alert table
                for (ParkingTransaction parkingTrans : hqImpl.listOfAlerts) {
                    alertModel.addRow(new String[]{parkingTrans.registration_number, parkingTrans.alert});
                }

                //update the total cash earned today label
                lblServerCashTotal.setText("Cash Total Today: £" + lServerRef.return_cash_total());
                //update the spaces remaining label
                lblSpacesLeft.setText(lServerRef.returnAvailableSpaces() + "/" + lServerRef.returnNumberofSpaces());
                //update the current price label
                lblPrice.setText("£" + lServerRef.getCost());
            } else {
                lblServerCashTotal.setText("Cash Total Today: £--");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Connection error", "Error", 0);
        }
    }


    public static void setupClientServerConnections(String args[]) {

        try {
            // create and initialize the ORB
            ORB orb = ORB.init(args, null);

            // get reference to rootpoa & activate the POAManager
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            //get reference to naming service
            org.omg.CORBA.Object nameServiceObject = orb.resolve_initial_references("NameService");
            if (nameServiceObject == null) {
                System.out.println("nameServiceObject = null");
            }
            nameService = NamingContextExtHelper.narrow(nameServiceObject);
            if (nameService == null) {
                System.out.println("nameService=null");
            }

            //get object reference from the servant
            org.omg.CORBA.Object hqRef = rootpoa.servant_to_reference(hqImpl);
            HQServer cref = HQServerHelper.narrow(hqRef);

            //bind the object in the naming service - can be hardcoded as only 1 HQ
            NameComponent[] hqName = nameService.to_name("HQConn");
            nameService.rebind(hqName, cref);

            //  wait for invocations from clients
            //orb.run();

        } catch (Exception e) {
            //can't connect to server
            JOptionPane.showMessageDialog(null, "Connection error", "Error", 0);
            System.exit(1);
        }

    }

    //Runnable to update tables every 3 seconds.
    Runnable updateTables = new Runnable() {
        @Override
        public void run() {
            updateTables();
        }
    };

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        javax.swing.JPanel Background = new javax.swing.JPanel();
        javax.swing.JPanel jPanel2 = new javax.swing.JPanel();
        javax.swing.JPanel jPanel7 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel6 = new javax.swing.JLabel();
        javax.swing.JPanel jPanel3 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        javax.swing.JPanel jPanel1 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel2 = new javax.swing.JLabel();
        javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        tableServers = new javax.swing.JTable();
        javax.swing.JButton btnRefreshServers = new javax.swing.JButton();
        lblServerCashTotal = new javax.swing.JLabel();
        javax.swing.JPanel jPanel4 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel3 = new javax.swing.JLabel();
        javax.swing.JScrollPane jScrollPane2 = new javax.swing.JScrollPane();
        tableEntryGates = new javax.swing.JTable();
        javax.swing.JButton btnToggleEntry = new javax.swing.JButton();
        javax.swing.JButton btnResetEntry = new javax.swing.JButton();
        javax.swing.JPanel jPanel5 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel4 = new javax.swing.JLabel();
        javax.swing.JScrollPane jScrollPane3 = new javax.swing.JScrollPane();
        tablePayStations = new javax.swing.JTable();
        lblPaystationCashTotal = new javax.swing.JLabel();
        javax.swing.JButton btnTogglePaystation = new javax.swing.JButton();
        javax.swing.JButton btnResetPaystation = new javax.swing.JButton();
        javax.swing.JPanel jPanel6 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel5 = new javax.swing.JLabel();
        javax.swing.JScrollPane jScrollPane4 = new javax.swing.JScrollPane();
        tableExitGates = new javax.swing.JTable();
        javax.swing.JButton btnToggleExit = new javax.swing.JButton();
        javax.swing.JButton btnResetExit = new javax.swing.JButton();
        javax.swing.JPanel jPanel8 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel8 = new javax.swing.JLabel();
        javax.swing.JScrollPane jScrollPane5 = new javax.swing.JScrollPane();
        tableCars = new javax.swing.JTable();
        javax.swing.JLabel jLabel9 = new javax.swing.JLabel();
        lblSpacesLeft = new javax.swing.JLabel();
        javax.swing.JPanel jPanel9 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel7 = new javax.swing.JLabel();
        javax.swing.JScrollPane jScrollPane6 = new javax.swing.JScrollPane();
        tableAlerts = new javax.swing.JTable();
        javax.swing.JPanel jPanel10 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel10 = new javax.swing.JLabel();
        javax.swing.JLabel jLabel11 = new javax.swing.JLabel();
        lblPrice = new javax.swing.JLabel();
        javax.swing.JLabel jLabel12 = new javax.swing.JLabel();
        txtNewPrice = new javax.swing.JTextField();
        javax.swing.JLabel jLabel13 = new javax.swing.JLabel();
        javax.swing.JButton btnChangePrice = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        Background.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(22, 160, 134));

        jPanel7.setBackground(new java.awt.Color(22, 186, 136));

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Connected");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel6)
                                .addContainerGap(31, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel6)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(128, 128, 128)
                                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(22, 186, 136));

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Headquarters");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(76, 76, 76)
                                .addComponent(jLabel1)
                                .addContainerGap(663, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(41, 41, 41)
                                .addComponent(jLabel1)
                                .addContainerGap(44, Short.MAX_VALUE))
        );

        jLabel2.setText("Connected Servers");

        tableServers.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                        "Server"
                }
        ) {
            Class[] types = new Class[]{
                    java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                    false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane1.setViewportView(tableServers);

        btnRefreshServers.setText("Refresh Servers");
        btnRefreshServers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshServersActionPerformed(evt);
            }
        });

        lblServerCashTotal.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        lblServerCashTotal.setText("Cash Total Today: £--");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addGap(41, 41, 41)
                                                                .addComponent(jLabel2))
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addGap(35, 35, 35)
                                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                                .addGap(12, 12, 12)
                                                                                .addComponent(lblServerCashTotal))
                                                                        .addComponent(btnRefreshServers))))
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel2)
                                .addGap(12, 12, 12)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnRefreshServers)
                                .addGap(12, 12, 12)
                                .addComponent(lblServerCashTotal)
                                .addContainerGap(36, Short.MAX_VALUE))
        );

        jLabel3.setText("Connected Entry Gates");

        tableEntryGates.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                        "Entry Gate", "Enabled"
                }
        ) {
            Class[] types = new Class[]{
                    java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                    false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane2.setViewportView(tableEntryGates);

        btnToggleEntry.setText("Toggle On/Off");
        btnToggleEntry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToggleEntryActionPerformed(evt);
            }
        });

        btnResetEntry.setText("Reset");
        btnResetEntry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetEntryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addGap(30, 30, 30)
                                                .addComponent(jLabel3)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                                .addGap(0, 11, Short.MAX_VALUE)
                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(45, 45, 45)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(btnToggleEntry, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnResetEntry, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnToggleEntry)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnResetEntry)
                                .addContainerGap(32, Short.MAX_VALUE))
        );

        jLabel4.setText("Connected Paystations");

        tablePayStations.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                        "Paystation", "Enabled"
                }
        ) {
            Class[] types = new Class[]{
                    java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                    false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane3.setViewportView(tablePayStations);

        lblPaystationCashTotal.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        lblPaystationCashTotal.setText("Cash Total Today: £--");

        btnTogglePaystation.setText("Toggle On/Off");
        btnTogglePaystation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTogglePaystationActionPerformed(evt);
            }
        });

        btnResetPaystation.setText("Reset");
        btnResetPaystation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetPaystationActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                .addGap(30, 30, 30)
                                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel4)
                                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                                .addGap(14, 14, 14)
                                                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(btnTogglePaystation)
                                                                        .addComponent(btnResetPaystation, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(lblPaystationCashTotal, javax.swing.GroupLayout.Alignment.TRAILING)))))
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel4)
                                .addGap(12, 12, 12)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnTogglePaystation)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnResetPaystation)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblPaystationCashTotal)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel5.setText("Connected Exit Gates");

        tableExitGates.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                        "Exit Gate", "Enabled"
                }
        ) {
            Class[] types = new Class[]{
                    java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                    false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane4.setViewportView(tableExitGates);

        btnToggleExit.setText("Toggle On/Off");
        btnToggleExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToggleExitActionPerformed(evt);
            }
        });

        btnResetExit.setText("Reset");
        btnResetExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetExitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addGap(30, 30, 30)
                                                .addComponent(jLabel5))
                                        .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addGap(44, 44, 44)
                                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(btnToggleExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(btnResetExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnToggleExit)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnResetExit)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel8.setText("Cars Currently in Selected Car Park");

        tableCars.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                        "Car Reg", "Status"
                }
        ) {
            Class[] types = new Class[]{
                    java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                    false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane5.setViewportView(tableCars);

        jLabel9.setText("Spaces Available: ");

        lblSpacesLeft.setText("[Spaces]");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel8Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel8Layout.createSequentialGroup()
                                                                .addGap(83, 83, 83))
                                                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(jPanel8Layout.createSequentialGroup()
                                                .addGap(33, 33, 33)
                                                .addComponent(jLabel8))
                                        .addGroup(jPanel8Layout.createSequentialGroup()
                                                .addGap(59, 59, 59)
                                                .addComponent(jLabel9)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(lblSpacesLeft)))
                                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel8)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel9)
                                        .addComponent(lblSpacesLeft))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel8.getAccessibleContext().setAccessibleName("Cars Currently in Car Park");

        jLabel7.setText("Alerts currently in Selected Car Park");

        tableAlerts.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                        "Car Reg", "Alert"
                }
        ) {
            Class[] types = new Class[]{
                    java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                    false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane6.setViewportView(tableAlerts);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
                jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGap(95, 95, 95))
                        .addGroup(jPanel9Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                        .addGroup(jPanel9Layout.createSequentialGroup()
                                                .addGap(17, 17, 17)
                                                .addComponent(jLabel7)
                                                .addGap(0, 26, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
                jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel9Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Change Price Per Hour");

        jLabel11.setText("Current Price:");

        lblPrice.setText("[Price]");

        jLabel12.setText("New Price:");

        jLabel13.setText("£");

        btnChangePrice.setText("Change Price");
        btnChangePrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangePriceActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
                jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel10Layout.createSequentialGroup()
                                .addContainerGap(35, Short.MAX_VALUE)
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                                                .addComponent(btnChangePrice)
                                                .addGap(36, 36, 36))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel10)
                                                        .addGroup(jPanel10Layout.createSequentialGroup()
                                                                .addComponent(jLabel12)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(jLabel13)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(txtNewPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(jPanel10Layout.createSequentialGroup()
                                                                .addComponent(jLabel11)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(lblPrice)))
                                                .addGap(24, 24, 24))))
        );
        jPanel10Layout.setVerticalGroup(
                jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel10Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel11)
                                        .addComponent(lblPrice))
                                .addGap(24, 24, 24)
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel12)
                                        .addComponent(txtNewPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel13))
                                .addGap(30, 30, 30)
                                .addComponent(btnChangePrice)
                                .addGap(25, 25, 25))
        );

        javax.swing.GroupLayout BackgroundLayout = new javax.swing.GroupLayout(Background);
        Background.setLayout(BackgroundLayout);
        BackgroundLayout.setHorizontalGroup(
                BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(BackgroundLayout.createSequentialGroup()
                                .addGroup(BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(BackgroundLayout.createSequentialGroup()
                                                .addGap(73, 73, 73)
                                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(BackgroundLayout.createSequentialGroup()
                                                .addGap(370, 370, 370)
                                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(BackgroundLayout.createSequentialGroup()
                                                .addGap(138, 138, 138)
                                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(BackgroundLayout.createSequentialGroup()
                                                .addGroup(BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addGroup(BackgroundLayout.createSequentialGroup()
                                                                .addGap(602, 602, 602)
                                                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(BackgroundLayout.createSequentialGroup()
                                                                .addGroup(BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(BackgroundLayout.createSequentialGroup()
                                                                                .addGap(138, 138, 138)
                                                                                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGap(15, 15, 15)
                                                .addGroup(BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        BackgroundLayout.setVerticalGroup(
                BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(BackgroundLayout.createSequentialGroup()
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(BackgroundLayout.createSequentialGroup()
                                                .addGap(22, 22, 22)
                                                .addGroup(BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addGroup(BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(BackgroundLayout.createSequentialGroup()
                                                        .addGap(22, 22, 22)
                                                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, BackgroundLayout.createSequentialGroup()
                                                        .addGap(24, 24, 24)
                                                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(9, 9, 9)
                                .addGroup(BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(Background, javax.swing.GroupLayout.PREFERRED_SIZE, 1075, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(Background, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>


    public void addListenersAndInitialiseModels() {
        tableServers.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                tableServersSelectionChange(e);
            }
        });

        tablePayStations.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                tablePaystationsSelectionChange(e);
            }
        });
        serverModel = (DefaultTableModel) tableServers.getModel();
        entryModel = (DefaultTableModel) tableEntryGates.getModel();
        payStationModel = (DefaultTableModel) tablePayStations.getModel();
        exitModel = (DefaultTableModel) tableExitGates.getModel();
        carModel = (DefaultTableModel) tableCars.getModel();
        alertModel = (DefaultTableModel) tableAlerts.getModel();

        tableServers.getTableHeader().setReorderingAllowed(false);
        tableEntryGates.getTableHeader().setReorderingAllowed(false);
        tablePayStations.getTableHeader().setReorderingAllowed(false);
        tableExitGates.getTableHeader().setReorderingAllowed(false);
        tableCars.getTableHeader().setReorderingAllowed(false);
        tableAlerts.getTableHeader().setReorderingAllowed(false);
    }


    static javax.swing.JLabel lblServerCashTotal;
    static javax.swing.JLabel lblPaystationCashTotal;
    static javax.swing.JLabel lblSpacesLeft;
    static javax.swing.JLabel lblPrice;
    JTextField txtNewPrice;
    javax.swing.JTable tableServers;
    javax.swing.JTable tableEntryGates;
    javax.swing.JTable tablePayStations;
    javax.swing.JTable tableExitGates;
    javax.swing.JTable tableCars;
    javax.swing.JTable tableAlerts;
    DefaultTableModel serverModel;
    DefaultTableModel entryModel;
    DefaultTableModel payStationModel;
    DefaultTableModel exitModel;
    DefaultTableModel carModel;
    DefaultTableModel alertModel;

}

