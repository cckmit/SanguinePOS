package com.sanguine.updatespos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author ajjim
 */
public class UpdateSPOS
{

    private static String[] configData;
    private static String[] tempData;
    private static File file;
    private static BufferedReader br;
    private static int i = 0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args )
    {
        UpdateSPOS objUpdateSPOS = new UpdateSPOS();
        String serverJarFilePath = objUpdateSPOS.funReadConfigFile();
        if (!serverJarFilePath.isEmpty())
        {
            i = objUpdateSPOS.funReadJarFile(serverJarFilePath);
        }

    }

    private String funReadConfigFile()
    {
        String serverJarFilePath = "";
        try
        {
            int i = 0;
            configData = new String[20];
            tempData = new String[20];
            file = new File(System.getProperty("user.dir") + "/DBConfigFile.txt");
            br = new BufferedReader(new FileReader(file));
            String fileData;
            while ((fileData = br.readLine()) != null)
            {
                String[] split = fileData.split("=");
                if (split.length > 1)
                {
                    tempData[i] = split[0];
                    configData[i] = split[1];
                    i++;
                }
            }

            serverJarFilePath = configData[15].trim();

            System.out.println("sourceJarFilePath=" + serverJarFilePath + "\n");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            return serverJarFilePath;
        }
    }

    private static int funReadJarFile(String serverJarFilePath)
    {
        int result = 0;
        File localJarFile = null;
        try
        {
            SimpleDateFormat ddMMyyyyDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

            //local
            System.out.println("\n\n----------------Client Machine--------------------\n\n");

            String localPath = System.getProperty("user.dir") + "\\prjSPOSStartUp.jar";
            localJarFile = new File(localPath);
            System.out.println("Local Jar File Path= " + localJarFile.getPath());
            long localLongDate = localJarFile.lastModified();
            if (localLongDate == 0)
            {
                JOptionPane.showMessageDialog(null, localJarFile + " File Not Found.");
                return 0;
            }

            Date localJarLastModifiedDate = new Date(localJarFile.lastModified());
            String localJarFormatedLastModifiedDate = ddMMyyyyDateFormat.format(localJarLastModifiedDate);
            System.out.println("Local Jar File Last Modified Date= " + localJarFormatedLastModifiedDate);
            //remote
            System.out.println("\n\n----------------Server Machine--------------------\n\n");
            File remoteJarFile = new File("\\" + serverJarFilePath + "\\prjSPOSStartUp.jar");            
            System.out.println("Remote Jar File Path= " + remoteJarFile.getPath());
            long remoteLongDate = remoteJarFile.lastModified();
            if (remoteLongDate == 0)
            {
                //JOptionPane.showMessageDialog(null,remoteJarFile+" File Not Found.");
                return 0;
            }

            Date remoteJarLastModifiedDate = new Date(remoteJarFile.lastModified());
            String remoteJarFormatedLastModifiedDate = ddMMyyyyDateFormat.format(remoteJarLastModifiedDate);
            System.out.println("Remote Jar File Last Modified Date= " + remoteJarFormatedLastModifiedDate);

            if (localLongDate < remoteLongDate)
            {
                int i = JOptionPane.showConfirmDialog(null, "<html>SPOS New Updates Are Available !!!<br><br>Do you want to update it?</html>", "SPOS New Upadates!", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                //0=yes;1=no;2=cancel
                if (i == 0)
                {
                    result = funUpdateJarFiles(serverJarFilePath);

                    String currentPath = System.getProperty("user.dir");
//                    System.out.println("currentPath=>"+currentPath+"\\prjSPOSStartUp.jar");
                    try
                    {
                        Runtime.getRuntime().exec("cmd /c " + currentPath + "\\prjSPOSStartUp.jar");
                    }
                    catch (IOException ex)
                    {
                        ex.printStackTrace();
                    }
                }
                else
                {
                    String currentPath = System.getProperty("user.dir");
//                    System.out.println("currentPath=>"+currentPath+"\\prjSPOSStartUp.jar");
                    try
                    {
                        Runtime.getRuntime().exec("cmd /c " + currentPath + "\\prjSPOSStartUp.jar");
                    }
                    catch (IOException ex)
                    {
                        ex.printStackTrace();
                    }
                    System.exit(0);
                }
            }
            else
            {
                result = 1;
                //JOptionPane.showMessageDialog(null, "SPOS Are Upto Date.");
                String currentPath = System.getProperty("user.dir");
//                    System.out.println("currentPath=>"+currentPath+"\\prjSPOSStartUp.jar");
                try
                {
                    Runtime.getRuntime().exec("cmd /c " + currentPath + "\\prjSPOSStartUp.jar");
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }

        }
        catch (Exception pe)
        {
            pe.printStackTrace();
        }
        finally
        {
            return result;
        }
    }

    private static int funUpdateJarFiles(String serverJarFilePath)
    {
        int result = 0;
        //source jar=remote jar
        //destination jar=local jar        
        String sourcePath = serverJarFilePath;
        String destinationPath = System.getProperty("user.dir");

        Path sourceJarPath = null;
        Path destinationJarPath = null;
        int i = 1;
        for (; i <= 6; i++)
        {
            switch (i)
            {
                //for POS Startup
                case 6:
                    sourceJarPath = Paths.get(sourcePath + "\\prjSPOSStartUp.jar");
                    destinationJarPath = Paths.get(destinationPath + "\\prjSPOSStartUp.jar");
                    funCopyAndReplaceFiles(sourceJarPath, destinationJarPath);
                    break;
                //for POSLicence
                case 5:
                    sourceJarPath = Paths.get(sourcePath + "\\lib\\prjSPOSLicence.jar");
                    destinationJarPath = Paths.get(destinationPath + "\\lib\\prjSPOSLicence.jar");
                    funCopyAndReplaceFiles(sourceJarPath, destinationJarPath);
                    break;
                //for POSGlobal
                case 4:
                    sourceJarPath = Paths.get(sourcePath + "\\lib\\prjSPOSGlobal.jar");
                    destinationJarPath = Paths.get(destinationPath + "\\lib\\prjSPOSGlobal.jar");
                    funCopyAndReplaceFiles(sourceJarPath, destinationJarPath);
                    break;
                //for POSMaster
                case 3:
                    sourceJarPath = Paths.get(sourcePath + "\\lib\\prjSPOSMasters.jar");
                    destinationJarPath = Paths.get(destinationPath + "\\lib\\prjSPOSMasters.jar");
                    funCopyAndReplaceFiles(sourceJarPath, destinationJarPath);
                    break;
                //for POSTransaction
                case 2:
                    sourceJarPath = Paths.get(sourcePath + "\\lib\\prjSPOSTransactions.jar");
                    destinationJarPath = Paths.get(destinationPath + "\\lib\\prjSPOSTransactions.jar");
                    funCopyAndReplaceFiles(sourceJarPath, destinationJarPath);
                    break;
                //for POSReport
                case 1:
                    sourceJarPath = Paths.get(sourcePath + "\\lib\\prjSPOSReports.jar");
                    destinationJarPath = Paths.get(destinationPath + "\\lib\\prjSPOSReports.jar");
                    funCopyAndReplaceFiles(sourceJarPath, destinationJarPath);
                    break;

            }
        }
        if (i > 6)
        {
            result = 1;
            JOptionPane.showMessageDialog(null, "Files Are Updated Successfully.");
        }
        return result;
    }

    private static void funCopyAndReplaceFiles(Path sourceJarPath, Path destinationJarPath)
    {
        try
        {
            //Files.move(sourceJarPath, destinationJarPath, StandardCopyOption.REPLACE_EXISTING);
            Files.copy(sourceJarPath, destinationJarPath, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
