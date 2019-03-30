import CarPark.*;
import org.omg.CORBA.*;
import org.omg.CosNaming.*;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

public class EntryClient extends JFrame {

    static EntryGateImpl entryImpl = new EntryGateImpl();

    private static EntryGate entry;

    public EntryClient(){

        //Create GUI
        initComponents();
    }

    public static void main(String args[])
    {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EntryClient().setVisible(true);
            }
        });

        setupClientServerConnections(args);
    }

    private void btnEnterMouseClicked(java.awt.event.MouseEvent evt) {
        LocalDateTime a = LocalDateTime.now();
        CarPark.Date date = new CarPark.Date();
        Time time = new Time();
        date.day = a.getDayOfMonth();
        date.month = a.getMonth().getValue();
        date.year = a.getYear();
        time.hr = a.getHour();
        time.min = a.getMinute();
        time.sec = a.getSecond();

        entry.car_entered(txtReg.getText(),date,time);

    }

    private static void setupClientServerConnections(String args[])
    {
        try {

            //initialising ORB
            ORB orb = ORB.init(args, null);

            //get reference to rootpoa & activate the POAManager
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            //Get a reference to the Naming service
            org.omg.CORBA.Object nameServiceObj = orb.resolve_initial_references("NameService");
            if (nameServiceObj == null) {
                System.out.println("nameServiceObj = null");
                return;
            }

            NamingContextExt nameServiceClients = NamingContextExtHelper.narrow(nameServiceObj);

            //only continue if the name service references are not null
            if (nameServiceClients == null) {
                System.out.println("nameServiceClients = null");
                return;
            }
            NamingContextExt nameServiceServer = NamingContextExtHelper.narrow(nameServiceObj);
            if (nameServiceServer == null) {
                System.out.println("nameServiceServer = null");
                return;
            }

            /* Make the entry gate client into a server
            so that the HQ can connect to it. */

            // get object reference from the servant
            org.omg.CORBA.Object hqref = rootpoa.servant_to_reference(HQ.hqimp);
            HQServer hqCref = HQServerHelper.narrow(hqref);

            //get gate name from the arguments
            String gateName = getArgs(args,"-Name");


            NameComponent[] hqName = nameServiceClients.to_name(gateName + "HQCon");
            nameServiceClients.rebind(hqName, hqCref);
            EntryGateImpl.EntryGateName = gateName;

            /*Make the entry client into a client
            so that it can connect to the LocalServer. */

            // create servant and register it with the ORB
            entry = EntryGateHelper.narrow(nameServiceServer.resolve_str(gateName));

            // Get the 'stringified IOR'
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(entryImpl);
            String stringified_ior =
                    orb.object_to_string(ref);

            //register gate with localServer
            entry.registerGate(gateName, stringified_ior);

            //  wait for invocations from clients
            orb.run();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static String getArgs(String[] args, String var) {
        //gets argument corresponding to string 'var'
        for (int i = 0; i < args.length; i++) {
            String param = args[i];
            if (param.toLowerCase().equals(var.toLowerCase())) {
                return args[i + 1];
            }
        }
        return "Unnamed";
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        background = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        lblWarning = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtReg = new javax.swing.JTextField();
        btnEnter = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        background.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(22, 160, 134));

        jPanel4.setBackground(new java.awt.Color(22, 186, 136));

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Entry");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(62, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel3)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(97, 97, 97)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(154, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(22, 186, 136));

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Entry Gate");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(83, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        lblWarning.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        lblWarning.setForeground(new java.awt.Color(255, 51, 51));

        jLabel2.setText("Vehicle Registration:");

        btnEnter.setText("Enter");
        btnEnter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEnterMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(322, 322, 322)
                                .addComponent(lblWarning, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(37, 37, 37)
                                .addComponent(jLabel2)
                                .addGap(56, 56, 56)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btnEnter)
                                        .addComponent(txtReg, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(102, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(59, 59, 59)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtReg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel2))
                                .addGap(18, 18, 18)
                                .addComponent(lblWarning)
                                .addGap(29, 29, 29)
                                .addComponent(btnEnter)
                                .addContainerGap(60, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout backgroundLayout = new javax.swing.GroupLayout(background);
        background.setLayout(backgroundLayout);
        backgroundLayout.setHorizontalGroup(
                backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(backgroundLayout.createSequentialGroup()
                                .addGap(120, 120, 120)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(backgroundLayout.createSequentialGroup()
                                .addGap(110, 110, 110)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        backgroundLayout.setVerticalGroup(
                backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(backgroundLayout.createSequentialGroup()
                                .addGap(90, 90, 90)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(background, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(background, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>


    // Variables declaration
    private javax.swing.JPanel background;
    private javax.swing.JButton btnEnter;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel lblWarning;
    private javax.swing.JTextField txtReg;
    // End of variables declaration

}
