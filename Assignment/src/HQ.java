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

public class HQ extends JFrame {

    public static HQImplementation hqImpl = new HQImplementation();
    static NamingContextExt nameService;

    public HQ() {
        initComponents();
        addListeners();
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

    public static void main(String args[]) {

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HQ().setVisible(true);
            }
        });
        setupClientServerConnections(args);
    }

    private void btnRefreshServersActionPerformed(java.awt.event.ActionEvent evt) {
        serverModel.setRowCount(0);
        for (Machine machine : hqImpl.listOfLocalServers) {
            serverModel.addRow(new String[]{machine.name});
        }
    }

    private void btnToggleEntryActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (tableEntryGates.getSelectedRow() != -1) {
                String serverName = tableEntryGates.getValueAt(tableEntryGates.getSelectedRow(), 0).toString();
                EntryGate entryRef = EntryGateHelper.narrow(nameService.resolve_str(serverName));

                entryRef.toggleEnabled();
                updateTables();

            } else {
                JOptionPane.showMessageDialog(null, "No selected entry gate");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void btnTogglePaystationActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (tablePayStations.getSelectedRow() != -1) {
                String serverName = tablePayStations.getValueAt(tablePayStations.getSelectedRow(), 0).toString();
                PayStation payStationRef = PayStationHelper.narrow(nameService.resolve_str(serverName));

                payStationRef.toggleEnabled();
                updateTables();


            } else {
                JOptionPane.showMessageDialog(null, "No selected paystation");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void btnToggleExitActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (tableExitGates.getSelectedRow() != -1) {
                String serverName = tableExitGates.getValueAt(tableExitGates.getSelectedRow(), 0).toString();
                ExitGate exitRef = ExitGateHelper.narrow(nameService.resolve_str(serverName));
                exitRef.toggleEnabled();
                updateTables();
            } else {
                JOptionPane.showMessageDialog(null, "No selected exit gate");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void tableServersSelectionChange(javax.swing.event.ListSelectionEvent evt) {
        if (evt.getValueIsAdjusting()) {
            lblPaystationCashTotal.setText("");
            updateTables();
        }
    }

    private void tablePaystationsSelectionChange(javax.swing.event.ListSelectionEvent evt) {
        try {
            if (evt.getValueIsAdjusting()) {
                if (tablePayStations.getSelectedRow() != -1) {
                    String serverName = tablePayStations.getValueAt(tablePayStations.getSelectedRow(), 0).toString();
                    PayStation payStationRef = PayStationHelper.narrow(nameService.resolve_str(serverName));
                    lblPaystationCashTotal.setText("Cash Total Today: £" + payStationRef.return_cash_total());
                } else {
                    lblPaystationCashTotal.setText("");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void btnRefreshAlertsActionPerformed(java.awt.event.ActionEvent evt) {
        updateTables();
    }

    private void btnRefreshCarsActionPerformed(java.awt.event.ActionEvent evt) {
        updateTables();
    }

    private void btnChangePriceActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (tableServers.getSelectedRow() != -1) {
                String serverName = tableServers.getValueAt(tableServers.getSelectedRow(), 0).toString();
                LocalServer lServerRef = LocalServerHelper.narrow(nameService.resolve_str(serverName));
                try {
                    double newPrice = Double.parseDouble(txtNewPrice.getText());
                    lServerRef.setCost(newPrice);
                    updateTables();
                    JOptionPane.showMessageDialog(null, "Price updated.");
                } catch (Exception e2)
                {
                    JOptionPane.showMessageDialog(null, "Error. Input price is invalid.");
                    return;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void updateTables() {
        try {
            if (tableServers.getSelectedRow() != -1) {
                String serverName = tableServers.getValueAt(tableServers.getSelectedRow(), 0).toString();
                LocalServer lServerRef = LocalServerHelper.narrow(nameService.resolve_str(serverName));
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
                for (ParkingTransaction parkingTrans : lServerRef.log())
                {
                    if (parkingTrans.event != EventType.Exited)
                    {
                        carModel.addRow(new String[]{parkingTrans.registration_number, parkingTrans.event == EventType.Paid ? "Paid" : "Entered"});
                    }
                }

                alertModel.setRowCount(0);
                for (ParkingTransaction parkingTrans : hqImpl.listOfAlerts) {
                    alertModel.addRow(new String[] {parkingTrans.registration_number, parkingTrans.alert});
                }

                lblServerCashTotal.setText("Cash Total Today: £" + lServerRef.return_cash_total());
                lblSpacesLeft.setText(lServerRef.returnAvailableSpaces()  + "/" + lServerRef.returnNumberofSpaces());
                lblPrice.setText("£" + lServerRef.getCost());
            } else {
                lblServerCashTotal.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void setupClientServerConnections(String args[]) {

        try {
            ORB orb = ORB.init(args, null);

            org.omg.CORBA.Object nameServiceObject = orb.resolve_initial_references("NameService");
            if (nameServiceObject == null) {
                System.out.println("nameServiceObject = null");
            }

            nameService = NamingContextExtHelper.narrow(nameServiceObject);
            if (nameService == null) {
                System.out.println("nameService=null");
            }

            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();


            org.omg.CORBA.Object hqRef = rootpoa.servant_to_reference(hqImpl);
            HQServer cref = HQServerHelper.narrow(hqRef);
            NameComponent[] hqName = nameService.to_name("HQConn");
            nameService.rebind(hqName, cref);
            System.out.println("HQ started...");
            orb.run();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

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
        javax.swing.JPanel jPanel5 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel4 = new javax.swing.JLabel();
        javax.swing.JScrollPane jScrollPane3 = new javax.swing.JScrollPane();
        tablePayStations = new javax.swing.JTable();
        javax.swing.JButton btnTogglePaystation = new javax.swing.JButton();
        lblPaystationCashTotal = new javax.swing.JLabel();
        javax.swing.JPanel jPanel6 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel5 = new javax.swing.JLabel();
        javax.swing.JScrollPane jScrollPane4 = new javax.swing.JScrollPane();
        tableExitGates = new javax.swing.JTable();
        javax.swing.JButton btnToggleExit = new javax.swing.JButton();
        javax.swing.JPanel jPanel8 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel8 = new javax.swing.JLabel();
        javax.swing.JScrollPane jScrollPane5 = new javax.swing.JScrollPane();
        tableCars = new javax.swing.JTable();
        javax.swing.JButton btnRefreshCars = new javax.swing.JButton();
        javax.swing.JLabel jLabel9 = new javax.swing.JLabel();
        lblSpacesLeft = new javax.swing.JLabel();
        javax.swing.JPanel jPanel9 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel7 = new javax.swing.JLabel();
        javax.swing.JScrollPane jScrollPane6 = new javax.swing.JScrollPane();
        tableAlerts = new javax.swing.JTable();
        javax.swing.JButton btnRefreshAlerts = new javax.swing.JButton();
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

        lblServerCashTotal.setText("");

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
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(lblServerCashTotal)
                                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                        .addGap(41, 41, 41)
                                                                        .addComponent(jLabel2))
                                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                        .addGap(35, 35, 35)
                                                                        .addComponent(btnRefreshServers))))
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnRefreshServers)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblServerCashTotal)
                                .addContainerGap(22, Short.MAX_VALUE))
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

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addGap(30, 30, 30)
                                                .addComponent(jLabel3))
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addGap(44, 44, 44)
                                                .addComponent(btnToggleEntry)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnToggleEntry)
                                .addContainerGap(49, Short.MAX_VALUE))
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

        btnTogglePaystation.setText("Toggle On/Off");
        btnTogglePaystation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTogglePaystationActionPerformed(evt);
            }
        });

        lblPaystationCashTotal.setText("");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap())
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                                .addComponent(btnTogglePaystation)
                                                .addGap(44, 44, 44))))
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel4)
                                        .addComponent(lblPaystationCashTotal))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnTogglePaystation)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblPaystationCashTotal)
                                .addContainerGap(22, Short.MAX_VALUE))
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
                                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGap(45, 45, 45)
                                        .addComponent(btnToggleExit)
                                        .addContainerGap(45, Short.MAX_VALUE)))
        );
        jPanel6Layout.setVerticalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(74, Short.MAX_VALUE))
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGap(152, 152, 152)
                                        .addComponent(btnToggleExit)
                                        .addContainerGap(42, Short.MAX_VALUE)))
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

        btnRefreshCars.setText("Refresh Cars");
        btnRefreshCars.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshCarsActionPerformed(evt);
            }
        });

        jLabel9.setText("Spaces Available: ");

        lblSpacesLeft.setText("[Spaces]");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel8Layout.createSequentialGroup()
                                                .addGap(54, 54, 54)
                                                .addComponent(jLabel8))
                                        .addGroup(jPanel8Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                                                                .addComponent(btnRefreshCars, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(6, 6, 6)
                                                                .addComponent(jLabel9)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(lblSpacesLeft))
                                                        .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel8)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnRefreshCars)
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

        btnRefreshAlerts.setText("Refresh Alerts");
        btnRefreshAlerts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshAlertsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
                jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnRefreshAlerts)
                                .addGap(95, 95, 95))
                        .addGroup(jPanel9Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel7))
                                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
                jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel9Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnRefreshAlerts)
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
                                .addContainerGap(32, Short.MAX_VALUE)
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
                                                .addGap(25, 25, 25))))
        );
        jPanel10Layout.setVerticalGroup(
                jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel10Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
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
                                                .addGap(510, 510, 510)
                                                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(BackgroundLayout.createSequentialGroup()
                                                .addGap(370, 370, 370)
                                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(BackgroundLayout.createSequentialGroup()
                                                .addGap(138, 138, 138)
                                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(BackgroundLayout.createSequentialGroup()
                                                .addGap(602, 602, 602)
                                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(BackgroundLayout.createSequentialGroup()
                                                .addGap(138, 138, 138)
                                                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(BackgroundLayout.createSequentialGroup()
                                                .addGap(835, 835, 835)
                                                .addGroup(BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        BackgroundLayout.setVerticalGroup(
                BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(BackgroundLayout.createSequentialGroup()
                                .addGroup(BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(BackgroundLayout.createSequentialGroup()
                                                .addGroup(BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addGroup(BackgroundLayout.createSequentialGroup()
                                                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(22, 22, 22)
                                                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(BackgroundLayout.createSequentialGroup()
                                                                .addGap(15, 15, 15)
                                                                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                                        .addGroup(BackgroundLayout.createSequentialGroup()
                                                                .addGap(17, 17, 17)
                                                                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
                                        .addGroup(BackgroundLayout.createSequentialGroup()
                                                .addGap(150, 150, 150)
                                                .addGroup(BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(17, 17, 17)
                                                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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


    public void addListeners() {
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

    }


    public static javax.swing.JLabel lblServerCashTotal;
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

