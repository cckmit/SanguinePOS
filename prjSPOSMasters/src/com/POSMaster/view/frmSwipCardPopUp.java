package com.POSMaster.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility;

public class frmSwipCardPopUp extends javax.swing.JDialog {

    private String formName;

    public frmSwipCardPopUp(java.awt.Frame parent) {

        super(parent, true);
        initComponents();
        funSetShortCutKeys();
        lblErrorMessage.setVisible(false);
        setAlwaysOnTop(true);
        setLocationRelativeTo(null);
    }

    private void funSetShortCutKeys() {
        btnCancel.setMnemonic('c');
        btnOk.setMnemonic('s');
    }

    /**
     * This method is used to create new form using form name
     *
     * @param parent
     * @param strformName
     */
//    %B211234342183?
    public frmSwipCardPopUp(java.awt.Frame parent, String strformName) {
        super(parent, true);
        formName = strformName;
        initComponents();
        lblErrorMessage.setVisible(false);
        setAlwaysOnTop(true);
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblMessage = new javax.swing.JLabel();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        txtCardNo = new javax.swing.JTextField();
        lblErrorMessage = new javax.swing.JLabel();
        txtCardString = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(null);
        setUndecorated(true);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(153, 204, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(600, 179));
        jPanel1.setLayout(null);

        lblMessage.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblMessage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMessage.setText("Please Swipe The Card");
        lblMessage.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel1.add(lblMessage);
        lblMessage.setBounds(0, 0, 510, 40);

        btnOk.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnOk.setForeground(new java.awt.Color(248, 248, 246));
        btnOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnOk.setText("Ok");
        btnOk.setToolTipText("Select Card");
        btnOk.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOk.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnOk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOkMouseClicked(evt);
            }
        });
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });
        jPanel1.add(btnOk);
        btnOk.setBounds(120, 130, 100, 40);

        btnCancel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(248, 248, 246));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnCancel.setText("Cancel");
        btnCancel.setToolTipText("Close");
        btnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancel.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelMouseClicked(evt);
            }
        });
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        jPanel1.add(btnCancel);
        btnCancel.setBounds(300, 130, 100, 40);

        txtCardNo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtCardNo.setForeground(new java.awt.Color(102, 102, 102));
        txtCardNo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCardNo.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153)));
        txtCardNo.setMargin(new java.awt.Insets(0, 0, 0, 0));
        txtCardNo.setMaximumSize(new java.awt.Dimension(0, 0));
        txtCardNo.setMinimumSize(new java.awt.Dimension(0, 0));
        txtCardNo.setPreferredSize(new java.awt.Dimension(0, 0));
        txtCardNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCardNoActionPerformed(evt);
            }
        });
        jPanel1.add(txtCardNo);
        txtCardNo.setBounds(156, 70, 0, 0);

        lblErrorMessage.setBackground(new java.awt.Color(204, 0, 0));
        lblErrorMessage.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblErrorMessage.setForeground(new java.awt.Color(255, 255, 255));
        lblErrorMessage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblErrorMessage.setText("This card is Not Activated");
        lblErrorMessage.setEnabled(false);
        lblErrorMessage.setFocusable(false);
        lblErrorMessage.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblErrorMessage.setOpaque(true);
        jPanel1.add(lblErrorMessage);
        lblErrorMessage.setBounds(180, 40, 160, 20);

        txtCardString.setEditable(false);
        txtCardString.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCardStringActionPerformed(evt);
            }
        });
        txtCardString.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCardStringKeyPressed(evt);
            }
        });
        jPanel1.add(txtCardString);
        txtCardString.setBounds(120, 80, 280, 30);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void funAddSwipCard() {
        clsUtility obj=new clsUtility();
        clsGlobalVarClass.gDebitCardNo = null;
        
        String cardNo=obj.funGetSingleTrackData(txtCardString.getText());
        txtCardString.setText(cardNo);
        
        clsGlobalVarClass.gDebitCardNo = txtCardString.getText();
        if ("frmRegisterDebitCard".trim().equalsIgnoreCase(formName)) {
            lblErrorMessage.setVisible(false);
            this.dispose();
        } else if ("frmRechargeDebitCard".trim().equalsIgnoreCase(formName)) {
            String status = obj.funGetDebitCardStatus(clsGlobalVarClass.gDebitCardNo,"CardString");
            if ("Deactive".trim().equalsIgnoreCase(status)) {
                lblErrorMessage.setVisible(true);
            } else {
                lblErrorMessage.setVisible(false);
                this.dispose();
            }
        } else {
            String status = obj.funGetDebitCardStatus(clsGlobalVarClass.gDebitCardNo,"CardString");
            if ("Deactive".trim().equalsIgnoreCase(status)) {
                lblErrorMessage.setVisible(true);
            } else {
                lblErrorMessage.setVisible(false);
                this.dispose();
            }
        }
    }
    private void btnOkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOkMouseClicked
        // TODO add your handling code here:
        funAddSwipCard();
    }//GEN-LAST:event_btnOkMouseClicked

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        // TODO add your handling code here:
        clsGlobalVarClass.gDebitCardNo = null;
        this.dispose();
    }//GEN-LAST:event_btnCancelMouseClicked

    private void txtCardNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCardNoActionPerformed
        // TODO add your handling code here:
        String lblCardNoSet = txtCardNo.getText();
        if (lblCardNoSet.trim().length() > 0) {
            txtCardString.setText("");
            txtCardString.setText(lblCardNoSet);
            txtCardNo.setText("");
        } else {
            txtCardString.setText("");
        }
    }//GEN-LAST:event_txtCardNoActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        clsGlobalVarClass.gDebitCardNo = null;
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        // TODO add your handling code here:
        funAddSwipCard();
    }//GEN-LAST:event_btnOkActionPerformed

    private void txtCardStringKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCardStringKeyPressed
        // TODO add your handling code here:
        funAddSwipCard();
    }//GEN-LAST:event_txtCardStringKeyPressed

    private void txtCardStringActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCardStringActionPerformed
        // TODO add your handling code here:
        funAddSwipCard();
    }//GEN-LAST:event_txtCardStringActionPerformed
    public static void main(String[] args) {
        new frmSwipCardPopUp(null).setVisible(true);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOk;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblErrorMessage;
    private javax.swing.JLabel lblMessage;
    private javax.swing.JTextField txtCardNo;
    private javax.swing.JPasswordField txtCardString;
    // End of variables declaration//GEN-END:variables

}
