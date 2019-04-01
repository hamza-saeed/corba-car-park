import CarPark.*;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PayStationClient extends JFrame {

    static PayStationImplementation payStationImpl = new PayStationImplementation();
    static int hrsStayed;
    static double cost;

    public PayStationClient()
    {
        initComponents();
    }

    public static void main(String args[])
    {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PayStationClient().setVisible(true);
            }
        });
        setupClientServerConnections(args);

    }

    private static void setupClientServerConnections(String[] args) {

        String payStationName = getArgs(args, "-Name");
        String serverName = getArgs(args,"-LocalServer");

        try {
            ORB orb = ORB.init(args, null);

            org.omg.CORBA.Object nameServiceObject = orb.resolve_initial_references("NameService");
            if (nameServiceObject == null)
            {
                System.out.println("nameServiceObject = null");
            }

            NamingContextExt nameService = NamingContextExtHelper.narrow(nameServiceObject);
            if (nameService == null)
            {
                System.out.println("nameService=null");
            }

            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            LocalServer localServer = LocalServerHelper.narrow(nameService.resolve_str(serverName));
            payStationImpl.lServerRef = localServer;

            if (localServer.isPayStationNameUnique(payStationName)) {
                payStationImpl.registerPaystation(payStationName);

                org.omg.CORBA.Object ref = rootpoa.servant_to_reference(payStationImpl);
                PayStation cref = PayStationHelper.narrow(ref);
                NameComponent[] payName = nameService.to_name(payStationName);
                nameService.rebind(payName, cref);


                lblName.setText("Name: " + payStationName);
                lblServer.setText("Server: " + serverName);

                orb.run();
            }
            else
            {
                JOptionPane.showMessageDialog(null,"Paystation name must be unique");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void txtHoursStayKeyTyped(java.awt.event.KeyEvent evt) {
    }

    private void txtHoursStayKeyReleased(java.awt.event.KeyEvent evt) {
        try {
            hrsStayed = (int)Math.ceil(Double.parseDouble(txtHoursStay.getText()));
            if (hrsStayed <= 48) {
                cost = hrsStayed * payStationImpl.lServerRef.getCost();
                lblCost.setText("£" + cost);
                lblWarning.setText("");
                btnPay.setEnabled(true);
            }
            else
            {
                lblCost.setText((""));
                lblWarning.setText("Max stay is 48 hours.");
                btnPay.setEnabled(false);
            }
        } catch (NumberFormatException nf) {
            lblCost.setText((""));
            lblWarning.setText("Input must be a number");
            btnPay.setEnabled(false);
        }
    }

    private void btnPayActionPerformed(java.awt.event.ActionEvent evt) {
        if (!payStationImpl.machine.enabled)
        {
            JOptionPane.showMessageDialog(null,"Paystation is disabled", "Error",JOptionPane.ERROR_MESSAGE);
            return;
        }

        String reg = txtReg.getText();

        if (payStationImpl.pay(txtReg.getText(), hrsStayed, cost)) {
            ParkingTransaction transaction = payStationImpl.lServerRef.getParkingTransaction(reg);
            LocalDateTime entryDateTime = LocalDateTime.of(transaction.entryDate.year,transaction.entryDate.month,
                    transaction.entryDate.day,transaction.entryTime.hr,transaction.entryTime.min,transaction.entryTime.sec);
            LocalDateTime expiry = entryDateTime.plusHours((long)hrsStayed);
            Date expiryDate = new Date();
            expiryDate.day = entryDateTime.getDayOfMonth();
            expiryDate.month = entryDateTime.getMonth().getValue();
            expiryDate.year = entryDateTime.getYear();

            Time expiryTime = new Time();
            expiryTime.hr = entryDateTime.getHour();
            expiryTime.min = entryDateTime.getMinute();
            expiryTime.sec = entryDateTime.getSecond();

            JTextArea ticket = new JTextArea();
            ticket.append("Car Reg: " + reg + "\n");
            ticket.append("Amount Paid: £" + cost + "\n");
            ticket.append("Entered: " + entryDateTime.getDayOfMonth() + "/" + entryDateTime.getMonth().getValue() + "/" + entryDateTime.getYear() + " " );
            ticket.append(entryDateTime.getHour() + ":" + entryDateTime.getMinute() + ":" + entryDateTime.getSecond() + "\n");
            ticket.append("Leave by: " + expiry.getDayOfMonth() + "/" + expiry.getMonth().getValue() + "/" +  expiry.getYear() + " ");
            ticket.append(expiry.getHour() + ":" + expiry.getMinute() + ":" + expiry.getSecond());

            System.out.println("amountPaid:" + cost);

            JOptionPane.showMessageDialog(null, new JScrollPane( ticket), "Ticket",
                    JOptionPane.INFORMATION_MESSAGE);

            //TODO: Clear screens for next customer
        }

    }

    private static String getArgs(String[] args, String var) {
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
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblServer = new javax.swing.JLabel();
        lblName = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        txtReg = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtHoursStay = new javax.swing.JTextField();
        lblCost = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblWarning = new javax.swing.JLabel();
        btnPay = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        background.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(22, 186, 136));

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Pay Station");

        lblServer.setForeground(new java.awt.Color(255, 255, 255));
        lblServer.setText("[Server]");

        lblName.setForeground(new java.awt.Color(255, 255, 255));
        lblName.setText("[Name]");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addContainerGap(52, Short.MAX_VALUE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblServer, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblName, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(71, 71, 71))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(lblName)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lblServer))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGap(33, 33, 33)
                                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)))
                                .addGap(27, 27, 27))
        );

        jPanel1.setBackground(new java.awt.Color(22, 160, 134));

        jPanel4.setBackground(new java.awt.Color(22, 186, 136));

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Pay");

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
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setText("Vehicle Registration:");

        jLabel4.setText("Hours Stay:");

        txtHoursStay.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtHoursStayKeyTyped(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtHoursStayKeyReleased(evt);
            }
        });

        lblCost.setText("£");

        jLabel5.setText("Cost:");

        lblWarning.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        lblWarning.setForeground(new java.awt.Color(255, 51, 51));

        btnPay.setText("Pay");

        btnPay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPayActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel5))
                                .addGap(57, 57, 57)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addComponent(txtHoursStay, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lblWarning, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(btnPay)
                                                        .addComponent(lblCost)
                                                        .addComponent(txtReg, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap(59, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel2)
                                        .addComponent(txtReg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addGap(23, 23, 23)
                                                .addComponent(jLabel4))
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(txtHoursStay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(lblWarning))))
                                .addGap(34, 34, 34)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblCost)
                                        .addComponent(jLabel5))
                                .addGap(18, 18, 18)
                                .addComponent(btnPay))
        );

        javax.swing.GroupLayout backgroundLayout = new javax.swing.GroupLayout(background);
        background.setLayout(backgroundLayout);
        backgroundLayout.setHorizontalGroup(
                backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(backgroundLayout.createSequentialGroup()
                                .addGap(126, 126, 126)
                                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(backgroundLayout.createSequentialGroup()
                                .addGap(113, 113, 113)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        backgroundLayout.setVerticalGroup(
                backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(backgroundLayout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 26, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(background, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(background, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>


    // Variables declaration - do not modify
    private javax.swing.JPanel background;
    private javax.swing.JButton btnPay;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel lblCost;
    static javax.swing.JLabel lblName;
    static javax.swing.JLabel lblServer;
    private javax.swing.JLabel lblWarning;
    private javax.swing.JTextField txtHoursStay;
    private javax.swing.JTextField txtReg;
    // End of variables declaration
}
