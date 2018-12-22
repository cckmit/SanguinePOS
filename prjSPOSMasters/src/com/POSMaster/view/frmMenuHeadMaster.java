    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSMaster.view;

import com.POSGlobal.controller.clsFixedSizeText;
import com.POSGlobal.controller.clsGlobalSingleObject;
import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import com.POSGlobal.controller.clsUtility;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Blob;
import java.sql.PreparedStatement;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author sss11
 */
public class frmMenuHeadMaster extends javax.swing.JFrame
{

    private String sql;
    String userImagefilePath;
    FileInputStream fileInImg;
    File imgFile;
    BufferedImage imgBf;
    String strPath, part1, part2;
    String[] parts;
    private int posCount;
    private File tempFile = null;
    private File destFile = null;

    private ResultSet posNameSet, countSet, rs;
    private String code, menuCode, menuName;
    private String strCode, posNames = "";
    private String gpCode, posCodes = "";
    private int SelectedMenuHeadRowNo;
    private int sequenceNoToInsert;
    private String OperationalYN = "N";
    private String subMenuHeadCode = "";
    boolean checksubMenuName = false;
    boolean checksubMenuShortName = false;

    private DefaultTableModel dm1;
    clsUtility obj = new clsUtility();

    /**
     * This method is used to initialize menu head master
     */
    public frmMenuHeadMaster()
    {
        initComponents();
        try
        {
            setAlwaysOnTop(true);
            Timer timer = new Timer(500, new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    Date date1 = new Date();
                    String newstr = String.format("%tr", date1);
                    String dateAndTime = clsGlobalVarClass.gPOSDateToDisplay + " " + newstr;
                    lblDate.setText(dateAndTime);
                }
            });
            timer.setRepeats(true);
            timer.setCoalesce(true);
            timer.setInitialDelay(0);
            timer.start();
            txtMenuName.setDocument(new clsFixedSizeText(20));
            txtSubMenuHeadName.setDocument(new clsFixedSizeText(20));
            txtSubMenuHeadShortName.setDocument(new clsFixedSizeText(20));
            txtSubMenuHeadShortName.setDocument(new clsFixedSizeText(12));

            txtSubMenuHeadName.requestFocus();
            lblUserCode.setText(clsGlobalVarClass.gUserCode);
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
            lblPosName.setText(clsGlobalVarClass.gPOSName);
            txtMenuCode.requestFocus();
            funLoadMenuHeadTable();
            funSetShortCutKeys();

        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funSetShortCutKeys()
    {
        btnCancel.setMnemonic('c');
        btnNew.setMnemonic('s');
        btnReset.setMnemonic('r');

    }

    /**
     * This method is used to menu names
     *
     * @param text
     */
    public void setMenuName(String text)
    {
        try
        {
            txtMenuName.setText(text);
        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to set data
     *
     * @param data
     */
    private void setData(Object[] data)
    {
        try
        {
            tempFile = null;
            posNames = "";
            posCodes = "";
            txtMenuCode.setText(data[0].toString());
            txtMenuName.setText(data[1].toString());
            if ("Y".equalsIgnoreCase(data[2].toString()))
            {
                cmbOperational.setSelectedIndex(0);

            }
            else
            {
                cmbOperational.setSelectedIndex(1);
            }

            sql = "select count(strPosName) from tblposmaster";
            posNameSet = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            posNameSet.next();
            posCount = posNameSet.getInt(1);
            posNameSet.close();

            sql = "select * from tblmenuhd where strMenuCode='" + clsGlobalVarClass.gSearchedItem + "'";
            ResultSet rsMenuHeadInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if(rsMenuHeadInfo.next())
            {    
            txtMenuCode.setText(rsMenuHeadInfo.getString(1));
            txtMenuName.setText(rsMenuHeadInfo.getString(2));
            txtIMenuHeadmage.setText(rsMenuHeadInfo.getString(11));
            strPath = rsMenuHeadInfo.getString(11);
            
             String extension = "";

            int i = strPath.lastIndexOf('.');
            if (i > 0) {
                extension = strPath.substring(i+1);
            }
         
            File fileMenuImage = new File(System.getProperty("user.dir") + "\\itemImages\\" + txtMenuCode.getText().trim() + "."+extension);
            
               if (fileMenuImage.exists())
                {
//                    String imagePath = fileMenuImage.getAbsolutePath();
//                    userImagefilePath = imagePath.substring(imagePath.lastIndexOf("/") + 1, imagePath.length());
//                   
//                   
//                    imgBf = funScaleImage(100, 100, userImagefilePath);
//                    ImageIO.write(imgBf, "png", fileMenuImage);
//
//                    ImageIcon icon1 = new ImageIcon(ImageIO.read(fileMenuImage));
//                    bttImage.setIcon(icon1);
//                    txtIMenuHeadmage.setText(imagePath);
                    
                     funSetImage() ;
                    
                }
                else
                {
                    Blob blob = rsMenuHeadInfo.getBlob(12);
                    //InputStream inImg = blob.getBinaryStream();

                    if (blob.length() > 0)
                    {
                        InputStream inImg = blob.getBinaryStream(1, blob.length());
                        byte[] imageBytes = blob.getBytes(1, (int) blob.length());
                        //BufferedImage image = ImageIO.read(inImg);
                        OutputStream outImg = new FileOutputStream(fileMenuImage);
                        int c = 0;
                        while ((c = inImg.read()) > -1)
                        {
                            outImg.write(c);
                        }
                        outImg.close();
                        inImg.close();

                        if (fileMenuImage.exists())
                        {
                            ImageIcon icon1 = new ImageIcon(ImageIO.read(fileMenuImage));
                            bttImage.setIcon(icon1);
                            
                        }
                        else
                        {
                            txtIMenuHeadmage.setText("");
                        }
                    }
                    else
                    {
                        txtIMenuHeadmage.setText("");
                    }
                }
            }  
            //////////////////////////////////////////////////
            
            rsMenuHeadInfo.close();
          
        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to reset fields
     */
    private void funResetField()
    {
        try
        {
            txtIMenuHeadmage.setText("");
            bttImage.setText("");
             bttImage.setIcon(null);
            btnNew.setText("SAVE");
            btnNew.setMnemonic('s');
            txtMenuCode.setText("");
            txtMenuName.setText("");
            txtMenuCode.requestFocus();
            cmbOperational.setSelectedIndex(0);
        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to save menu heads
     */
    private void funSaveMenuHead()
    {
        try
        {
            sql = "select count(*) from tblmenuhd";
            countSet = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            countSet.next();
            int cn = countSet.getInt(1);
            countSet.close();
            if (cn > 0)
            {
                sql = "select max(strMenuCode),MAX(intSequence) from tblmenuhd";
                countSet = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                countSet.next();
                code = countSet.getString(1);
                sequenceNoToInsert = countSet.getInt(2) + 1;

                StringBuilder sb = new StringBuilder(code);
                String ss = sb.delete(0, 1).toString();
                for (int i = 0; i < ss.length(); i++)
                {
                    if (ss.charAt(i) != '0')
                    {
                        strCode = ss.substring(i, ss.length());
                        break;
                    }
                }
                int intCode = Integer.parseInt(strCode);
                intCode++;
                if (intCode < 10)
                {
                    gpCode = "M00000" + intCode;
                }
                else if (intCode < 100)
                {
                    gpCode = "M0000" + intCode;
                }
                else if (intCode < 1000)
                {
                    gpCode = "M000" + intCode;
                }
                else if (intCode < 10000)
                {
                    gpCode = "M00" + intCode;
                }
                else if (intCode < 100000)
                {
                    gpCode = "M0" + intCode;
                }
                else if (intCode < 1000000)
                {
                    gpCode = "M" + intCode;
                }
            }
            else
            {
                gpCode = "M000001";
                sequenceNoToInsert = 0;
            }

            String sqlUniqueName = "select strMenuName from tblmenuhd where strMenuName='" + txtMenuName.getText().trim() + "'";
            ResultSet rsUniqueName = clsGlobalVarClass.dbMysql.executeResultSet(sqlUniqueName);
            if (rsUniqueName.next())
            {
                new frmOkPopUp(this, "This Menu Name is Already Exist", "Error", 0).setVisible(true);
                txtMenuName.requestFocus();
            }
            rsUniqueName.close();
            if (!clsGlobalVarClass.validateEmpty(txtMenuName.getText()))
            {
                new frmOkPopUp(this, "Please Enter Menu Name", "Error", 0).setVisible(true);
                txtMenuName.requestFocus();
            }
            else if (!obj.funCheckLength(txtMenuName.getText(), 20))
            {
                new frmOkPopUp(this, "Menu Head Name length must be less than 20", "Error", 0).setVisible(true);
                txtMenuName.requestFocus();
            }
            else
            {
                txtMenuCode.setText(gpCode);
                if (cmbOperational.getSelectedIndex() == 0)
                {
                    OperationalYN = "Y";

                }
                else
                {
                    OperationalYN = "N";
                }
                menuCode = txtMenuCode.getText();
                menuName = txtMenuName.getText();
//                sql = "insert into tblmenuhd (strMenuCode,strMenuName,strUserCreated,strUserEdited"
//                        + ",dteDateCreated,dteDateEdited,strClientCode,intSequence,strOperational,strMenuImage) "
//                        + "values('" + txtMenuCode.getText() + "','" + txtMenuName.getText() + "'"
//                        + ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
//                        + ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
//                        + ",'" + clsGlobalVarClass.gClientCode + "'," + sequenceNoToInsert + ",'" + OperationalYN + "','"+"')";
//                int exc = clsGlobalVarClass.dbMysql.execute(sql);

                String query = "insert into tblmenuhd values( ?,?,?,?,?,?,?,?,?,?,?,? )";
                PreparedStatement pre = clsGlobalVarClass.conPrepareStatement.prepareStatement(query);
                pre.setString(1, txtMenuCode.getText());
                pre.setString(2, txtMenuName.getText());
                pre.setString(3, clsGlobalVarClass.gUserCode);
                pre.setString(4, clsGlobalVarClass.gUserCode);
                pre.setString(5, clsGlobalVarClass.getCurrentDateTime());
                pre.setString(6, clsGlobalVarClass.getCurrentDateTime());
                pre.setString(7, clsGlobalVarClass.gClientCode);
                pre.setString(8, "N");
                pre.setInt(9, sequenceNoToInsert);
                pre.setString(10, OperationalYN);

                if (txtIMenuHeadmage.getText().toString().trim().isEmpty())
                {
                    pre.setString(12, "");
                    pre.setString(11, "");
                }
                else
                {
                    pre.setBinaryStream(12, (InputStream) fileInImg, (int) tempFile.length());
                    pre.setString(11, userImagefilePath);
                }

                int exc = pre.executeUpdate();
                pre.close();

                if (exc > 0)
                {

                    String sql = "update tblmasteroperationstatus set dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
                            + " where strTableName='Menu' ";
                    clsGlobalVarClass.dbMysql.execute(sql);
                    new frmOkPopUp(this, "Entry added Successfully", "Successfull", 3).setVisible(true);
                    funResetField();
                    funLoadMenuHeadTable();
                }
            }
        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to copy image if present
     *
     * @throws IOException
     */
    private void funCopyImageIfPresent() throws IOException
    {
        if (null != tempFile && null != bttImage.getIcon())
        {
            String imagePath = tempFile.getAbsolutePath();
           
                String extension = "";

                int i = imagePath.lastIndexOf('.');
                if (i > 0)
                {
                    extension = imagePath.substring(i + 1);
                }

            String filePath = System.getProperty("user.dir");
            funCreateitemImagesFolder();
            fileInImg.close();
//            if(fileInImg!=null)
//                {    
                   
                   
                //}
          
            
            destFile = new File(filePath + "/itemImages/" + txtMenuCode.getText().trim() + "."+extension);
            if (destFile.exists())
            {
                destFile.setExecutable(true);
                destFile.setWritable(true);
                destFile.delete();
                
            }
            copyImageFiles(tempFile, destFile);

         

        }
    }

    /**
     * Manisha 29 April 2017
     */
    private void funCreateitemImagesFolder()
    {
        try
        {
            String filePath = System.getProperty("user.dir");
            File file = new File(filePath + "/itemImages");
            if (!file.exists())
            {
                if (file.mkdir())
                {
                    //System.out.println("Directory is created!");
                }
                else
                {
                    //System.out.println("Failed to create directory!");
                }
            }
           
        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to copy image file
     *
     * @param source
     * @param dest
     * @throws IOException
     */
    private void copyImageFiles(File source, File dest) throws IOException, SecurityException 
    { 
        try {
         
            Files.copy(source.toPath(), dest.toPath());
            //,StandardCopyOption.REPLACE_EXISTING
          
        } 
        catch (FileAlreadyExistsException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//       fileInImg.close();
//        boolean bool = false;
//        bool=dest.delete();
//        Files.copy(source.toPath(), dest.toPath(),StandardCopyOption.REPLACE_EXISTING);
//         bool=dest.delete();
        tempFile = null;
        destFile = null;
    } 

    /**
     * This method is used to update menu heads
     */
    private void funUpdateMenuHead()
    {
        try
        {
            String sqlUniqueName = "select strMenuName from tblmenuhd where strMenuName='" + txtMenuName.getText().trim() + "' and strMenuCode !='" + txtMenuCode.getText() + "'";
            ResultSet rsUniqueName = clsGlobalVarClass.dbMysql.executeResultSet(sqlUniqueName);
            if (rsUniqueName.next())
            {
                new frmOkPopUp(this, "This Menu Name is Already Exist", "Error", 0).setVisible(true);
                txtMenuName.requestFocus();
            }
            rsUniqueName.close();
            if (!clsGlobalVarClass.validateEmpty(txtMenuName.getText()))
            {
                new frmOkPopUp(this, "Please Enter Menu Name", "Error", 0).setVisible(true);
                txtMenuName.requestFocus();
            }
            else if (!obj.funCheckLength(txtMenuName.getText(), 20))
            {
                new frmOkPopUp(this, "Menu Head Name length must be less than 20", "Error", 0).setVisible(true);
                txtMenuName.requestFocus();
            }
            else
            {
                menuCode = txtMenuCode.getText();
                menuName = txtMenuName.getText();
                if (cmbOperational.getSelectedIndex() == 0)
                {
                    OperationalYN = "Y";
                }
                else
                {
                    OperationalYN = "N";
                }
//                sql = "UPDATE tblmenuhd SET strMenuName = '" + txtMenuName.getText() + "',strUserEdited='"
//                        + clsGlobalVarClass.gUserCode + "',dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime()
//                        + "',strOperational='" + OperationalYN + "' WHERE strMenuCode ='" + txtMenuCode.getText() + "'";
//                int exc = clsGlobalVarClass.dbMysql.execute(sql);

                String query = "Update tblmenuhd SET strMenuName=? ,strUserEdited=? ,"
                        + " dteDateEdited=? ,strOperational=? ,strImagePath=? ,imgImage=? where strMenuCode=? ";
                PreparedStatement pre = clsGlobalVarClass.conPrepareStatement.prepareStatement(query);
                pre.setString(1, txtMenuName.getText());
                pre.setString(2, clsGlobalVarClass.gUserCode);
                pre.setString(3, clsGlobalVarClass.getCurrentDateTime());
                pre.setString(4, OperationalYN);
                pre.setString(7, txtMenuCode.getText());
//                String imageFilePath = System.getProperty("user.dir") + "\\itemImages\\" + txtMenuCode.getText() + ".jpg";
//                File fileMenuImage = new File(imageFilePath);
//                if (fileMenuImage.exists())
//                {
//                    FileInputStream fileInputStream = new FileInputStream(fileMenuImage);
//                    pre.setBinaryStream(6, (InputStream) fileInputStream, (int) fileMenuImage.length());
//                    pre.setString(5, imageFilePath);
//                }
//                else
//                {
//                    pre.setString(6, "");
//                    pre.setString(5, "");
//
//                }
                
                FileInputStream fileInputStream=null;
                if (txtIMenuHeadmage.getText().toString().trim().isEmpty())
                {
                    pre.setString(6, "");
                    pre.setString(5, "");
                }
                else
                {
                if(tempFile!=null)
                 {
                 strPath = tempFile.getAbsolutePath();
                 }
                 else
                 {
                  strPath = txtIMenuHeadmage.getText();  
                 }    
                 String extension = "";
                 int i = strPath.lastIndexOf('.');
                if (i > 0) 
                {
                  extension = strPath.substring(i+1);
                }
                    File fileItemImage = new File(System.getProperty("user.dir") + "\\itemImages\\" + txtMenuCode.getText().trim() + "." + extension);

                    if (tempFile==null) {
                        String imagePath = fileItemImage.getAbsolutePath();
                        fileInputStream = new FileInputStream(fileItemImage);
                        pre.setBinaryStream(6, (InputStream) fileInputStream, (int) fileItemImage.length());
                        pre.setString(5, imagePath);
                        
                        
                        
                    } else {
                        String imagePath = tempFile.getAbsolutePath();
                        fileInImg = new FileInputStream(tempFile);
                        pre.setBinaryStream(6, (InputStream) fileInImg, (int) tempFile.length());
                        pre.setString(5, imagePath);
                        
                        
//                        OutputStream outImg = new FileOutputStream(fileItemImage);
//                        int c = 0;
//                        while ((c = fileInImg.read()) > -1) {
//                            outImg.write(c);
//                        }
//                        outImg.close();
                        
//                        String imagePath = fileItemImage.getAbsolutePath();
//                        fileInputStream = new FileInputStream(fileItemImage);
//                        pre.setBinaryStream(6, (InputStream) fileInputStream, (int) fileItemImage.length());
//                        pre.setString(5, imagePath);
                        
                        
                    }
//                    pre.setBinaryStream(6, (InputStream) fileInImg, (int) imgFile.length());
//                    pre.setString(5, userImagefilePath);
                }

                int exc = pre.executeUpdate();
                pre.close();
                
                if(fileInputStream!=null)
                {
                    fileInputStream.close();
               }    
                
                if (exc > 0)
                {
                    funCopyImageIfPresent();
                    String sql = "update tblmasteroperationstatus set dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
                            + " where strTableName='Menu' ";
                    clsGlobalVarClass.dbMysql.execute(sql);
                    new frmOkPopUp(this, "Updated Successfully", "Successfull", 3).setVisible(true);
                    funResetField();
                    funLoadMenuHeadTable();
                }
            }
        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to select menu head code
     */
    private void funSelectMenuHeadCode()
    {
        try
        {
            clsUtility obj = new clsUtility();
            obj.funCallForSearchForm("Menu");
            new frmSearchFormDialog(this, true).setVisible(true);
            if (clsGlobalVarClass.gSearchItemClicked)
            {
                btnNew.setText("UPDATE");
                btnNew.setMnemonic('u');
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                setData(data);
                clsGlobalVarClass.gSearchItemClicked = false;
                funSetImage();
               
            }
        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }
    
      /**
     * This method is used to set image
     */
    private void funSetImage() {
        try {
            String imgCode = txtMenuCode.getText().trim();
           
           
                String extension = "";

                int i = strPath.lastIndexOf('.');
                if (i > 0)
                {
                    extension = strPath.substring(i + 1);
                }
            if (imgCode.length() > 0) 
            {
                String filePath = System.getProperty("user.dir");
                File f = new File(filePath + "/itemImages/" + imgCode + "." + extension);
                ImageIcon icon1 = new ImageIcon(ImageIO.read(f));
                bttImage.setIcon(icon1);
                // txtItemImage.setText(userImagefilePath);
            }
        } catch (Exception e) {
            bttImage.setText("NO IMAGE");
            bttImage.setIcon(null);
        }

    }

    /**
     * This method is used to load menu head table
     */
    private void funLoadMenuHeadTable()
    {
        try
        {
            int SequenceNo = 1;
            dm1 = (DefaultTableModel) tblMenuHead.getModel();
            dm1.setRowCount(0);
            sql = "select strMenuCode, strMenuName from tblmenuhd ORDER by intSequence";
            rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rs.next())
            {
                Object[] ob =
                {
                    SequenceNo, rs.getString(1), rs.getString(2)
                };
                dm1.addRow(ob);
                SequenceNo++;
            }
            rs.close();
            tblMenuHead.setModel(dm1);

        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to save sub menu head
     */
   
    private void funSaveSubMenuHead()
    {
        try
        {
            String subMenuName = txtSubMenuHeadName.getText().trim();
            checksubMenuName = funCheckMenuName(subMenuName, txtMenuHead.getText().trim());

            String subMenuShortName = txtSubMenuHeadShortName.getText().trim();
            checksubMenuShortName = funCheckMenuShortName(subMenuShortName);

            if (txtSubMenuHeadName.getText().trim().length() == 0)
            {
                new frmOkPopUp(this, "Please Enter Sub Menu Name", "Error", 0).setVisible(true);
                txtSubMenuHeadName.requestFocus();
                return;
            }
            if (txtSubMenuHeadShortName.getText().trim().length() == 0)
            {
                new frmOkPopUp(this, "Please Enter Short Name For Sub Menu", "Error", 0).setVisible(true);
                txtSubMenuHeadShortName.requestFocus();
                return;
            }
            if (txtMenuHead.getText().trim().length() == 0)
            {
                new frmOkPopUp(this, "Please Select Menu Head", "Error", 0).setVisible(true);
                txtMenuHead.requestFocus();
                return;
            }

            if (checksubMenuName)
            {
                new frmOkPopUp(this, "Sub Menu Name is already exist", "Error", 0).setVisible(true);
                return;
            }
            if (checksubMenuShortName)
            {
                new frmOkPopUp(this, "Sub Menu Short Name is already exist", "Error", 0).setVisible(true);
                return;
            }
            else
            {

                funGenerateSubMenuHeadCode();
                txtSubMenuHeadCode.setText(subMenuHeadCode);
                String operational = "Y";
                if (cmbOperationalSubMenu.getSelectedIndex() == 1)
                {
                    operational = "N";
                }
                String insert = "insert into tblsubmenuhead (strSubMenuHeadCode,strMenuCode,strSubMenuHeadShortName,strSubMenuHeadName,"
                        + "strSubMenuOperational,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strClientCode)"
                        + " values('" + subMenuHeadCode + "','" + txtMenuHead.getText().trim() + "','" + txtSubMenuHeadShortName.getText().trim() + "','" + txtSubMenuHeadName.getText().trim() + ""
                        + "','" + operational + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "',"
                        + "'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "')";
                int exc = clsGlobalVarClass.dbMysql.execute(insert);
		 String sql = "update tblmasteroperationstatus set dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
                            + " where strTableName='SubMenuHead' ";
                    clsGlobalVarClass.dbMysql.execute(sql);
                if (exc > 0)
                {
                    new frmOkPopUp(this, "Entry added Successfully", "Successfull", 3).setVisible(true);
                    funResetSubMenuHeadTab();
                }
            }

        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }
    /**
     * This method is used to generate sub menu head code
     */
    private void funGenerateSubMenuHeadCode()
    {
        try
        {
            sql = "select count(*) from tblsubmenuhead";
            String subCode = "";
            ResultSet count = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            count.next();
            int cn = count.getInt(1);
            count.close();
            if (cn > 0)
            {
                sql = "select max(strSubMenuHeadCode) from tblsubmenuhead";
                ResultSet rsMax = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rsMax.next();
                String code = rsMax.getString(1);
                StringBuilder sb = new StringBuilder(code);
                String ss = sb.delete(0, 2).toString();
                for (int i = 0; i < ss.length(); i++)
                {
                    if (ss.charAt(i) != '0')
                    {
                        subCode = ss.substring(i, ss.length());
                        break;
                    }
                }
                int intCode = Integer.parseInt(subCode);
                intCode++;
                if (intCode < 10)
                {
                    subMenuHeadCode = "SM00000" + intCode;
                }
                else if (intCode < 100)
                {
                    subMenuHeadCode = "SM0000" + intCode;
                }
                else if (intCode < 1000)
                {
                    subMenuHeadCode = "SM000" + intCode;
                }
                else if (intCode < 10000)
                {
                    subMenuHeadCode = "SM00" + intCode;
                }
                else if (intCode < 100000)
                {
                    subMenuHeadCode = "SM0" + intCode;
                }
                else if (intCode < 1000000)
                {
                    subMenuHeadCode = "SM" + intCode;
                }
            }
            else
            {
                subMenuHeadCode = "SM000001";
            }

        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to reset sub menu head tab
     */
    private void funResetSubMenuHeadTab()
    {
        subMenuHeadCode = "";
        txtSubMenuHeadName.setText("");
        txtSubMenuHeadShortName.setText("");
        txtMenuHead.setText("");
        txtSubMenuHeadCode.setText("");
        lblMenuHeadName.setText("");
        cmbOperationalSubMenu.setSelectedIndex(0);
        btnSubMenuSave.setText("SAVE");
        btnSubMenuSave.setMnemonic('s');
        btnNew.setMnemonic('s');
    }

    /**
     * This method is used to select menu head
     */
    private void funSelectMenuHead()
    {
        try
        {
            clsUtility obj = new clsUtility();
            obj.funCallForSearchForm("Menu");
            new frmSearchFormDialog(this, true).setVisible(true);
            if (clsGlobalVarClass.gSearchItemClicked)
            {
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                funSetMenuHead(data);
                clsGlobalVarClass.gSearchItemClicked = false;
            }
        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * select sub menu head code
     */
    private void funSelectSubMenuHeadCode()
    {
        try
        {
            clsUtility obj = new clsUtility();
            obj.funCallForSearchForm("SubMenu");
            new frmSearchFormDialog(this, true).setVisible(true);
            if (clsGlobalVarClass.gSearchItemClicked)
            {
                btnSubMenuSave.setText("UPDATE");
                btnSubMenuSave.setMnemonic('u');
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                funSetSubMenuHeadData(data);
                clsGlobalVarClass.gSearchItemClicked = false;
            }
        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to set data1
     *
     * @param data
     */
    private void funSetMenuHead(Object[] data)
    {
        try
        {
            txtMenuHead.setText(data[0].toString());
            lblMenuHeadName.setText(data[1].toString());

        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to set data3
     *
     * @param data
     */
    private void funSetSubMenuHeadData(Object[] data)
    {
        try
        {

            txtSubMenuHeadCode.setText(data[0].toString());
            txtSubMenuHeadName.setText(data[1].toString());
            txtSubMenuHeadShortName.setText(data[2].toString());
            if ("Y".equalsIgnoreCase(data[3].toString()))
            {
                cmbOperationalSubMenu.setSelectedIndex(0);
            }
            else
            {
                cmbOperationalSubMenu.setSelectedIndex(1);
            }
            String sql = "select b.strMenuCode ,b.strMenuName "
                    + " from tblsubmenuhead a, tblmenuhd b "
                    + " where a.strMenuCode=b.strMenuCode and a.strSubMenuHeadCode='" + data[0].toString() + "'";
            ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rs.next())
            {
                txtMenuHead.setText(rs.getString(1));
                lblMenuHeadName.setText(rs.getString(2));
            }
            rs.close();

        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to update sub menu head
     */
    private void funUpdateSubMenuHead()
    {
        try
        {
            String subMenuName = txtSubMenuHeadName.getText().trim();
            checksubMenuName = funCheckMenuName(subMenuName, txtMenuHead.getText().trim());

            String subMenuShortName = txtSubMenuHeadShortName.getText().trim();
            checksubMenuShortName = funCheckMenuShortName(subMenuShortName);

            if (txtSubMenuHeadName.getText().trim().length() == 0)
            {
                new frmOkPopUp(this, "Please Enter Sub Menu Name", "Error", 0).setVisible(true);
                txtSubMenuHeadName.requestFocus();
                return;
            }
            if (txtSubMenuHeadShortName.getText().trim().length() == 0)
            {
                new frmOkPopUp(this, "Please Enter Short Name For Sub Menu", "Error", 0).setVisible(true);
                txtSubMenuHeadShortName.requestFocus();
                return;
            }
            if (txtMenuHead.getText().trim().length() == 0)
            {
                new frmOkPopUp(this, "Please Select Menu Head", "Error", 0).setVisible(true);
                txtMenuHead.requestFocus();
                return;
            }
            else
            {
                String operational = "Y";
                if (cmbOperationalSubMenu.getSelectedIndex() == 1)
                {
                    operational = "N";
                }
                String updateQuery = "update tblsubmenuhead set strMenuCode='" + txtMenuHead.getText() + "',strSubMenuHeadShortName='" + txtSubMenuHeadShortName.getText() + "',"
                        + "strSubMenuHeadName='" + txtSubMenuHeadName.getText() + "',strSubMenuOperational='" + operational + "',strUserEdited='" + clsGlobalVarClass.gUserCode + "',dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "'"
                        + " where strSubMenuHeadCode='" + txtSubMenuHeadCode.getText() + "'";
                int exec = clsGlobalVarClass.dbMysql.execute(updateQuery);
                if (exec > 0)
                {
                    new frmOkPopUp(this, "Entry Update Successfully", "Successfull", 3).setVisible(true);
                    funResetSubMenuHeadTab();
                }
            }

        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to chec menu names
     *
     * @param subMenuName
     * @param menuCode
     * @return boolean
     */
    private boolean funCheckMenuName(String subMenuName, String menuCode)
    {
        try
        {
            String sql = "select strSubMenuHeadName from tblsubmenuhead "
                    + "where strSubMenuHeadName='" + subMenuName + "' and strMenuCode='" + menuCode + "' ";
            ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rs.next())
            {
                checksubMenuName = true;
            }
            else
            {
                checksubMenuName = false;
            }

        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }
        return checksubMenuName;
    }

    /**
     * This method is used to check menu short name
     *
     * @param subMenuShortName
     * @return boolean
     */
    private boolean funCheckMenuShortName(String subMenuShortName)
    {
        try
        {
            String sql = "select strSubMenuHeadShortName from tblsubmenuhead where strSubMenuHeadShortName='" + subMenuShortName + "'";
            ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rs.next())
            {
                checksubMenuShortName = true;
            }
            else
            {
                checksubMenuShortName = false;
            }

        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }
        return checksubMenuShortName;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

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
        panelLayout = new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSMaster/images/imgBGJPOS.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  ;
        panelBody = new javax.swing.JPanel();
        tabPane = new javax.swing.JTabbedPane();
        tabMenuHead = new javax.swing.JPanel();
        lblMenuHeadMaster = new javax.swing.JLabel();
        lblmenuCode = new javax.swing.JLabel();
        txtMenuCode = new javax.swing.JTextField();
        lblMenuName = new javax.swing.JLabel();
        txtMenuName = new javax.swing.JTextField();
        btnNew = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblOperation = new javax.swing.JLabel();
        cmbOperational = new javax.swing.JComboBox();
        txtIMenuHeadmage = new javax.swing.JTextField();
        lblMenuHeadImage = new javax.swing.JLabel();
        btnBrowse = new javax.swing.JButton();
        bttImage = new javax.swing.JButton();
        tabSubMenuHead = new javax.swing.JPanel();
        btnSubMenuSave = new javax.swing.JButton();
        lblSubMenuHeadCode = new javax.swing.JLabel();
        btnSubMenuReset = new javax.swing.JButton();
        lblSubMenuHeadName = new javax.swing.JLabel();
        btnSubMenuClose = new javax.swing.JButton();
        txtSubMenuHeadCode = new javax.swing.JTextField();
        txtSubMenuHeadShortName = new javax.swing.JTextField();
        txtMenuHead = new javax.swing.JTextField();
        lblSubMenuHeadMast = new javax.swing.JLabel();
        lblsubMenuShortName = new javax.swing.JLabel();
        lblSelectMenuHead = new javax.swing.JLabel();
        lblOperational = new javax.swing.JLabel();
        txtSubMenuHeadName = new javax.swing.JTextField();
        cmbOperationalSubMenu = new javax.swing.JComboBox();
        lblMenuHeadName = new javax.swing.JLabel();
        tabMenuHeadSeq = new javax.swing.JPanel();
        srollPane = new javax.swing.JScrollPane();
        tblMenuHead = new javax.swing.JTable();
        btnClose = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        lblMenuHeadSeq = new javax.swing.JLabel();
        lblMoveUp = new javax.swing.JLabel();
        lblMoveDown = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setMinimumSize(new java.awt.Dimension(800, 600));
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

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
        lblformName.setText("- Menu Head Master");
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

        tabPane.setBackground(new java.awt.Color(255, 255, 255));

        tabMenuHead.setBackground(new java.awt.Color(255, 255, 255));
        tabMenuHead.setOpaque(false);

        lblMenuHeadMaster.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblMenuHeadMaster.setForeground(new java.awt.Color(24, 19, 19));
        lblMenuHeadMaster.setText("Menu Head Master");

        lblmenuCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblmenuCode.setText("Menu Head Code  :");

        txtMenuCode.setEditable(false);
        txtMenuCode.setBackground(new java.awt.Color(204, 204, 204));
        txtMenuCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtMenuCodeMouseClicked(evt);
            }
        });
        txtMenuCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMenuCodeKeyPressed(evt);
            }
        });

        lblMenuName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMenuName.setText("Menu Head Name :");

        txtMenuName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtMenuNameMouseClicked(evt);
            }
        });
        txtMenuName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMenuNameKeyPressed(evt);
            }
        });

        btnNew.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNew.setForeground(new java.awt.Color(255, 255, 255));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnNew.setText("SAVE");
        btnNew.setToolTipText("Save Menu Head ");
        btnNew.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNew.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnNew.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNewMouseClicked(evt);
            }
        });
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        btnNew.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnNewKeyPressed(evt);
            }
        });

        btnReset.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnReset.setForeground(new java.awt.Color(255, 255, 255));
        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnReset.setText("RESET");
        btnReset.setToolTipText("Reset All Fields");
        btnReset.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReset.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnReset.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnResetMouseClicked(evt);
            }
        });
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        btnCancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnCancel.setText("CLOSE");
        btnCancel.setToolTipText("Close Menu Head Master");
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

        lblOperation.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblOperation.setText("Operational          :");

        cmbOperational.setBackground(new java.awt.Color(51, 102, 255));
        cmbOperational.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbOperational.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "YES", "NO" }));
        cmbOperational.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbOperationalKeyPressed(evt);
            }
        });

        txtIMenuHeadmage.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtIMenuHeadmageKeyPressed(evt);
            }
        });

        lblMenuHeadImage.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMenuHeadImage.setText("Image                 :");

        btnBrowse.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnBrowse.setForeground(new java.awt.Color(254, 254, 254));
        btnBrowse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnBrowse.setText("Browse");
        btnBrowse.setToolTipText("Search Item Image");
        btnBrowse.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBrowse.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnBrowse.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBrowseMouseClicked(evt);
            }
        });
        btnBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseActionPerformed(evt);
            }
        });
        btnBrowse.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnBrowseKeyPressed(evt);
            }
        });

        bttImage.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        bttImage.setToolTipText("Item Image");
        bttImage.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout tabMenuHeadLayout = new javax.swing.GroupLayout(tabMenuHead);
        tabMenuHead.setLayout(tabMenuHeadLayout);
        tabMenuHeadLayout.setHorizontalGroup(
            tabMenuHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabMenuHeadLayout.createSequentialGroup()
                .addGap(263, 263, 263)
                .addComponent(lblMenuHeadMaster)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabMenuHeadLayout.createSequentialGroup()
                .addGap(170, 170, 170)
                .addGroup(tabMenuHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabMenuHeadLayout.createSequentialGroup()
                        .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabMenuHeadLayout.createSequentialGroup()
                        .addGroup(tabMenuHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(tabMenuHeadLayout.createSequentialGroup()
                                .addGroup(tabMenuHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblMenuName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblOperation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblmenuCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18))
                            .addGroup(tabMenuHeadLayout.createSequentialGroup()
                                .addComponent(lblMenuHeadImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)))
                        .addGroup(tabMenuHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tabMenuHeadLayout.createSequentialGroup()
                                .addComponent(txtIMenuHeadmage, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBrowse, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bttImage, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtMenuName, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMenuCode, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbOperational, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(147, Short.MAX_VALUE))))
        );
        tabMenuHeadLayout.setVerticalGroup(
            tabMenuHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabMenuHeadLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(lblMenuHeadMaster, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(105, 105, 105)
                .addGroup(tabMenuHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblmenuCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMenuCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(tabMenuHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMenuName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMenuName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(tabMenuHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblOperation, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbOperational, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(tabMenuHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabMenuHeadLayout.createSequentialGroup()
                        .addGroup(tabMenuHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtIMenuHeadmage, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblMenuHeadImage, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBrowse, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(tabMenuHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21))
                    .addGroup(tabMenuHeadLayout.createSequentialGroup()
                        .addComponent(bttImage, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        tabPane.addTab("Menu Head Master", tabMenuHead);

        tabSubMenuHead.setBackground(new java.awt.Color(255, 255, 255));
        tabSubMenuHead.setOpaque(false);

        btnSubMenuSave.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSubMenuSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSubMenuSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnSubMenuSave.setText("SAVE");
        btnSubMenuSave.setToolTipText("Save Sub Menu Head");
        btnSubMenuSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSubMenuSave.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnSubMenuSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSubMenuSaveMouseClicked(evt);
            }
        });
        btnSubMenuSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubMenuSaveActionPerformed(evt);
            }
        });
        btnSubMenuSave.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnSubMenuSaveKeyPressed(evt);
            }
        });

        lblSubMenuHeadCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblSubMenuHeadCode.setText("Sub Menu Head Code             :");

        btnSubMenuReset.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSubMenuReset.setForeground(new java.awt.Color(255, 255, 255));
        btnSubMenuReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnSubMenuReset.setText("RESET");
        btnSubMenuReset.setToolTipText("Reset All Fields");
        btnSubMenuReset.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSubMenuReset.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnSubMenuReset.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSubMenuResetMouseClicked(evt);
            }
        });

        lblSubMenuHeadName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblSubMenuHeadName.setText("Sub Menu Head Name            :");

        btnSubMenuClose.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSubMenuClose.setForeground(new java.awt.Color(255, 255, 255));
        btnSubMenuClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnSubMenuClose.setText("CLOSE");
        btnSubMenuClose.setToolTipText("Close Sub Menu Head");
        btnSubMenuClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSubMenuClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnSubMenuClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSubMenuCloseMouseClicked(evt);
            }
        });

        txtSubMenuHeadCode.setEditable(false);
        txtSubMenuHeadCode.setBackground(new java.awt.Color(204, 204, 204));
        txtSubMenuHeadCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSubMenuHeadCodeMouseClicked(evt);
            }
        });
        txtSubMenuHeadCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSubMenuHeadCodeKeyPressed(evt);
            }
        });

        txtSubMenuHeadShortName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSubMenuHeadShortNameMouseClicked(evt);
            }
        });
        txtSubMenuHeadShortName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSubMenuHeadShortNameKeyPressed(evt);
            }
        });

        txtMenuHead.setEditable(false);
        txtMenuHead.setBackground(new java.awt.Color(204, 204, 204));
        txtMenuHead.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtMenuHeadMouseClicked(evt);
            }
        });
        txtMenuHead.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMenuHeadKeyPressed(evt);
            }
        });

        lblSubMenuHeadMast.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblSubMenuHeadMast.setForeground(new java.awt.Color(24, 19, 19));
        lblSubMenuHeadMast.setText("Sub Menu Head Master");

        lblsubMenuShortName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblsubMenuShortName.setText("Sub Menu Head Short Name  :");

        lblSelectMenuHead.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblSelectMenuHead.setText("Select Menu Head                :");

        lblOperational.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblOperational.setText("Operational                        :");

        txtSubMenuHeadName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSubMenuHeadNameMouseClicked(evt);
            }
        });
        txtSubMenuHeadName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSubMenuHeadNameKeyPressed(evt);
            }
        });

        cmbOperationalSubMenu.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "YES", "NO" }));
        cmbOperationalSubMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbOperationalSubMenuActionPerformed(evt);
            }
        });
        cmbOperationalSubMenu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbOperationalSubMenuKeyPressed(evt);
            }
        });

        lblMenuHeadName.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        javax.swing.GroupLayout tabSubMenuHeadLayout = new javax.swing.GroupLayout(tabSubMenuHead);
        tabSubMenuHead.setLayout(tabSubMenuHeadLayout);
        tabSubMenuHeadLayout.setHorizontalGroup(
            tabSubMenuHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabSubMenuHeadLayout.createSequentialGroup()
                .addGroup(tabSubMenuHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabSubMenuHeadLayout.createSequentialGroup()
                        .addGroup(tabSubMenuHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tabSubMenuHeadLayout.createSequentialGroup()
                                .addGap(263, 263, 263)
                                .addComponent(lblSubMenuHeadMast, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(tabSubMenuHeadLayout.createSequentialGroup()
                                .addGap(170, 170, 170)
                                .addGroup(tabSubMenuHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblsubMenuShortName)
                                    .addComponent(lblSelectMenuHead, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblSubMenuHeadName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblSubMenuHeadCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblOperational, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(52, 52, 52)
                                .addGroup(tabSubMenuHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtSubMenuHeadName, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtSubMenuHeadCode, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtSubMenuHeadShortName, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(tabSubMenuHeadLayout.createSequentialGroup()
                                        .addGroup(tabSubMenuHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtMenuHead)
                                            .addComponent(cmbOperationalSubMenu, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblMenuHeadName, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabSubMenuHeadLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnSubMenuSave, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSubMenuReset, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSubMenuClose, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        tabSubMenuHeadLayout.setVerticalGroup(
            tabSubMenuHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabSubMenuHeadLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(lblSubMenuHeadMast, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addGroup(tabSubMenuHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSubMenuHeadCode, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSubMenuHeadCode))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(tabSubMenuHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSubMenuHeadName, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSubMenuHeadName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(tabSubMenuHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSubMenuHeadShortName, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblsubMenuShortName, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(tabSubMenuHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMenuHead, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSelectMenuHead, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMenuHeadName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(tabSubMenuHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbOperationalSubMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblOperational, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(111, 111, 111)
                .addGroup(tabSubMenuHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSubMenuSave, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSubMenuReset, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSubMenuClose, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49))
        );

        tabPane.addTab("SubMenuHead", tabSubMenuHead);

        tabMenuHeadSeq.setBackground(new java.awt.Color(255, 255, 255));
        tabMenuHeadSeq.setOpaque(false);

        tblMenuHead.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sequence No.", "MenuHeadCode", "MenuHeadName"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblMenuHead.setRowHeight(25);
        tblMenuHead.getTableHeader().setReorderingAllowed(false);
        tblMenuHead.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMenuHeadMouseClicked(evt);
            }
        });
        srollPane.setViewportView(tblMenuHead);
        if (tblMenuHead.getColumnModel().getColumnCount() > 0) {
            tblMenuHead.getColumnModel().getColumn(0).setMinWidth(80);
            tblMenuHead.getColumnModel().getColumn(0).setPreferredWidth(80);
            tblMenuHead.getColumnModel().getColumn(0).setMaxWidth(80);
            tblMenuHead.getColumnModel().getColumn(1).setMinWidth(100);
            tblMenuHead.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblMenuHead.getColumnModel().getColumn(1).setMaxWidth(100);
        }

        btnClose.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnClose.setText("CLOSE");
        btnClose.setToolTipText("Close Menu Head Sequence");
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCloseMouseClicked(evt);
            }
        });

        btnUpdate.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnUpdate.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnUpdate.setText("SAVE");
        btnUpdate.setToolTipText("Save Menu Head Sequence");
        btnUpdate.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnUpdate.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnUpdateMouseClicked(evt);
            }
        });

        lblMenuHeadSeq.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblMenuHeadSeq.setForeground(new java.awt.Color(24, 19, 19));
        lblMenuHeadSeq.setText("Menu Head Sequence");

        lblMoveUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgMoveUp.png"))); // NOI18N
        lblMoveUp.setToolTipText("Move Up");
        lblMoveUp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblMoveUpMouseClicked(evt);
            }
        });

        lblMoveDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgMoveDown.png"))); // NOI18N
        lblMoveDown.setToolTipText("Move Down");
        lblMoveDown.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblMoveDownMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout tabMenuHeadSeqLayout = new javax.swing.GroupLayout(tabMenuHeadSeq);
        tabMenuHeadSeq.setLayout(tabMenuHeadSeqLayout);
        tabMenuHeadSeqLayout.setHorizontalGroup(
            tabMenuHeadSeqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabMenuHeadSeqLayout.createSequentialGroup()
                .addGroup(tabMenuHeadSeqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabMenuHeadSeqLayout.createSequentialGroup()
                        .addGap(175, 175, 175)
                        .addComponent(srollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabMenuHeadSeqLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblMoveUp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblMoveDown)
                        .addGap(217, 217, 217)))
                .addGap(132, 132, 132))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabMenuHeadSeqLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(tabMenuHeadSeqLayout.createSequentialGroup()
                .addGap(259, 259, 259)
                .addComponent(lblMenuHeadSeq, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tabMenuHeadSeqLayout.setVerticalGroup(
            tabMenuHeadSeqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabMenuHeadSeqLayout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addComponent(lblMenuHeadSeq, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(srollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabMenuHeadSeqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMoveUp)
                    .addComponent(lblMoveDown))
                .addGap(30, 30, 30)
                .addGroup(tabMenuHeadSeqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        tabPane.addTab("Menu Head Sequence", tabMenuHeadSeq);

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 796, Short.MAX_VALUE)
            .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelBodyLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(tabPane, javax.swing.GroupLayout.PREFERRED_SIZE, 790, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 566, Short.MAX_VALUE)
            .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelBodyLayout.createSequentialGroup()
                    .addGap(0, 3, Short.MAX_VALUE)
                    .addComponent(tabPane, javax.swing.GroupLayout.PREFERRED_SIZE, 560, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 3, Short.MAX_VALUE)))
        );

        panelLayout.add(panelBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtMenuCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtMenuCodeMouseClicked
        // TODO add your handling code here:
        funSelectMenuHeadCode();
    }//GEN-LAST:event_txtMenuCodeMouseClicked

    private void txtMenuNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtMenuNameMouseClicked
        // TODO add your handling code here:
        if (txtMenuName.getText().length() == 0)
        {
            new frmAlfaNumericKeyBoard(this, true, "1", "Enter Menu Head Name").setVisible(true);
            txtMenuName.setText(clsGlobalVarClass.gKeyboardValue);
        }
        else
        {
            new frmAlfaNumericKeyBoard(this, true, txtMenuName.getText(), "1", "Enter Menu Head Name").setVisible(true);
            txtMenuName.setText(clsGlobalVarClass.gKeyboardValue);
        }
    }//GEN-LAST:event_txtMenuNameMouseClicked

    private void txtMenuNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMenuNameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            if (txtMenuName.getText().length() == 0)
            {
                JOptionPane.showMessageDialog(this, "Please Enter Menu Head Name");
            }
            else
            {
                cmbOperational.requestFocus();
            }
        }
    }//GEN-LAST:event_txtMenuNameKeyPressed

    private void btnNewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewMouseClicked
        // TODO add your handling code here:
        if (btnNew.getText().equalsIgnoreCase("SAVE"))
        {
            funSaveMenuHead(); //Save new Menu Head
        }
        else
        {
            funUpdateMenuHead();  //Update Existing Menu Head
        }
        fileInImg=null;
        imgBf=null;
        tempFile=null;
        bttImage.setIcon(null);
        userImagefilePath=null;
        destFile=null;
        imgFile=null;
        
        
        
        
        
        
        
        
        
        
        
        
        
        
    }//GEN-LAST:event_btnNewMouseClicked

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
        // TODO add your handling code here:
        funResetField();
    }//GEN-LAST:event_btnResetMouseClicked

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Menu Head");
    }//GEN-LAST:event_btnCancelMouseClicked

    private void cmbOperationalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbOperationalKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            btnNew.requestFocus();
        }
    }//GEN-LAST:event_cmbOperationalKeyPressed

    private void btnSubMenuSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSubMenuSaveMouseClicked
        if ("SAVE".equalsIgnoreCase(btnSubMenuSave.getText()))
        {
            //code for save
            funSaveSubMenuHead();
        }
        else
        {
            //code for update
            funUpdateSubMenuHead();
        }

    }//GEN-LAST:event_btnSubMenuSaveMouseClicked

    private void btnSubMenuResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSubMenuResetMouseClicked
        funResetSubMenuHeadTab();
    }//GEN-LAST:event_btnSubMenuResetMouseClicked

    private void btnSubMenuCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSubMenuCloseMouseClicked
        dispose();
    }//GEN-LAST:event_btnSubMenuCloseMouseClicked

    private void txtSubMenuHeadCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSubMenuHeadCodeMouseClicked

        funSelectSubMenuHeadCode();
    }//GEN-LAST:event_txtSubMenuHeadCodeMouseClicked

    private void txtSubMenuHeadShortNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSubMenuHeadShortNameMouseClicked
        // TODO add your handling code here:

        if (txtSubMenuHeadShortName.getText().length() == 0)
        {
            new frmAlfaNumericKeyBoard(this, true, "1", "Enter Sub Menu Head Short Name").setVisible(true);
            txtSubMenuHeadShortName.setText(clsGlobalVarClass.gKeyboardValue);
        }
        else
        {
            new frmAlfaNumericKeyBoard(this, true, txtSubMenuHeadShortName.getText(), "1", "Enter Sub Menu Head Short Name").setVisible(true);
            txtSubMenuHeadShortName.setText(clsGlobalVarClass.gKeyboardValue);
        }
    }//GEN-LAST:event_txtSubMenuHeadShortNameMouseClicked

    private void txtSubMenuHeadShortNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSubMenuHeadShortNameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            if (txtSubMenuHeadShortName.getText().length() == 0)
            {
                JOptionPane.showMessageDialog(this, "Please Enter Short Sub Menu Head Name");
            }
            else
            {
                cmbOperationalSubMenu.requestFocus();
            }
        }
    }//GEN-LAST:event_txtSubMenuHeadShortNameKeyPressed

    private void txtMenuHeadMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtMenuHeadMouseClicked

        funSelectMenuHead();
    }//GEN-LAST:event_txtMenuHeadMouseClicked

    private void txtSubMenuHeadNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSubMenuHeadNameMouseClicked
        // TODO add your handling code here:

        if (txtSubMenuHeadName.getText().length() == 0)
        {
            new frmAlfaNumericKeyBoard(this, true, "1", "Enter Sub Menu Head Name").setVisible(true);
            txtSubMenuHeadName.setText(clsGlobalVarClass.gKeyboardValue);
        }
        else
        {
            new frmAlfaNumericKeyBoard(this, true, txtSubMenuHeadName.getText(), "1", "Enter Sub Menu Head Name").setVisible(true);
            txtSubMenuHeadName.setText(clsGlobalVarClass.gKeyboardValue);
        }
    }//GEN-LAST:event_txtSubMenuHeadNameMouseClicked

    private void txtSubMenuHeadNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSubMenuHeadNameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            if (txtSubMenuHeadName.getText().length() == 0)
            {
                JOptionPane.showMessageDialog(this, "Please Enter Sub Menu Head Name");
            }
            else
            {
                txtSubMenuHeadShortName.requestFocus();
            }
        }
    }//GEN-LAST:event_txtSubMenuHeadNameKeyPressed

    private void cmbOperationalSubMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbOperationalSubMenuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbOperationalSubMenuActionPerformed

    private void cmbOperationalSubMenuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbOperationalSubMenuKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            btnSubMenuSave.requestFocus();
        }
    }//GEN-LAST:event_cmbOperationalSubMenuKeyPressed

    private void tblMenuHeadMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMenuHeadMouseClicked
        // TODO add your handling code here:
        SelectedMenuHeadRowNo = 0;
        SelectedMenuHeadRowNo = tblMenuHead.getSelectedRow();
    }//GEN-LAST:event_tblMenuHeadMouseClicked

    private void btnCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseClicked
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCloseMouseClicked

    private void btnUpdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUpdateMouseClicked
        // TODO add your handling code here:
        try
        {
            int exec = 0;
            for (int i = 0; i < tblMenuHead.getRowCount(); i++)
            {
                sql = "update tblmenuhd set intSequence=" + i + " where strMenuCode='" + tblMenuHead.getValueAt(i, 1) + "'";
                exec = clsGlobalVarClass.dbMysql.execute(sql);
            }
            if (exec > 0)
            {
                new frmOkPopUp(this, "Sequence Updated Successfully", "Successfull", 3).setVisible(true);
                funLoadMenuHeadTable();
            }

        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnUpdateMouseClicked

    private void lblMoveUpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMoveUpMouseClicked
        if (SelectedMenuHeadRowNo == 0)
        {
            //do nothing this is first row
        }
        else
        {

            String temSelectedMenuHeadCode = tblMenuHead.getValueAt(SelectedMenuHeadRowNo, 1).toString();
            String tempSelectedMenuHeadName = tblMenuHead.getValueAt(SelectedMenuHeadRowNo, 2).toString();
            String tempUpperMenuHeadCode = tblMenuHead.getValueAt(SelectedMenuHeadRowNo - 1, 1).toString();
            String tempUpperMenuHeadName = tblMenuHead.getValueAt(SelectedMenuHeadRowNo - 1, 2).toString();
            tblMenuHead.setValueAt(tempUpperMenuHeadCode, SelectedMenuHeadRowNo, 1);
            tblMenuHead.setValueAt(tempUpperMenuHeadName, SelectedMenuHeadRowNo, 2);
            tblMenuHead.setValueAt(temSelectedMenuHeadCode, SelectedMenuHeadRowNo - 1, 1);
            tblMenuHead.setValueAt(tempSelectedMenuHeadName, SelectedMenuHeadRowNo - 1, 2);
            SelectedMenuHeadRowNo = SelectedMenuHeadRowNo - 1;
            tblMenuHead.setRowSelectionInterval(SelectedMenuHeadRowNo, SelectedMenuHeadRowNo);
        }        // TODO add your handling code here:

    }//GEN-LAST:event_lblMoveUpMouseClicked

    private void lblMoveDownMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMoveDownMouseClicked
        // TODO add your handling code here:
        if (tblMenuHead.getRowCount() == SelectedMenuHeadRowNo + 1)
        {
            //do nothing this is last row
        }
        else
        {
            String temSelectedMenuHeadCode = tblMenuHead.getValueAt(SelectedMenuHeadRowNo, 1).toString();
            String tempSelectedMenuHeadName = tblMenuHead.getValueAt(SelectedMenuHeadRowNo, 2).toString();
            String tempLowerMenuHeadCode = tblMenuHead.getValueAt(SelectedMenuHeadRowNo + 1, 1).toString();
            String tempLowerMenuHeadName = tblMenuHead.getValueAt(SelectedMenuHeadRowNo + 1, 2).toString();
            tblMenuHead.setValueAt(tempLowerMenuHeadCode, SelectedMenuHeadRowNo, 1);
            tblMenuHead.setValueAt(tempLowerMenuHeadName, SelectedMenuHeadRowNo, 2);
            tblMenuHead.setValueAt(temSelectedMenuHeadCode, SelectedMenuHeadRowNo + 1, 1);
            tblMenuHead.setValueAt(tempSelectedMenuHeadName, SelectedMenuHeadRowNo + 1, 2);
            SelectedMenuHeadRowNo = SelectedMenuHeadRowNo + 1;
            tblMenuHead.setRowSelectionInterval(SelectedMenuHeadRowNo, SelectedMenuHeadRowNo);
        }
    }//GEN-LAST:event_lblMoveDownMouseClicked

    private void btnSubMenuSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubMenuSaveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSubMenuSaveActionPerformed

    private void txtSubMenuHeadCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSubMenuHeadCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
        {
            funSelectSubMenuHeadCode();
        }
        if (evt.getKeyCode() == 10)
        {
            txtSubMenuHeadName.requestFocus();
        }
    }//GEN-LAST:event_txtSubMenuHeadCodeKeyPressed

    private void txtMenuHeadKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMenuHeadKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            cmbOperationalSubMenu.requestFocus();
        }
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
        {
            funSelectMenuHead();
        }
    }//GEN-LAST:event_txtMenuHeadKeyPressed

    private void btnSubMenuSaveKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSubMenuSaveKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            if ("SAVE".equalsIgnoreCase(btnSubMenuSave.getText()))
            {
                //code for save
                funSaveSubMenuHead();
            }
            else
            {
                //code for update
                funUpdateSubMenuHead();
            }
        }


    }//GEN-LAST:event_btnSubMenuSaveKeyPressed

    private void txtMenuCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMenuCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
        {
            funSelectMenuHeadCode();
        }
        if (evt.getKeyCode() == 10)
        {
            txtMenuName.requestFocus();
        }

    }//GEN-LAST:event_txtMenuCodeKeyPressed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        if (btnNew.getText().equalsIgnoreCase("SAVE"))
        {
            funSaveMenuHead(); //Save new Menu Head
        }
        else
        {
            funUpdateMenuHead();  //Update Existing Menu Head
        }
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        funResetField();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Menu Head");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Menu Head");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Menu Head");
    }//GEN-LAST:event_formWindowClosing

    private void btnNewKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnNewKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            if (btnNew.getText().equalsIgnoreCase("SAVE"))
            {
                funSaveMenuHead(); //Save new Menu Head
            }
            else
            {
                funUpdateMenuHead();  //Update Existing Menu Head
            }
        }
    }//GEN-LAST:event_btnNewKeyPressed

    private void txtIMenuHeadmageKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIMenuHeadmageKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            btnBrowse.requestFocus();
        }
    }//GEN-LAST:event_txtIMenuHeadmageKeyPressed

    private void btnBrowseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBrowseMouseClicked
      try {

            JFileChooser jfc = new JFileChooser();

            if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                tempFile = jfc.getSelectedFile();
                String imagePath = tempFile.getAbsolutePath();
                userImagefilePath = imagePath.substring(imagePath.lastIndexOf("/") + 1, imagePath.length());
                txtIMenuHeadmage.setText(tempFile.getAbsolutePath());
                fileInImg = new FileInputStream(tempFile);
                bttImage.setText("");
                imgBf = funScaleImage(100, 100, userImagefilePath);
                ImageIO.write(imgBf, "png", tempFile);
                bttImage.setIcon(new javax.swing.ImageIcon(imgBf));
            }
        } catch (Exception e) {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }
        
       
    }//GEN-LAST:event_btnBrowseMouseClicked

    private void btnBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBrowseActionPerformed

    private void btnBrowseKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnBrowseKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            txtMenuName.requestFocus();
        }
    }//GEN-LAST:event_btnBrowseKeyPressed

    /**
     *
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBrowse;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSubMenuClose;
    private javax.swing.JButton btnSubMenuReset;
    private javax.swing.JButton btnSubMenuSave;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JButton bttImage;
    private javax.swing.JComboBox cmbOperational;
    private javax.swing.JComboBox cmbOperationalSubMenu;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblMenuHeadImage;
    private javax.swing.JLabel lblMenuHeadMaster;
    private javax.swing.JLabel lblMenuHeadName;
    private javax.swing.JLabel lblMenuHeadSeq;
    private javax.swing.JLabel lblMenuName;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblMoveDown;
    private javax.swing.JLabel lblMoveUp;
    private javax.swing.JLabel lblOperation;
    private javax.swing.JLabel lblOperational;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblSelectMenuHead;
    private javax.swing.JLabel lblSubMenuHeadCode;
    private javax.swing.JLabel lblSubMenuHeadMast;
    private javax.swing.JLabel lblSubMenuHeadName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JLabel lblmenuCode;
    private javax.swing.JLabel lblsubMenuShortName;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JScrollPane srollPane;
    private javax.swing.JPanel tabMenuHead;
    private javax.swing.JPanel tabMenuHeadSeq;
    private javax.swing.JTabbedPane tabPane;
    private javax.swing.JPanel tabSubMenuHead;
    private javax.swing.JTable tblMenuHead;
    private javax.swing.JTextField txtIMenuHeadmage;
    private javax.swing.JTextField txtMenuCode;
    private javax.swing.JTextField txtMenuHead;
    private javax.swing.JTextField txtMenuName;
    private javax.swing.JTextField txtSubMenuHeadCode;
    private javax.swing.JTextField txtSubMenuHeadName;
    private javax.swing.JTextField txtSubMenuHeadShortName;
    // End of variables declaration//GEN-END:variables

    private BufferedImage funScaleImage(int WIDTH, int HEIGHT, String filename)
    {
        BufferedImage bi = null;
        try
        {
            ImageIcon ii = new ImageIcon(filename);//path to image
            bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
            Graphics2D gra2d = (Graphics2D) bi.createGraphics();
            gra2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
            gra2d.drawImage(ii.getImage(), 0, 0, WIDTH, HEIGHT, null);
        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
            return null;
        }
        return bi;
    }
    
    
}
