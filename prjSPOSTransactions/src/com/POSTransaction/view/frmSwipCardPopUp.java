package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import javax.swing.JOptionPane;

public class frmSwipCardPopUp extends javax.swing.JDialog
{

    private String formName;
    private clsUtility objUtility;

    public frmSwipCardPopUp(java.awt.Frame parent)
    {
	super(parent, true);
	initComponents();
	objUtility = new clsUtility();
	lblErrorMessage.setVisible(false);
	setAlwaysOnTop(true);
	setLocationRelativeTo(null);
	txtDebitCardString.requestFocus();
    }

    public frmSwipCardPopUp(java.awt.Frame parent, String strformName)
    {
	super(parent, true);
	formName = strformName;
	initComponents();
	objUtility = new clsUtility();
	lblErrorMessage.setVisible(false);
	setAlwaysOnTop(true);
	setLocationRelativeTo(null);
	txtDebitCardString.requestFocus();
    }
    
    public frmSwipCardPopUp(java.awt.Frame parent, String strformName,boolean strCardSwipe)
    {
	super(parent, true);
	formName = strformName;
	initComponents();
	objUtility = new clsUtility();
	lblErrorMessage.setVisible(false);
	setAlwaysOnTop(!strCardSwipe); //set always top to false if card swipe is on make KOT
	setLocationRelativeTo(null);
	txtDebitCardString.requestFocus();
    }

    /**
     * This function reads the card string from the card string it checks
     * whether card is register or not , card id valid or not,card is active or
     * not and depending on the card status it shows the error message
     */
    private void funOKButtonPressed()
    {
	if (txtDebitCardString.getText().isEmpty())
	{
	    JOptionPane.showMessageDialog(null, "Please Swipe Valid Card!!!");
	    return;
	}
	clsGlobalVarClass.gDebitCardNo = null;
	//%2016000176?;2016000176?;2016000176?
	String cardNo = objUtility.funGetSingleTrackData(txtDebitCardString.getText());
	txtDebitCardString.setText(cardNo);

	clsGlobalVarClass.gDebitCardNo = txtDebitCardString.getText();
	if ("frmRegisterDebitCard".trim().equalsIgnoreCase(formName))
	{
	    lblErrorMessage.setVisible(false);
	    this.dispose();
	}
	else if ("frmRechargeDebitCard".trim().equalsIgnoreCase(formName))
	{
	    String status = objUtility.funGetDebitCardStatus(clsGlobalVarClass.gDebitCardNo, "CardString");
	    if ("Active".trim().equalsIgnoreCase(status))
	    {
		lblErrorMessage.setVisible(false);
		this.dispose();
	    }
	    else if (status.startsWith("Card Time Expired"))
	    {
		lblErrorMessage.setVisible(false);
		this.dispose();

		String[] arrMesg = status.split("!");

		JOptionPane.showMessageDialog(null, "<html>Recharge No:" + arrMesg[1] + "<br>Recharge Amt:" + arrMesg[2] + "<br>Recharge Time:" + arrMesg[3] + "</html>", "Card Time Expired", JOptionPane.WARNING_MESSAGE);
	    }
	    else
	    {
		lblErrorMessage.setVisible(true);
		lblErrorMessage.setText(status);
	    }
	}
	else if ("frmMakeKOT".trim().equalsIgnoreCase(formName))
	{
	    this.dispose();
	}
	else if ("frmRegisterInOutPlayZone".trim().equalsIgnoreCase(formName))
	{
	    String status = objUtility.funGetDebitCardStatus(clsGlobalVarClass.gDebitCardNo, "CardString", formName);
	    if ("Active".trim().equalsIgnoreCase(status))
	    {
		lblErrorMessage.setVisible(false);
		this.dispose();
	    }
	    else if (status.startsWith("Card Time Expired"))
	    {
		lblErrorMessage.setVisible(false);
		this.dispose();

		String[] arrMesg = status.split("!");

		JOptionPane.showMessageDialog(null, "<html>Recharge No:" + arrMesg[1] + "<br>Recharge Amt:" + arrMesg[2] + "<br>Recharge Time:" + arrMesg[3] + "</html>", "Card Time Expired", JOptionPane.WARNING_MESSAGE);
	    }
	    else
	    {
		lblErrorMessage.setVisible(true);
		lblErrorMessage.setText(status);
	    }
	}
	else
	{
	    String status = objUtility.funGetDebitCardStatus(clsGlobalVarClass.gDebitCardNo, "CardString");
	    if ("Active".trim().equalsIgnoreCase(status))
	    {
		lblErrorMessage.setVisible(false);
		this.dispose();
	    }
	    else if (status.startsWith("Card Time Expired"))
	    {
		lblErrorMessage.setVisible(true);
		lblErrorMessage.setText("Card Time Expired");
		
		funCancelPopUp();

		String[] arrMesg = status.split("!");

		JOptionPane.showMessageDialog(null, "<html>Recharge No:" + arrMesg[1] + "<br>Recharge Amt:" + arrMesg[2] + "<br>Recharge Time:" + arrMesg[3] + "</html>", "Card Time Expired", JOptionPane.ERROR_MESSAGE);
	    }
	    else
	    {
		lblErrorMessage.setVisible(true);
		lblErrorMessage.setText(status);
	    }
	}
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
        txtDebitCardString = new javax.swing.JPasswordField();

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
        btnOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnOk.setText("Ok");
        btnOk.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOk.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnOk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOkMouseClicked(evt);
            }
        });
        jPanel1.add(btnOk);
        btnOk.setBounds(120, 130, 100, 40);

        btnCancel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(248, 248, 246));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnCancel.setText("Cancel");
        btnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancel.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelMouseClicked(evt);
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

        txtDebitCardString.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtDebitCardStringMouseClicked(evt);
            }
        });
        txtDebitCardString.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDebitCardStringKeyPressed(evt);
            }
        });
        jPanel1.add(txtDebitCardString);
        txtDebitCardString.setBounds(160, 70, 200, 40);

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

    private void btnOkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOkMouseClicked
	// TODO add your handling code here:
	funOKButtonPressed();
    }//GEN-LAST:event_btnOkMouseClicked

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
	// TODO add your handling code here:

	funCancelPopUp();
    }//GEN-LAST:event_btnCancelMouseClicked

    private void txtCardNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCardNoActionPerformed
	// TODO add your handling code here:
	String lblCardNoSet = txtCardNo.getText();
	if (lblCardNoSet.trim().length() > 0)
	{
	    txtDebitCardString.setText("");
	    txtDebitCardString.setText(lblCardNoSet);
	    txtCardNo.setText("");
	}
	else
	{
	    txtDebitCardString.setText("");
	}
    }//GEN-LAST:event_txtCardNoActionPerformed

    private void txtDebitCardStringKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDebitCardStringKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    funOKButtonPressed();
	}
    }//GEN-LAST:event_txtDebitCardStringKeyPressed

    private void txtDebitCardStringMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtDebitCardStringMouseClicked
    {//GEN-HEADEREND:event_txtDebitCardStringMouseClicked
	if (clsGlobalVarClass.gTouchScreenMode)
	{
	    if (txtDebitCardString.getText().length() == 0)
	    {
		frmAlfaNumericKeyBoard objAlfaNumericKeyBoard = new frmAlfaNumericKeyBoard(null, true, "1", "Please Swipe");

		objAlfaNumericKeyBoard.setVisible(true);
		txtDebitCardString.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	    else
	    {
		frmAlfaNumericKeyBoard objAlfaNumericKeyBoard = new frmAlfaNumericKeyBoard(null, true, txtDebitCardString.getText(), "1", "Please Swipe");

		objAlfaNumericKeyBoard.setVisible(true);
		txtDebitCardString.setText(clsGlobalVarClass.gKeyboardValue);
	    }

	    funOKButtonPressed();
	}
    }//GEN-LAST:event_txtDebitCardStringMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOk;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblErrorMessage;
    private javax.swing.JLabel lblMessage;
    private javax.swing.JTextField txtCardNo;
    public static javax.swing.JPasswordField txtDebitCardString;
    // End of variables declaration//GEN-END:variables

    private void funCancelPopUp()
    {
	clsGlobalVarClass.gDebitCardNo = null;
	this.dispose();
    }

}
