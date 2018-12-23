package com.sanguine.forms;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class frmConfigSettings extends javax.swing.JFrame
{

    private String fileData;
    private File file;
    private BufferedReader br;

    public frmConfigSettings() throws Exception
    {
         initComponents(); 
        cmbBillPrinters.addItem("NA");
        cmbAdvRecPrinters.addItem("NA");

        try
        {
            List<String> arrListConfigData = funReadConfigFile();

            if (arrListConfigData.size() > 0)
            {
                txServerName.setText(arrListConfigData.get(0));
                textDataBaseName.setText(arrListConfigData.get(1));
                String decUserId = clsEncryptDecryptAlgorithm.decrypt(arrListConfigData.get(2));
                String decPassword = clsEncryptDecryptAlgorithm.decrypt(arrListConfigData.get(3));
                txtUserIdName.setText(decUserId);
                textPassword.setText(decPassword);
                textIpaddress.setText(arrListConfigData.get(4));
                txtPortNo.setText(arrListConfigData.get(5));
                txtBackUpPathName.setText(arrListConfigData.get(6));
                txtExportPath.setText(arrListConfigData.get(7));
                cmbOSName.setActionCommand(arrListConfigData.get(8));
                Vector vPrinterNames = funGetPrinterNames();
                for (int cntPrinters = 0; cntPrinters < vPrinterNames.size(); cntPrinters++)
                {
                    cmbBillPrinters.addItem(vPrinterNames.elementAt(cntPrinters).toString());
                    cmbAdvRecPrinters.addItem(vPrinterNames.elementAt(cntPrinters).toString());
                }
                cmbBillPrinters.setSelectedItem(arrListConfigData.get(9));

                cmbPrinterType.setActionCommand(arrListConfigData.get(10));
                String touchScreenMode = arrListConfigData.get(11);
                if (touchScreenMode.equals("true"))
                {
                    chkTouchScreenMode.setSelected(true);
                }
                txtServerFilePath.setText(arrListConfigData.get(12));
                String waiterSelectionFromCardSwipe = arrListConfigData.get(13);
                if (waiterSelectionFromCardSwipe.equals("true"))
                {
                    chkSelectWaiterFromCardSwipe.setSelected(true);
                }
                txtMySqlBackupPath.setText(arrListConfigData.get(14));
                if (arrListConfigData.get(15).equals("true"))
                {
                    chkHOCommunication.setSelected(true);
                }
                else
                {
                    chkHOCommunication.setSelected(false);
                }
                cmbAdvRecPrinters.setSelectedItem(arrListConfigData.get(16));
            }
            txServerName.requestFocus();
            funSetShortCutKeys();

        }
        catch (Exception e)
        {
            throw e;
        }
    }

    private void funSetShortCutKeys()
    {
        btnClose.setMnemonic('c');
        btnSave.setMnemonic('s');
        btnReset.setMnemonic('r');
    }

    public List<String> funReadConfigFile() throws Exception
    {
        List<String> arrListConfigData = new ArrayList<String>();
        file = new File(System.getProperty("user.dir") + "/ConfigFile.txt");
        br = new BufferedReader(new FileReader(file));
        while ((fileData = br.readLine()) != null)
        {
            arrListConfigData.add(fileData.trim());
        }

        return arrListConfigData;
    }

    /**
     * This method is used to get printer names
     *
     * @return vector vTemPrinterNames
     */
    public java.util.Vector funGetPrinterNames()
    {
        java.util.Vector vTemPrinterNames = new java.util.Vector();
        vTemPrinterNames.add("");
        try
        {
            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE; // MY FILE IS .txt TYPE
            PrintService[] printService = PrintServiceLookup.lookupPrintServices(flavor, pras);
            for (int i = 0; i < printService.length; i++)
            {
                //System.out.println("Printer Names= "+printService[i].getName());
                vTemPrinterNames.add(printService[i].getName());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        //System.out.println("Size=="+vTemPrinterNames.size());
        return vTemPrinterNames;
    }

    private void funResetFields()
    {
        cmbOSName.setActionCommand("");
        textDataBaseName.setText("");
        txServerName.setText("");
        txtBackUpPathName.setText("");
        cmbBillPrinters.setSelectedIndex(0);
        chkSelectWaiterFromCardSwipe.setSelected(false);
        chkTouchScreenMode.setSelected(false);
        txtExportPath.setText("");
        textPassword.setText("");
        txtPortNo.setText("");
        cmbPrinterType.setActionCommand(" ");
        txtUserIdName.setText("");
        textIpaddress.setText("");
        txtMySqlBackupPath.setText("");
        cmbAdvRecPrinters.setSelectedIndex(0);
    }

    public static void main(String args[])
    {
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    new frmConfigSettings().setVisible(true);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

    }

    private void funSaveButtonPressed() throws Exception
    {
        String pass = textPassword.getText().trim();
        if (pass.equalsIgnoreCase("root"))
        {
            System.out.println("Invalid pass");
            JOptionPane.showMessageDialog(this, "You can not set root as mysql password. Please change mysql password in both config settings and mysql!!!");

        }
        else
        {
            String serverName = txServerName.getText();
            String dataBaseName = textDataBaseName.getText();
            String userId = txtUserIdName.getText().trim();
            String dbPassword = textPassword.getText().trim();
            String encPassword = clsEncryptDecryptAlgorithm.encrypt(dbPassword);
            String encUserId = clsEncryptDecryptAlgorithm.encrypt(userId);
            String ipAddress = textIpaddress.getText();
            String port = txtPortNo.getText();
            String backUpPath = txtBackUpPathName.getText();
            String exprotPath = txtExportPath.getText();
            String serverFilePath = txtServerFilePath.getText();
            String Osname = cmbOSName.getSelectedItem().toString();
            String printerName = cmbBillPrinters.getSelectedItem().toString();
            String printerType = cmbPrinterType.getSelectedItem().toString();
            String mysqlBackupPath = txtMySqlBackupPath.getText();
            String touchScreenMode = "false";
            if (chkTouchScreenMode.isSelected())
            {
                touchScreenMode = "true";
            }
            else
            {
                touchScreenMode = "false";
            }

            String waiterSelectionFromCardSwipe = "false";
            if (chkSelectWaiterFromCardSwipe.isSelected())
            {
                waiterSelectionFromCardSwipe = "true";
            }
            else
            {
                waiterSelectionFromCardSwipe = "false";
            }
            String HOCommunication = "false";
            if (chkHOCommunication.isSelected())
            {
                HOCommunication = "true";
            }

            try
            {

                String path = System.getProperty("user.dir") + "/ConfigFile.txt";
                PrintWriter writer = new PrintWriter(path, "UTF-8");

                writer.println(serverName);
                writer.println(dataBaseName);
                writer.println(encUserId);
                writer.println(encPassword);
                writer.println(ipAddress);
                writer.println(port);
                writer.println(backUpPath);
                writer.println(exprotPath);
                writer.println(Osname);
                writer.println(printerName);
                writer.println(printerType);
                writer.println(touchScreenMode);
                writer.println(serverFilePath);
                writer.println(waiterSelectionFromCardSwipe);

                mysqlBackupPath = mysqlBackupPath.replaceAll("\"", "");
                mysqlBackupPath = "\"" + mysqlBackupPath + "\"";

                writer.println(mysqlBackupPath);
                writer.println(HOCommunication);
                writer.println(cmbAdvRecPrinters.getSelectedItem().toString());
                writer.close();
               
                JOptionPane.showMessageDialog(this, "File Updated successfully");
                
                

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        panelHeader = new javax.swing.JPanel();
        lblProductName = new javax.swing.JLabel();
        lblModuleName = new javax.swing.JLabel();
        lblformName = new javax.swing.JLabel();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        lblPosName = new javax.swing.JLabel();
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        lblUserCode = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        lblHOSign = new javax.swing.JLabel();
        panelLayout = new JPanel()
        {
            public void paintComponent(Graphics g)
            {
                Image img = Toolkit.getDefaultToolkit().getImage(
                    getClass().getResource("/com/sanguine/images/imgBGJPOS.png"));
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };  ;
        panelBody = new javax.swing.JPanel();
        tabGeneral = new javax.swing.JTabbedPane();
        pnllayout = new javax.swing.JPanel();
        lblserver = new javax.swing.JLabel();
        btnSave = new javax.swing.JButton();
        txServerName = new javax.swing.JTextField();
        lblheading = new javax.swing.JLabel();
        textDataBaseName = new javax.swing.JTextField();
        lblPortNo = new javax.swing.JLabel();
        lblSelectMenuHead24 = new javax.swing.JLabel();
        lblOS = new javax.swing.JLabel();
        lblDefaultPrinter = new javax.swing.JLabel();
        txtUserIdName = new javax.swing.JTextField();
        txtPortNo = new javax.swing.JTextField();
        txtBackUpPathName = new javax.swing.JTextField();
        btnReset = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        lblUserId = new javax.swing.JLabel();
        lblDBName = new javax.swing.JLabel();
        lblPassword = new javax.swing.JLabel();
        lblBackUpPath = new javax.swing.JLabel();
        lblPrinterType = new javax.swing.JLabel();
        lblExportPath = new javax.swing.JLabel();
        txtExportPath = new javax.swing.JTextField();
        btnSubMenuClose9 = new javax.swing.JButton();
        lblIpAddress = new javax.swing.JLabel();
        textIpaddress = new javax.swing.JTextField();
        textPassword = new javax.swing.JPasswordField();
        cmbOSName = new javax.swing.JComboBox();
        cmbPrinterType = new javax.swing.JComboBox();
        btnBKBrowse = new javax.swing.JButton();
        lblServerFilePath = new javax.swing.JLabel();
        txtServerFilePath = new javax.swing.JTextField();
        btnBrowseServerFilePath = new javax.swing.JButton();
        lblTouchScreenMode = new javax.swing.JLabel();
        chkTouchScreenMode = new javax.swing.JCheckBox();
        lblWaiterSelection = new javax.swing.JLabel();
        chkSelectWaiterFromCardSwipe = new javax.swing.JCheckBox();
        lblMySqlBackupPath = new javax.swing.JLabel();
        txtMySqlBackupPath = new javax.swing.JTextField();
        lblHOCommunication = new javax.swing.JLabel();
        chkHOCommunication = new javax.swing.JCheckBox();
        cmbBillPrinters = new javax.swing.JComboBox();
        lblAdvReceiptPrinter = new javax.swing.JLabel();
        cmbAdvRecPrinters = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setBounds(new java.awt.Rectangle(0, 0, 0, 0));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setExtendedState(MAXIMIZED_BOTH);
        setUndecorated(true);

        panelHeader.setBackground(new java.awt.Color(69, 164, 238));
        panelHeader.setLayout(new javax.swing.BoxLayout(panelHeader, javax.swing.BoxLayout.LINE_AXIS));

        lblProductName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblProductName.setForeground(new java.awt.Color(255, 255, 255));
        lblProductName.setText("SPOS -");
        panelHeader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("- Config Settings");
        panelHeader.add(lblformName);
        panelHeader.add(filler4);
        panelHeader.add(filler5);

        lblPosName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPosName.setForeground(new java.awt.Color(255, 255, 255));
        lblPosName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPosName.setMaximumSize(new java.awt.Dimension(321, 30));
        lblPosName.setMinimumSize(new java.awt.Dimension(321, 30));
        lblPosName.setPreferredSize(new java.awt.Dimension(321, 30));
        panelHeader.add(lblPosName);
        panelHeader.add(filler6);

        lblUserCode.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblUserCode.setForeground(new java.awt.Color(255, 255, 255));
        lblUserCode.setMaximumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setMinimumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setPreferredSize(new java.awt.Dimension(90, 30));
        panelHeader.add(lblUserCode);

        lblDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDate.setForeground(new java.awt.Color(255, 255, 255));
        lblDate.setMaximumSize(new java.awt.Dimension(192, 30));
        lblDate.setMinimumSize(new java.awt.Dimension(192, 30));
        lblDate.setPreferredSize(new java.awt.Dimension(192, 30));
        panelHeader.add(lblDate);

        lblHOSign.setMaximumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setMinimumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setPreferredSize(new java.awt.Dimension(34, 30));
        panelHeader.add(lblHOSign);

        getContentPane().add(panelHeader, java.awt.BorderLayout.PAGE_START);

        panelLayout.setBackground(new java.awt.Color(255, 255, 255));
        panelLayout.setLayout(new java.awt.GridBagLayout());

        panelBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelBody.setOpaque(false);

        tabGeneral.setBackground(new java.awt.Color(255, 255, 255));
        tabGeneral.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        tabGeneral.setOpaque(true);

        pnllayout.setBackground(new java.awt.Color(255, 255, 255));
        pnllayout.setOpaque(false);

        lblserver.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblserver.setText("Server                      : ");

        btnSave.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sanguine/images/imgCmnBtn1.png"))); // NOI18N
        btnSave.setText("SAVE");
        btnSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSave.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sanguine/images/imgCmnBtn2.png"))); // NOI18N
        btnSave.addAncestorListener(new javax.swing.event.AncestorListener()
        {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt)
            {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt)
            {
                btnSaveAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt)
            {
            }
        });
        btnSave.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnSavebtnSubMenuCloseMouseClicked(evt);
            }
        });
        btnSave.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSaveActionPerformed(evt);
            }
        });
        btnSave.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnSaveKeyPressed(evt);
            }
        });

        txServerName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txServerNametxtSubMenuHeadCodeMouseClicked(evt);
            }
        });
        txServerName.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txServerNametxtSubMenuHeadCodeActionPerformed(evt);
            }
        });
        txServerName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txServerNameKeyPressed(evt);
            }
        });

        lblheading.setBackground(new java.awt.Color(255, 255, 255));
        lblheading.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblheading.setForeground(new java.awt.Color(51, 51, 51));
        lblheading.setText("Configuration Settings");
        lblheading.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        textDataBaseName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                textDataBaseNametxtSubMenuHeadNameMouseClicked(evt);
            }
        });
        textDataBaseName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                textDataBaseNametxtSubMenuHeadNameKeyPressed(evt);
            }
        });

        lblPortNo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPortNo.setText("Port No             :");

        lblSelectMenuHead24.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblSelectMenuHead24.setText(" ");

        lblOS.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblOS.setText("OS                   :");

        lblDefaultPrinter.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDefaultPrinter.setText("Bill Printer          :");

        txtUserIdName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtUserIdNametxtSubMenuHeadShortName1MouseClicked(evt);
            }
        });
        txtUserIdName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtUserIdNametxtSubMenuHeadShortName1KeyPressed(evt);
            }
        });

        txtPortNo.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtPortNotxtSubMenuHeadShortName9MouseClicked(evt);
            }
        });
        txtPortNo.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPortNotxtSubMenuHeadShortName9KeyPressed(evt);
            }
        });

        txtBackUpPathName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtBackUpPathNametxtSubMenuHeadShortName10MouseClicked(evt);
            }
        });
        txtBackUpPathName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtBackUpPathNametxtSubMenuHeadShortName10KeyPressed(evt);
            }
        });

        btnReset.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnReset.setForeground(new java.awt.Color(255, 255, 255));
        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sanguine/images/imgCmnBtn1.png"))); // NOI18N
        btnReset.setText("RESET");
        btnReset.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReset.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sanguine/images/imgCmnBtn2.png"))); // NOI18N
        btnReset.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnResetbtnSubMenuClose1MouseClicked(evt);
            }
        });
        btnReset.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnResetActionPerformed(evt);
            }
        });

        btnClose.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sanguine/images/imgCmnBtn1.png"))); // NOI18N
        btnClose.setText("CLOSE");
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sanguine/images/imgCmnBtn2.png"))); // NOI18N
        btnClose.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnClosebtnSubMenuClose2MouseClicked(evt);
            }
        });
        btnClose.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCloseActionPerformed(evt);
            }
        });

        lblUserId.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblUserId.setText("User Id                    : ");

        lblDBName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDBName.setText("Database Name         : ");

        lblPassword.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPassword.setText("Password               : ");

        lblBackUpPath.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblBackUpPath.setText("Database Backup path:");

        lblPrinterType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPrinterType.setText("Printer Type      :");

        lblExportPath.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblExportPath.setText("Export path          :");

        txtExportPath.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtExportPathKeyPressed(evt);
            }
        });

        btnSubMenuClose9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSubMenuClose9.setForeground(new java.awt.Color(255, 255, 255));
        btnSubMenuClose9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sanguine/images/imgCmnBtn1.png"))); // NOI18N
        btnSubMenuClose9.setText("Browse");
        btnSubMenuClose9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSubMenuClose9.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sanguine/images/imgCmnBtn2.png"))); // NOI18N
        btnSubMenuClose9.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnSubMenuClose9btnSubMenuClose2MouseClicked(evt);
            }
        });
        btnSubMenuClose9.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSubMenuClose9ActionPerformed(evt);
            }
        });

        lblIpAddress.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblIpAddress.setText("IP Adress               :");

        textIpaddress.setText(" ");
        textIpaddress.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                textIpaddressKeyPressed(evt);
            }
        });

        textPassword.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                textPasswordActionPerformed(evt);
            }
        });
        textPassword.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                textPasswordKeyPressed(evt);
            }
        });

        cmbOSName.setEditable(true);
        cmbOSName.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Windows", "Linux" }));
        cmbOSName.setToolTipText("select");
        cmbOSName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbOSNameKeyPressed(evt);
            }
        });

        cmbPrinterType.setEditable(true);
        cmbPrinterType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Inbuild", "Other" }));
        cmbPrinterType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbPrinterTypeActionPerformed(evt);
            }
        });
        cmbPrinterType.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbPrinterTypeKeyPressed(evt);
            }
        });

        btnBKBrowse.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnBKBrowse.setForeground(new java.awt.Color(255, 255, 255));
        btnBKBrowse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sanguine/images/imgCmnBtn1.png"))); // NOI18N
        btnBKBrowse.setText("Browse");
        btnBKBrowse.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBKBrowse.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sanguine/images/imgCmnBtn2.png"))); // NOI18N
        btnBKBrowse.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBKBrowseMouseClicked(evt);
            }
        });

        lblServerFilePath.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblServerFilePath.setText("Share Folder Path :");

        txtServerFilePath.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtServerFilePathKeyPressed(evt);
            }
        });

        btnBrowseServerFilePath.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnBrowseServerFilePath.setForeground(new java.awt.Color(255, 255, 255));
        btnBrowseServerFilePath.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sanguine/images/imgCmnBtn1.png"))); // NOI18N
        btnBrowseServerFilePath.setText("Browse");
        btnBrowseServerFilePath.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBrowseServerFilePath.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sanguine/images/imgCmnBtn2.png"))); // NOI18N
        btnBrowseServerFilePath.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBrowseServerFilePathbtnSubMenuClose2MouseClicked(evt);
            }
        });
        btnBrowseServerFilePath.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnBrowseServerFilePathActionPerformed(evt);
            }
        });

        lblTouchScreenMode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTouchScreenMode.setText("Touch Screen Mode   :");

        chkTouchScreenMode.setOpaque(false);

        lblWaiterSelection.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblWaiterSelection.setText("Select Waiter From Card Swipe :");

        chkSelectWaiterFromCardSwipe.setOpaque(false);

        lblMySqlBackupPath.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMySqlBackupPath.setText("Mysql Dump Path       :");

        txtMySqlBackupPath.setText(" ");
        txtMySqlBackupPath.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtMySqlBackupPathKeyPressed(evt);
            }
        });

        lblHOCommunication.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblHOCommunication.setText("HO Communication   :");

        chkHOCommunication.setText("jCheckBox1");
        chkHOCommunication.setOpaque(false);

        lblAdvReceiptPrinter.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblAdvReceiptPrinter.setText("Adv Rec Printer  :");

        javax.swing.GroupLayout pnllayoutLayout = new javax.swing.GroupLayout(pnllayout);
        pnllayout.setLayout(pnllayoutLayout);
        pnllayoutLayout.setHorizontalGroup(
            pnllayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnllayoutLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(pnllayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnllayoutLayout.createSequentialGroup()
                        .addGroup(pnllayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnllayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(lblDBName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblUserId, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblserver, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(lblPassword)
                            .addComponent(lblIpAddress))
                        .addGap(11, 11, 11)
                        .addGroup(pnllayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnllayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(textPassword, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(pnllayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txServerName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                                    .addComponent(textDataBaseName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                                    .addComponent(txtUserIdName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)))
                            .addComponent(textIpaddress, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35)
                        .addGroup(pnllayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblAdvReceiptPrinter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblOS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblPrinterType)
                            .addComponent(lblPortNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblDefaultPrinter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(2, 2, 2)
                        .addGroup(pnllayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnllayoutLayout.createSequentialGroup()
                                .addGroup(pnllayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtPortNo, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(cmbPrinterType, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cmbOSName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 173, Short.MAX_VALUE)
                                .addComponent(lblSelectMenuHead24))
                            .addGroup(pnllayoutLayout.createSequentialGroup()
                                .addGroup(pnllayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cmbAdvRecPrinters, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmbBillPrinters, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 62, Short.MAX_VALUE)))
                        .addGap(27, 27, 27))
                    .addGroup(pnllayoutLayout.createSequentialGroup()
                        .addGroup(pnllayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblServerFilePath, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblExportPath, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblBackUpPath, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblMySqlBackupPath, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblTouchScreenMode, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnllayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnllayoutLayout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(chkTouchScreenMode, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblWaiterSelection)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(chkSelectWaiterFromCardSwipe)
                                .addGap(30, 30, 30)
                                .addComponent(lblHOCommunication, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chkHOCommunication, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnllayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(pnllayoutLayout.createSequentialGroup()
                                    .addComponent(txtServerFilePath, javax.swing.GroupLayout.PREFERRED_SIZE, 422, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(pnllayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btnSubMenuClose9, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnBrowseServerFilePath, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnllayoutLayout.createSequentialGroup()
                                    .addGap(1, 1, 1)
                                    .addGroup(pnllayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(pnllayoutLayout.createSequentialGroup()
                                            .addComponent(txtExportPath)
                                            .addGap(109, 109, 109))
                                        .addGroup(pnllayoutLayout.createSequentialGroup()
                                            .addComponent(txtBackUpPathName, javax.swing.GroupLayout.PREFERRED_SIZE, 422, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(btnBKBrowse, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGroup(pnllayoutLayout.createSequentialGroup()
                                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(47, 47, 47)
                                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(54, 54, 54)
                                    .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(txtMySqlBackupPath, javax.swing.GroupLayout.PREFERRED_SIZE, 527, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(pnllayoutLayout.createSequentialGroup()
                .addGap(284, 284, 284)
                .addComponent(lblheading, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnllayoutLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {textDataBaseName, txServerName, txtUserIdName});

        pnllayoutLayout.setVerticalGroup(
            pnllayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnllayoutLayout.createSequentialGroup()
                .addComponent(lblheading, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addGroup(pnllayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnllayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnllayoutLayout.createSequentialGroup()
                            .addGroup(pnllayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txServerName, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblserver, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(pnllayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(textDataBaseName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblDBName, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(6, 6, 6)
                            .addGroup(pnllayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(pnllayoutLayout.createSequentialGroup()
                                    .addComponent(lblUserId, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(lblPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(pnllayoutLayout.createSequentialGroup()
                                    .addComponent(txtUserIdName, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(textPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(pnllayoutLayout.createSequentialGroup()
                            .addComponent(txtPortNo, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(43, 43, 43)
                            .addComponent(cmbPrinterType, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(cmbOSName, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnllayoutLayout.createSequentialGroup()
                        .addComponent(lblPortNo, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnllayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblDefaultPrinter, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbBillPrinters, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblPrinterType, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblOS, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(pnllayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnllayoutLayout.createSequentialGroup()
                        .addGroup(pnllayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnllayoutLayout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addGroup(pnllayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblIpAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(textIpaddress, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnllayoutLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(pnllayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblAdvReceiptPrinter, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                                    .addComponent(cmbAdvRecPrinters))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnllayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnllayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnllayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblWaiterSelection, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(chkSelectWaiterFromCardSwipe, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(pnllayoutLayout.createSequentialGroup()
                                    .addGap(3, 3, 3)
                                    .addGroup(pnllayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblHOCommunication, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(chkHOCommunication, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(chkTouchScreenMode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblTouchScreenMode, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnllayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblMySqlBackupPath, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMySqlBackupPath, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnllayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBackUpPathName, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblBackUpPath, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBKBrowse, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnllayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblExportPath, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtExportPath, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSubMenuClose9, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnllayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblServerFilePath, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtServerFilePath, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBrowseServerFilePath, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnllayoutLayout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(lblSelectMenuHead24, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(pnllayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pnllayoutLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnBKBrowse, btnBrowseServerFilePath, btnSubMenuClose9, chkHOCommunication, chkSelectWaiterFromCardSwipe, chkTouchScreenMode, cmbAdvRecPrinters, cmbBillPrinters, cmbPrinterType, lblAdvReceiptPrinter, lblBackUpPath, lblDBName, lblDefaultPrinter, lblExportPath, lblHOCommunication, lblIpAddress, lblMySqlBackupPath, lblPortNo, lblPrinterType, lblServerFilePath, lblTouchScreenMode, lblUserId, lblWaiterSelection, lblserver, textDataBaseName, textIpaddress, textPassword, txServerName, txtBackUpPathName, txtExportPath, txtMySqlBackupPath, txtPortNo, txtServerFilePath, txtUserIdName});

        tabGeneral.addTab("General", pnllayout);

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 796, Short.MAX_VALUE)
            .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelBodyLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(tabGeneral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelBodyLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(tabGeneral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        tabGeneral.getAccessibleContext().setAccessibleName("General "); // NOI18N

        panelLayout.add(panelBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbPrinterTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbPrinterTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbPrinterTypeActionPerformed

    private void textPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textPasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textPasswordActionPerformed

    private void btnSubMenuClose9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubMenuClose9ActionPerformed

        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle("Select Folder");
        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        jfc.setAcceptAllFileFilterUsed(false);
        if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            File tempFile = jfc.getSelectedFile();
            String imagePath = tempFile.getPath();
            String fileName = imagePath.substring(imagePath.lastIndexOf("/") + 1, imagePath.length());
            txtExportPath.setText(tempFile.getPath());
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_btnSubMenuClose9ActionPerformed

    private void btnSubMenuClose9btnSubMenuClose2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSubMenuClose9btnSubMenuClose2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSubMenuClose9btnSubMenuClose2MouseClicked

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        dispose();
        System.exit(0);
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnClosebtnSubMenuClose2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClosebtnSubMenuClose2MouseClicked
        // TODO add your handling code here:
        dispose();
        System.exit(0);
    }//GEN-LAST:event_btnClosebtnSubMenuClose2MouseClicked

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed

        funResetFields();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnResetbtnSubMenuClose1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetbtnSubMenuClose1MouseClicked
        // TODO add your handling code here:
        funResetFields();
    }//GEN-LAST:event_btnResetbtnSubMenuClose1MouseClicked

    private void txtBackUpPathNametxtSubMenuHeadShortName10KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBackUpPathNametxtSubMenuHeadShortName10KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            txtExportPath.requestFocus();
        }
    }//GEN-LAST:event_txtBackUpPathNametxtSubMenuHeadShortName10KeyPressed

    private void txtBackUpPathNametxtSubMenuHeadShortName10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtBackUpPathNametxtSubMenuHeadShortName10MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBackUpPathNametxtSubMenuHeadShortName10MouseClicked

    private void txtPortNotxtSubMenuHeadShortName9KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPortNotxtSubMenuHeadShortName9KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            textDataBaseName.requestFocus();
        }
    }//GEN-LAST:event_txtPortNotxtSubMenuHeadShortName9KeyPressed

    private void txtPortNotxtSubMenuHeadShortName9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPortNotxtSubMenuHeadShortName9MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPortNotxtSubMenuHeadShortName9MouseClicked

    private void txtUserIdNametxtSubMenuHeadShortName1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUserIdNametxtSubMenuHeadShortName1KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            cmbPrinterType.requestFocus();
        }
    }//GEN-LAST:event_txtUserIdNametxtSubMenuHeadShortName1KeyPressed

    private void txtUserIdNametxtSubMenuHeadShortName1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtUserIdNametxtSubMenuHeadShortName1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUserIdNametxtSubMenuHeadShortName1MouseClicked

    private void textDataBaseNametxtSubMenuHeadNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textDataBaseNametxtSubMenuHeadNameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            cmbBillPrinters.requestFocus();
        }
    }//GEN-LAST:event_textDataBaseNametxtSubMenuHeadNameKeyPressed

    private void textDataBaseNametxtSubMenuHeadNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_textDataBaseNametxtSubMenuHeadNameMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_textDataBaseNametxtSubMenuHeadNameMouseClicked

    private void txServerNametxtSubMenuHeadCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txServerNametxtSubMenuHeadCodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txServerNametxtSubMenuHeadCodeActionPerformed

    private void txServerNametxtSubMenuHeadCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txServerNametxtSubMenuHeadCodeMouseClicked

    }//GEN-LAST:event_txServerNametxtSubMenuHeadCodeMouseClicked

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed

    }//GEN-LAST:event_btnSaveActionPerformed


    private void btnSavebtnSubMenuCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSavebtnSubMenuCloseMouseClicked
        try
        {
            funSaveButtonPressed();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnSavebtnSubMenuCloseMouseClicked

    private void btnBKBrowseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBKBrowseMouseClicked
        // TODO add your handling code here:
        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle("Select Folder");
        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        jfc.setAcceptAllFileFilterUsed(false);
        if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            File tempFile = jfc.getSelectedFile();
            String BackUpPath = tempFile.getPath();
            String fileName = BackUpPath.substring(BackUpPath.lastIndexOf("/") + 1, BackUpPath.length());
            txtBackUpPathName.setText(tempFile.getPath());
        }

    }//GEN-LAST:event_btnBKBrowseMouseClicked

    private void txServerNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txServerNameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            txtPortNo.requestFocus();
        }
    }//GEN-LAST:event_txServerNameKeyPressed

    private void cmbPrinterTypeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbPrinterTypeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            textPassword.requestFocus();
        }
    }//GEN-LAST:event_cmbPrinterTypeKeyPressed

    private void textPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textPasswordKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            cmbOSName.requestFocus();
        }
    }//GEN-LAST:event_textPasswordKeyPressed

    private void cmbOSNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbOSNameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            textIpaddress.requestFocus();
        }
    }//GEN-LAST:event_cmbOSNameKeyPressed

    private void textIpaddressKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textIpaddressKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {

        }
    }//GEN-LAST:event_textIpaddressKeyPressed

    private void txtExportPathKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtExportPathKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            btnSave.requestFocus();
        }
    }//GEN-LAST:event_txtExportPathKeyPressed

    private void btnSaveKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSaveKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            try
            {
                funSaveButtonPressed();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnSaveKeyPressed

    private void txtServerFilePathKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtServerFilePathKeyPressed
    {//GEN-HEADEREND:event_txtServerFilePathKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtServerFilePathKeyPressed

    private void btnBrowseServerFilePathbtnSubMenuClose2MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnBrowseServerFilePathbtnSubMenuClose2MouseClicked
    {//GEN-HEADEREND:event_btnBrowseServerFilePathbtnSubMenuClose2MouseClicked
        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle("Select Folder");
        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        jfc.setAcceptAllFileFilterUsed(false);
        if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            File tempFile = jfc.getSelectedFile();
            String BackUpPath = tempFile.getPath();
            String fileName = BackUpPath.substring(BackUpPath.lastIndexOf("/") + 1, BackUpPath.length());
            txtServerFilePath.setText(tempFile.getPath());
        }

    }//GEN-LAST:event_btnBrowseServerFilePathbtnSubMenuClose2MouseClicked

    private void btnBrowseServerFilePathActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnBrowseServerFilePathActionPerformed
    {//GEN-HEADEREND:event_btnBrowseServerFilePathActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBrowseServerFilePathActionPerformed

    private void txtMySqlBackupPathKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMySqlBackupPathKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMySqlBackupPathKeyPressed

    private void btnSaveAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_btnSaveAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSaveAncestorAdded


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBKBrowse;
    private javax.swing.JButton btnBrowseServerFilePath;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSubMenuClose9;
    private javax.swing.JCheckBox chkHOCommunication;
    private javax.swing.JCheckBox chkSelectWaiterFromCardSwipe;
    private javax.swing.JCheckBox chkTouchScreenMode;
    private javax.swing.JComboBox cmbAdvRecPrinters;
    private javax.swing.JComboBox cmbBillPrinters;
    private javax.swing.JComboBox cmbOSName;
    private javax.swing.JComboBox cmbPrinterType;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblAdvReceiptPrinter;
    private javax.swing.JLabel lblBackUpPath;
    private javax.swing.JLabel lblDBName;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDefaultPrinter;
    private javax.swing.JLabel lblExportPath;
    private javax.swing.JLabel lblHOCommunication;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblIpAddress;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblMySqlBackupPath;
    private javax.swing.JLabel lblOS;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblPortNo;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblPrinterType;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblSelectMenuHead24;
    private javax.swing.JLabel lblServerFilePath;
    private javax.swing.JLabel lblTouchScreenMode;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblUserId;
    private javax.swing.JLabel lblWaiterSelection;
    private javax.swing.JLabel lblformName;
    private javax.swing.JLabel lblheading;
    private javax.swing.JLabel lblserver;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JPanel pnllayout;
    private javax.swing.JTabbedPane tabGeneral;
    private javax.swing.JTextField textDataBaseName;
    private javax.swing.JTextField textIpaddress;
    private javax.swing.JPasswordField textPassword;
    private javax.swing.JTextField txServerName;
    private javax.swing.JTextField txtBackUpPathName;
    private javax.swing.JTextField txtExportPath;
    private javax.swing.JTextField txtMySqlBackupPath;
    private javax.swing.JTextField txtPortNo;
    private javax.swing.JTextField txtServerFilePath;
    private javax.swing.JTextField txtUserIdName;
    // End of variables declaration//GEN-END:variables
}
