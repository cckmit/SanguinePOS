/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JToggleButton;

public class panelAlfaNumericKeyboard extends javax.swing.JPanel 
{
    private String textValue="",formName;
    private frmMakeKOT objMakeKOT;
    private frmDirectBiller objDirectBiller;
    
    
    
    
    public panelAlfaNumericKeyboard(Object objParent, String frmName) 
    {
        if (clsGlobalVarClass.gTouchScreenMode) 
        {
            try {
                initComponents();
                
                textValue="";
                formName=frmName;
                if(formName.equals("Make KOT"))
                {
                    objMakeKOT=(frmMakeKOT)objParent;
                }
                else if(formName.equals("Direct Biller"))
                {
                    objDirectBiller=(frmDirectBiller)objParent;
                }
                this.revalidate();
                this.repaint();
                
                capsON();
                funSetShortCutKeys();
                //setLocationRelativeTo(objParent);
                super.setVisible(clsGlobalVarClass.gTouchScreenMode);
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else 
        {
            setVisible(clsGlobalVarClass.gTouchScreenMode);
            clsGlobalVarClass.gKeyboardValue="";
        }
    }
    
    private void funSetShortCutKeys() {
        btnclose.setMnemonic('c');
    }    

    public void capsON() {
        btnq.setText("Q");
        btnw.setText("W");
        btne.setText("E");
        btnr.setText("R");
        btnt.setText("T");
        btny.setText("Y");
        btnu.setText("U");
        btni.setText("I");
        btno.setText("O");
        btnp.setText("P");
        btna.setText("A");
        btns.setText("S");
        btnd.setText("D");
        btnf.setText("F");
        btnh.setText("H");
        btng.setText("G");
        btnj.setText("J");
        btnk.setText("K");
        btnl.setText("L");
        btnz.setText("Z");
        btnx.setText("X");
        btnc.setText("C");
        btnv.setText("V");
        btnb.setText("B");
        btnn.setText("N");
        btnm.setText("M");
    }

    public void capOff() {
        btnq.setText("q");
        btnw.setText("w");
        btne.setText("e");
        btnr.setText("r");
        btnt.setText("t");
        btny.setText("y");
        btnu.setText("u");
        btni.setText("i");
        btno.setText("o");
        btnp.setText("p");
        btna.setText("a");
        btns.setText("s");
        btnd.setText("d");
        btnf.setText("f");
        btng.setText("g");
        btnh.setText("h");
        btnj.setText("j");
        btnk.setText("k");
        btnl.setText("l");
        btnz.setText("z");
        btnx.setText("x");
        btnc.setText("c");
        btnv.setText("v");
        btnb.setText("b");
        btnn.setText("n");
        btnm.setText("m");
    }

    @Override
    public void setVisible(boolean b) {

    }
    
    
    private void funGenerateString(String text)
    {
        textValue+=text;
        
        if(formName.equals("Make KOT"))
        {
            objMakeKOT.funSetKeyBoardValueOnPLUTextBox(textValue);
        }
        else if(formName.equals("Direct Biller"))
        {
            objDirectBiller.funSetKeyBoardValueOnPLUTextBox(textValue);
        }
    }
    
    private void funPressEnterBtn() {
        try {
            clsGlobalVarClass.gKeyboardValue = textValue;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void pressShiftbtnChangeCaption() {
        try {
            btnTilda.setText("~");
            btn1.setText("!");
            btn2.setText("@");
            btn3.setText("#");
            btn4.setText("$");
            btn5.setText("%");
            btn6.setText("^");
            btn7.setText("&");
            btn8.setText("*");
            btn9.setText("(");
            btn0.setText(")");
            btnhypen.setText("_");
            btnequal.setText("+");
            btncomma.setText("<");
            btndot.setText(">");
            btnleftSquare.setText("{");
            btnRigthSqure.setText("}");
            btnbackslace.setText("|");
            btnforwardslace.setText("?");
            btnsemecolon.setText(":");
            btnapostropes.setText("\"");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void relesedShiftbtnChangeCaption() {
        try {
            btnTilda.setText("`");
            btn1.setText("1");
            btn2.setText("2");
            btn3.setText("3");
            btn4.setText("4");
            btn5.setText("5");
            btn6.setText("6");
            btn7.setText("7");
            btn8.setText("8");
            btn9.setText("9");
            btn0.setText("0");
            btnhypen.setText("-");
            btnequal.setText("=");
            btncomma.setText(",");
            btndot.setText(".");
            btnleftSquare.setText("[");
            btnRigthSqure.setText("]");
            btnbackslace.setText("\\");
            btnforwardslace.setText("/");
            btnsemecolon.setText(";");
            btnapostropes.setText("'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unselectedShiftbtn() {
        try {
            if (btnleftShift.isSelected() && btnrightshift.isSelected()) {
                btnleftShift.setSelected(false);
                btnrightshift.setSelected(false);
                relesedShiftbtnChangeCaption();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelMain = new javax.swing.JPanel();
        btnbackSpace = new javax.swing.JButton();
        btnequal = new javax.swing.JButton();
        btnhypen = new javax.swing.JButton();
        btn0 = new javax.swing.JButton();
        btn9 = new javax.swing.JButton();
        btn8 = new javax.swing.JButton();
        btn7 = new javax.swing.JButton();
        btn6 = new javax.swing.JButton();
        btn5 = new javax.swing.JButton();
        btn4 = new javax.swing.JButton();
        btn3 = new javax.swing.JButton();
        btn2 = new javax.swing.JButton();
        btn1 = new javax.swing.JButton();
        btnTilda = new javax.swing.JButton();
        btnenter = new javax.swing.JButton();
        btnSpace = new javax.swing.JButton();
        btns = new javax.swing.JButton();
        btnq = new javax.swing.JButton();
        btnx = new javax.swing.JButton();
        btng = new javax.swing.JButton();
        btnCtrl = new javax.swing.JButton();
        btnz = new javax.swing.JButton();
        btni = new javax.swing.JButton();
        btno = new javax.swing.JButton();
        btnw = new javax.swing.JButton();
        btnapostropes = new javax.swing.JButton();
        btne = new javax.swing.JButton();
        btnp = new javax.swing.JButton();
        btnu = new javax.swing.JButton();
        btny = new javax.swing.JButton();
        btnt = new javax.swing.JButton();
        btnr = new javax.swing.JButton();
        btnh = new javax.swing.JButton();
        btnTab = new javax.swing.JButton();
        btna = new javax.swing.JButton();
        btnb = new javax.swing.JButton();
        btnf = new javax.swing.JButton();
        btnc = new javax.swing.JButton();
        btnv = new javax.swing.JButton();
        btnd = new javax.swing.JButton();
        btnn = new javax.swing.JButton();
        btnj = new javax.swing.JButton();
        btnm = new javax.swing.JButton();
        btnk = new javax.swing.JButton();
        btncomma = new javax.swing.JButton();
        btnl = new javax.swing.JButton();
        btndot = new javax.swing.JButton();
        btnforwardslace = new javax.swing.JButton();
        btnRigthSqure = new javax.swing.JButton();
        btnclose = new javax.swing.JButton();
        btnsemecolon = new javax.swing.JButton();
        btnbackslace = new javax.swing.JButton();
        btnalt = new javax.swing.JButton();
        btnleftSquare = new javax.swing.JButton();
        btnCaps = new javax.swing.JToggleButton();
        btnleftShift = new javax.swing.JToggleButton();
        btnrightshift = new javax.swing.JToggleButton();
        lblBodyImg = new javax.swing.JLabel();

        panelMain.setBackground(new java.awt.Color(255, 255, 255));
        panelMain.setPreferredSize(new java.awt.Dimension(801, 330));
        panelMain.setLayout(null);

        btnbackSpace.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnbackSpace.setForeground(new java.awt.Color(255, 255, 255));
        btnbackSpace.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgBackspaceBtn1.png"))); // NOI18N
        btnbackSpace.setText("BACK");
        btnbackSpace.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnbackSpace.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnbackSpace.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgBackspaceBtn2.png"))); // NOI18N
        btnbackSpace.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnbackSpaceMouseClicked(evt);
            }
        });
        panelMain.add(btnbackSpace);
        btnbackSpace.setBounds(660, 10, 120, 40);

        btnequal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnequal.setForeground(new java.awt.Color(255, 255, 255));
        btnequal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btnequal.setText("=");
        btnequal.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnequal.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnequal.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btnequal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnequalActionPerformed(evt);
            }
        });
        panelMain.add(btnequal);
        btnequal.setBounds(610, 10, 50, 40);

        btnhypen.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnhypen.setForeground(new java.awt.Color(255, 255, 255));
        btnhypen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btnhypen.setText("-");
        btnhypen.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnhypen.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnhypen.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btnhypen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnhypenActionPerformed(evt);
            }
        });
        panelMain.add(btnhypen);
        btnhypen.setBounds(560, 10, 50, 40);

        btn0.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn0.setForeground(new java.awt.Color(255, 255, 255));
        btn0.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btn0.setText("0");
        btn0.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btn0.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn0.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btn0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn0ActionPerformed(evt);
            }
        });
        panelMain.add(btn0);
        btn0.setBounds(510, 10, 50, 40);

        btn9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn9.setForeground(new java.awt.Color(255, 255, 255));
        btn9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btn9.setText("9");
        btn9.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btn9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn9.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btn9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn9ActionPerformed(evt);
            }
        });
        panelMain.add(btn9);
        btn9.setBounds(460, 10, 50, 40);

        btn8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn8.setForeground(new java.awt.Color(255, 255, 255));
        btn8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btn8.setText("8");
        btn8.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btn8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn8.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btn8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn8ActionPerformed(evt);
            }
        });
        panelMain.add(btn8);
        btn8.setBounds(410, 10, 50, 40);

        btn7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn7.setForeground(new java.awt.Color(255, 255, 255));
        btn7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btn7.setText("7");
        btn7.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btn7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn7.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btn7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn7ActionPerformed(evt);
            }
        });
        panelMain.add(btn7);
        btn7.setBounds(360, 10, 50, 40);

        btn6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn6.setForeground(new java.awt.Color(255, 255, 255));
        btn6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btn6.setText("6");
        btn6.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btn6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn6.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btn6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn6ActionPerformed(evt);
            }
        });
        panelMain.add(btn6);
        btn6.setBounds(310, 10, 50, 40);

        btn5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn5.setForeground(new java.awt.Color(255, 255, 255));
        btn5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btn5.setText("5");
        btn5.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btn5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn5.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btn5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn5ActionPerformed(evt);
            }
        });
        panelMain.add(btn5);
        btn5.setBounds(260, 10, 50, 40);

        btn4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn4.setForeground(new java.awt.Color(255, 255, 255));
        btn4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btn4.setText("4");
        btn4.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btn4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn4.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btn4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn4ActionPerformed(evt);
            }
        });
        panelMain.add(btn4);
        btn4.setBounds(210, 10, 50, 40);

        btn3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn3.setForeground(new java.awt.Color(255, 255, 255));
        btn3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btn3.setText("3");
        btn3.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btn3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn3.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn3ActionPerformed(evt);
            }
        });
        panelMain.add(btn3);
        btn3.setBounds(160, 10, 50, 40);

        btn2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn2.setForeground(new java.awt.Color(255, 255, 255));
        btn2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btn2.setText("2");
        btn2.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btn2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn2ActionPerformed(evt);
            }
        });
        panelMain.add(btn2);
        btn2.setBounds(110, 10, 50, 40);

        btn1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn1.setForeground(new java.awt.Color(255, 255, 255));
        btn1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btn1.setText("1");
        btn1.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btn1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn1ActionPerformed(evt);
            }
        });
        panelMain.add(btn1);
        btn1.setBounds(60, 10, 50, 40);

        btnTilda.setBackground(new java.awt.Color(255, 255, 255));
        btnTilda.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnTilda.setForeground(new java.awt.Color(255, 255, 255));
        btnTilda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btnTilda.setText("`");
        btnTilda.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnTilda.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTilda.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btnTilda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTildaActionPerformed(evt);
            }
        });
        panelMain.add(btnTilda);
        btnTilda.setBounds(10, 10, 50, 40);

        btnenter.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnenter.setForeground(new java.awt.Color(255, 255, 255));
        btnenter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardEnter1.png"))); // NOI18N
        btnenter.setText("Enter");
        btnenter.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnenter.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnenter.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardEnter2.png"))); // NOI18N
        btnenter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnenterMouseClicked(evt);
            }
        });
        btnenter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnenterActionPerformed(evt);
            }
        });
        panelMain.add(btnenter);
        btnenter.setBounds(660, 50, 120, 80);

        btnSpace.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSpace.setForeground(new java.awt.Color(255, 255, 255));
        btnSpace.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgSpacebar1.png"))); // NOI18N
        btnSpace.setText("Space");
        btnSpace.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnSpace.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSpace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSpaceActionPerformed(evt);
            }
        });
        panelMain.add(btnSpace);
        btnSpace.setBounds(110, 170, 500, 40);

        btns.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btns.setForeground(new java.awt.Color(255, 255, 255));
        btns.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btns.setText("s");
        btns.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btns.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btns.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btns.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsActionPerformed(evt);
            }
        });
        panelMain.add(btns);
        btns.setBounds(160, 90, 50, 40);

        btnq.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnq.setForeground(new java.awt.Color(255, 255, 255));
        btnq.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btnq.setText("q");
        btnq.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnq.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnq.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btnq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnqActionPerformed(evt);
            }
        });
        panelMain.add(btnq);
        btnq.setBounds(110, 50, 50, 40);

        btnx.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnx.setForeground(new java.awt.Color(255, 255, 255));
        btnx.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btnx.setText("x");
        btnx.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnx.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnx.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btnx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnxActionPerformed(evt);
            }
        });
        panelMain.add(btnx);
        btnx.setBounds(160, 130, 50, 40);

        btng.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btng.setForeground(new java.awt.Color(255, 255, 255));
        btng.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btng.setText("g");
        btng.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btng.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btng.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btng.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btngActionPerformed(evt);
            }
        });
        panelMain.add(btng);
        btng.setBounds(310, 90, 50, 40);

        btnCtrl.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCtrl.setForeground(new java.awt.Color(255, 255, 255));
        btnCtrl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtnShift1.png"))); // NOI18N
        btnCtrl.setText("Ctrl");
        btnCtrl.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnCtrl.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCtrl.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtnShift2.png"))); // NOI18N
        panelMain.add(btnCtrl);
        btnCtrl.setBounds(10, 170, 100, 40);

        btnz.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnz.setForeground(new java.awt.Color(255, 255, 255));
        btnz.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btnz.setText("z");
        btnz.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnz.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnz.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btnz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnzActionPerformed(evt);
            }
        });
        panelMain.add(btnz);
        btnz.setBounds(110, 130, 50, 40);

        btni.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btni.setForeground(new java.awt.Color(255, 255, 255));
        btni.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btni.setText("i");
        btni.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btni.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btni.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btni.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btniActionPerformed(evt);
            }
        });
        panelMain.add(btni);
        btni.setBounds(460, 50, 50, 40);

        btno.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btno.setForeground(new java.awt.Color(255, 255, 255));
        btno.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btno.setText("o");
        btno.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btno.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btno.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnoActionPerformed(evt);
            }
        });
        panelMain.add(btno);
        btno.setBounds(510, 50, 50, 40);

        btnw.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnw.setForeground(new java.awt.Color(255, 255, 255));
        btnw.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btnw.setText("w");
        btnw.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnw.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnw.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btnw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnwActionPerformed(evt);
            }
        });
        panelMain.add(btnw);
        btnw.setBounds(160, 50, 50, 40);

        btnapostropes.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnapostropes.setForeground(new java.awt.Color(255, 255, 255));
        btnapostropes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btnapostropes.setText("'");
        btnapostropes.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnapostropes.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnapostropes.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btnapostropes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnapostropesActionPerformed(evt);
            }
        });
        panelMain.add(btnapostropes);
        btnapostropes.setBounds(610, 50, 50, 40);

        btne.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btne.setForeground(new java.awt.Color(255, 255, 255));
        btne.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btne.setText("e");
        btne.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btne.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btne.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btne.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneActionPerformed(evt);
            }
        });
        panelMain.add(btne);
        btne.setBounds(210, 50, 50, 40);

        btnp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnp.setForeground(new java.awt.Color(255, 255, 255));
        btnp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btnp.setText("p");
        btnp.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnp.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btnp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnpActionPerformed(evt);
            }
        });
        panelMain.add(btnp);
        btnp.setBounds(560, 50, 50, 40);

        btnu.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnu.setForeground(new java.awt.Color(255, 255, 255));
        btnu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btnu.setText("u");
        btnu.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnu.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnu.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btnu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnuActionPerformed(evt);
            }
        });
        panelMain.add(btnu);
        btnu.setBounds(410, 50, 50, 40);

        btny.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btny.setForeground(new java.awt.Color(255, 255, 255));
        btny.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btny.setText("y");
        btny.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btny.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btny.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btny.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnyActionPerformed(evt);
            }
        });
        panelMain.add(btny);
        btny.setBounds(360, 50, 50, 40);

        btnt.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnt.setForeground(new java.awt.Color(255, 255, 255));
        btnt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btnt.setText("t");
        btnt.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnt.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnt.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btntActionPerformed(evt);
            }
        });
        panelMain.add(btnt);
        btnt.setBounds(310, 50, 50, 40);

        btnr.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnr.setForeground(new java.awt.Color(255, 255, 255));
        btnr.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btnr.setText("r");
        btnr.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnr.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnr.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btnr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnrActionPerformed(evt);
            }
        });
        panelMain.add(btnr);
        btnr.setBounds(260, 50, 50, 40);

        btnh.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnh.setForeground(new java.awt.Color(255, 255, 255));
        btnh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btnh.setText("h");
        btnh.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnh.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnh.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btnh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnhActionPerformed(evt);
            }
        });
        panelMain.add(btnh);
        btnh.setBounds(360, 90, 50, 40);

        btnTab.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnTab.setForeground(new java.awt.Color(255, 255, 255));
        btnTab.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtnShift1.png"))); // NOI18N
        btnTab.setText("Tab");
        btnTab.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnTab.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTab.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtnShift2.png"))); // NOI18N
        panelMain.add(btnTab);
        btnTab.setBounds(10, 50, 100, 40);

        btna.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btna.setForeground(new java.awt.Color(255, 255, 255));
        btna.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btna.setText("a");
        btna.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btna.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btna.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btna.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaActionPerformed(evt);
            }
        });
        panelMain.add(btna);
        btna.setBounds(110, 90, 50, 40);

        btnb.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnb.setForeground(new java.awt.Color(255, 255, 255));
        btnb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btnb.setText("b");
        btnb.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnb.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnb.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btnb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbActionPerformed(evt);
            }
        });
        panelMain.add(btnb);
        btnb.setBounds(310, 130, 50, 40);

        btnf.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnf.setForeground(new java.awt.Color(255, 255, 255));
        btnf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btnf.setText("f");
        btnf.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnf.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnf.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btnf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnfActionPerformed(evt);
            }
        });
        panelMain.add(btnf);
        btnf.setBounds(260, 90, 50, 40);

        btnc.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnc.setForeground(new java.awt.Color(255, 255, 255));
        btnc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btnc.setText("c");
        btnc.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnc.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnc.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btnc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncActionPerformed(evt);
            }
        });
        panelMain.add(btnc);
        btnc.setBounds(210, 130, 50, 40);

        btnv.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnv.setForeground(new java.awt.Color(255, 255, 255));
        btnv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btnv.setText("v");
        btnv.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnv.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnv.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btnv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnvActionPerformed(evt);
            }
        });
        panelMain.add(btnv);
        btnv.setBounds(260, 130, 50, 40);

        btnd.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnd.setForeground(new java.awt.Color(255, 255, 255));
        btnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btnd.setText("d");
        btnd.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnd.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndActionPerformed(evt);
            }
        });
        panelMain.add(btnd);
        btnd.setBounds(210, 90, 50, 40);

        btnn.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnn.setForeground(new java.awt.Color(255, 255, 255));
        btnn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btnn.setText("n");
        btnn.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnn.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btnn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnnActionPerformed(evt);
            }
        });
        panelMain.add(btnn);
        btnn.setBounds(360, 130, 50, 40);

        btnj.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnj.setForeground(new java.awt.Color(255, 255, 255));
        btnj.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btnj.setText("j");
        btnj.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnj.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnj.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btnj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnjActionPerformed(evt);
            }
        });
        panelMain.add(btnj);
        btnj.setBounds(410, 90, 50, 40);

        btnm.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnm.setForeground(new java.awt.Color(255, 255, 255));
        btnm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btnm.setText("m");
        btnm.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnm.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnm.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btnm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmActionPerformed(evt);
            }
        });
        panelMain.add(btnm);
        btnm.setBounds(410, 130, 50, 40);

        btnk.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnk.setForeground(new java.awt.Color(255, 255, 255));
        btnk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btnk.setText("k");
        btnk.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnk.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnk.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btnk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnkActionPerformed(evt);
            }
        });
        panelMain.add(btnk);
        btnk.setBounds(460, 90, 50, 40);

        btncomma.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btncomma.setForeground(new java.awt.Color(255, 255, 255));
        btncomma.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btncomma.setText(",");
        btncomma.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btncomma.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btncomma.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btncomma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncommaActionPerformed(evt);
            }
        });
        panelMain.add(btncomma);
        btncomma.setBounds(460, 130, 50, 40);

        btnl.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnl.setForeground(new java.awt.Color(255, 255, 255));
        btnl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btnl.setText("l");
        btnl.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnl.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnl.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btnl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnlActionPerformed(evt);
            }
        });
        panelMain.add(btnl);
        btnl.setBounds(510, 90, 50, 40);

        btndot.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btndot.setForeground(new java.awt.Color(255, 255, 255));
        btndot.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btndot.setText(".");
        btndot.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btndot.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btndot.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btndot.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btndotMouseClicked(evt);
            }
        });
        btndot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndotActionPerformed(evt);
            }
        });
        panelMain.add(btndot);
        btndot.setBounds(510, 130, 50, 40);

        btnforwardslace.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnforwardslace.setForeground(new java.awt.Color(255, 255, 255));
        btnforwardslace.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btnforwardslace.setText("/");
        btnforwardslace.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnforwardslace.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnforwardslace.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btnforwardslace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnforwardslaceActionPerformed(evt);
            }
        });
        panelMain.add(btnforwardslace);
        btnforwardslace.setBounds(560, 130, 50, 40);

        btnRigthSqure.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnRigthSqure.setForeground(new java.awt.Color(255, 255, 255));
        btnRigthSqure.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btnRigthSqure.setText("]");
        btnRigthSqure.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnRigthSqure.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRigthSqure.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btnRigthSqure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRigthSqureActionPerformed(evt);
            }
        });
        panelMain.add(btnRigthSqure);
        btnRigthSqure.setBounds(610, 90, 50, 40);

        btnclose.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnclose.setForeground(new java.awt.Color(255, 255, 255));
        btnclose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtnShift1.png"))); // NOI18N
        btnclose.setText("Close");
        btnclose.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnclose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnclose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtnShift2.png"))); // NOI18N
        btnclose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btncloseMouseClicked(evt);
            }
        });
        btnclose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncloseActionPerformed(evt);
            }
        });
        panelMain.add(btnclose);
        btnclose.setBounds(690, 170, 90, 40);

        btnsemecolon.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnsemecolon.setForeground(new java.awt.Color(255, 255, 255));
        btnsemecolon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btnsemecolon.setText(";");
        btnsemecolon.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnsemecolon.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnsemecolon.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btnsemecolon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsemecolonActionPerformed(evt);
            }
        });
        panelMain.add(btnsemecolon);
        btnsemecolon.setBounds(660, 130, 50, 40);

        btnbackslace.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnbackslace.setForeground(new java.awt.Color(255, 255, 255));
        btnbackslace.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
        btnbackslace.setText("\\");
            btnbackslace.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
            btnbackslace.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            btnbackslace.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
            btnbackslace.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnbackslaceActionPerformed(evt);
                }
            });
            panelMain.add(btnbackslace);
            btnbackslace.setBounds(610, 130, 50, 40);

            btnalt.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            btnalt.setForeground(new java.awt.Color(255, 255, 255));
            btnalt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtnShift1.png"))); // NOI18N
            btnalt.setText("Clear");
            btnalt.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
            btnalt.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            btnalt.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtnShift2.png"))); // NOI18N
            btnalt.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    btnaltMouseClicked(evt);
                }
            });
            btnalt.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnaltActionPerformed(evt);
                }
            });
            panelMain.add(btnalt);
            btnalt.setBounds(610, 170, 80, 40);

            btnleftSquare.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            btnleftSquare.setForeground(new java.awt.Color(255, 255, 255));
            btnleftSquare.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn1.png"))); // NOI18N
            btnleftSquare.setText("[");
            btnleftSquare.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
            btnleftSquare.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            btnleftSquare.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
            btnleftSquare.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnleftSquareActionPerformed(evt);
                }
            });
            panelMain.add(btnleftSquare);
            btnleftSquare.setBounds(560, 90, 50, 40);

            btnCaps.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            btnCaps.setForeground(new java.awt.Color(255, 255, 255));
            btnCaps.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtnShift1.png"))); // NOI18N
            btnCaps.setSelected(true);
            btnCaps.setText("Caps");
            btnCaps.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
            btnCaps.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            btnCaps.setPreferredSize(new java.awt.Dimension(110, 50));
            btnCaps.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtnShift2.png"))); // NOI18N
            btnCaps.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnCapsActionPerformed(evt);
                }
            });
            panelMain.add(btnCaps);
            btnCaps.setBounds(10, 90, 100, 40);

            btnleftShift.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            btnleftShift.setForeground(new java.awt.Color(255, 255, 255));
            btnleftShift.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtnShift1.png"))); // NOI18N
            btnleftShift.setText("Shift");
            btnleftShift.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), new java.awt.Color(51, 51, 255)));
            btnleftShift.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            btnleftShift.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtnShift2.png"))); // NOI18N
            btnleftShift.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnleftShiftActionPerformed(evt);
                }
            });
            panelMain.add(btnleftShift);
            btnleftShift.setBounds(10, 130, 100, 40);

            btnrightshift.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            btnrightshift.setForeground(new java.awt.Color(255, 255, 255));
            btnrightshift.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtnShift1.png"))); // NOI18N
            btnrightshift.setText("Shift");
            btnrightshift.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 255)));
            btnrightshift.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            btnrightshift.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtnShift2.png"))); // NOI18N
            btnrightshift.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnrightshiftActionPerformed(evt);
                }
            });
            panelMain.add(btnrightshift);
            btnrightshift.setBounds(710, 130, 70, 40);

            lblBodyImg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgBGJPOS.png"))); // NOI18N
            lblBodyImg.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
            lblBodyImg.setMaximumSize(new java.awt.Dimension(800, 300));
            lblBodyImg.setMinimumSize(new java.awt.Dimension(800, 300));
            lblBodyImg.setPreferredSize(new java.awt.Dimension(800, 300));
            panelMain.add(lblBodyImg);
            lblBodyImg.setBounds(10, 10, 770, 200);

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
            this.setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(panelMain, javax.swing.GroupLayout.PREFERRED_SIZE, 779, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(panelMain, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
            );
        }// </editor-fold>//GEN-END:initComponents

    private void btnbackSpaceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnbackSpaceMouseClicked
        // TODO add your handling code here:
        try {
            if (textValue.length() > 0) {
                StringBuilder sb = new StringBuilder(textValue);
                sb.delete(textValue.length() - 1, textValue.length());
                String s = sb.toString();
                textValue=s;

                if(formName.equals("Make KOT"))
                {
                    objMakeKOT.funSetKeyBoardValueOnPLUTextBox(textValue);
                }
                else if(formName.equals("Direct Biller"))
                {
                    objDirectBiller.funSetKeyBoardValueOnPLUTextBox(textValue);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnbackSpaceMouseClicked

    private void btnequalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnequalActionPerformed
        // TODO add your handling code here:
        funGenerateString(btnequal.getActionCommand());
    }//GEN-LAST:event_btnequalActionPerformed

    private void btnhypenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnhypenActionPerformed
        funGenerateString(btnhypen.getActionCommand());
    }//GEN-LAST:event_btnhypenActionPerformed

    private void btn0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn0ActionPerformed

        funGenerateString(btn0.getActionCommand());
    }//GEN-LAST:event_btn0ActionPerformed

    private void btn9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn9ActionPerformed
        // TODO add your handling code here:
        funGenerateString(btn9.getActionCommand());
    }//GEN-LAST:event_btn9ActionPerformed

    private void btn8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn8ActionPerformed
        // TODO add your handling code here:
        funGenerateString(btn8.getActionCommand());
    }//GEN-LAST:event_btn8ActionPerformed

    private void btn7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn7ActionPerformed
        funGenerateString(btn7.getActionCommand());
    }//GEN-LAST:event_btn7ActionPerformed

    private void btn6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn6ActionPerformed
        // TODO add your handling code here:
        funGenerateString(btn6.getActionCommand());
    }//GEN-LAST:event_btn6ActionPerformed

    private void btn5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn5ActionPerformed
        // TODO add your handling code here:
        funGenerateString(btn5.getActionCommand());
    }//GEN-LAST:event_btn5ActionPerformed

    private void btn4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn4ActionPerformed
        // TODO add your handling code here:
        funGenerateString(btn4.getActionCommand());
    }//GEN-LAST:event_btn4ActionPerformed

    private void btn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn3ActionPerformed
        // TODO add your handling code here:
        funGenerateString(btn3.getActionCommand());
    }//GEN-LAST:event_btn3ActionPerformed

    private void btn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn2ActionPerformed
        // TODO add your handling code here:
        funGenerateString(btn2.getActionCommand());
    }//GEN-LAST:event_btn2ActionPerformed

    private void btn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn1ActionPerformed
        // TODO add your handling code here:
        funGenerateString(btn1.getActionCommand());
    }//GEN-LAST:event_btn1ActionPerformed

    private void btnTildaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTildaActionPerformed
        // TODO add your handling code here:
        funGenerateString(btnTilda.getActionCommand());
    }//GEN-LAST:event_btnTildaActionPerformed

    private void btnenterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnenterMouseClicked
        funPressEnterBtn();
    }//GEN-LAST:event_btnenterMouseClicked

    private void btnenterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnenterActionPerformed

        funPressEnterBtn();
    }//GEN-LAST:event_btnenterActionPerformed

    private void btnSpaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSpaceActionPerformed
        funGenerateString(" ");
    }//GEN-LAST:event_btnSpaceActionPerformed

    private void btnsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsActionPerformed
        // TODO add your handling code here:
        funGenerateString(btns.getActionCommand());
    }//GEN-LAST:event_btnsActionPerformed

    private void btnqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnqActionPerformed
        // TODO add your handling code here:
        funGenerateString(btnq.getActionCommand());
    }//GEN-LAST:event_btnqActionPerformed

    private void btnxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnxActionPerformed
        // TODO add your handling code here:
        funGenerateString(btnx.getActionCommand());
    }//GEN-LAST:event_btnxActionPerformed

    private void btngActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btngActionPerformed
        // TODO add your handling code here:
        funGenerateString(btng.getActionCommand());
    }//GEN-LAST:event_btngActionPerformed

    private void btnzActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnzActionPerformed
        // TODO add your handling code here:
        funGenerateString(btnz.getActionCommand());
    }//GEN-LAST:event_btnzActionPerformed

    private void btniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btniActionPerformed
        // TODO add your handling code here:
        funGenerateString(btni.getActionCommand());
    }//GEN-LAST:event_btniActionPerformed

    private void btnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnoActionPerformed
        // TODO add your handling code here:
        funGenerateString(btno.getActionCommand());
    }//GEN-LAST:event_btnoActionPerformed

    private void btnwActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnwActionPerformed
        // TODO add your handling code here:
        funGenerateString(btnw.getActionCommand());
    }//GEN-LAST:event_btnwActionPerformed

    private void btnapostropesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnapostropesActionPerformed
        funGenerateString(btnapostropes.getActionCommand());
    }//GEN-LAST:event_btnapostropesActionPerformed

    private void btneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneActionPerformed
        // TODO add your handling code here:
        funGenerateString(btne.getActionCommand());
    }//GEN-LAST:event_btneActionPerformed

    private void btnpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnpActionPerformed
        // TODO add your handling code here:
        funGenerateString(btnp.getActionCommand());
    }//GEN-LAST:event_btnpActionPerformed

    private void btnuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnuActionPerformed
        // TODO add your handling code here:
        funGenerateString(btnu.getActionCommand());
    }//GEN-LAST:event_btnuActionPerformed

    private void btnyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnyActionPerformed
        // TODO add your handling code here:
        funGenerateString(btny.getActionCommand());
    }//GEN-LAST:event_btnyActionPerformed

    private void btntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btntActionPerformed
        // TODO add your handling code here:
        funGenerateString(btnt.getActionCommand());
    }//GEN-LAST:event_btntActionPerformed

    private void btnrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnrActionPerformed
        // TODO add your handling code here:
        funGenerateString(btnr.getActionCommand());
    }//GEN-LAST:event_btnrActionPerformed

    private void btnhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnhActionPerformed
        // TODO add your handling code here:
        funGenerateString(btnh.getActionCommand());
    }//GEN-LAST:event_btnhActionPerformed

    private void btnaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaActionPerformed
        // TODO add your handling code here:
        funGenerateString(btna.getActionCommand());
    }//GEN-LAST:event_btnaActionPerformed

    private void btnbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbActionPerformed

        funGenerateString(btnb.getActionCommand());
    }//GEN-LAST:event_btnbActionPerformed

    private void btnfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnfActionPerformed
        // TODO add your handling code here:
        funGenerateString(btnf.getActionCommand());
    }//GEN-LAST:event_btnfActionPerformed

    private void btncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncActionPerformed
        // TODO add your handling code here:
        funGenerateString(btnc.getActionCommand());
    }//GEN-LAST:event_btncActionPerformed

    private void btnvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnvActionPerformed
        // TODO add your handling code here:
        funGenerateString(btnv.getActionCommand());
    }//GEN-LAST:event_btnvActionPerformed

    private void btndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndActionPerformed
        // TODO add your handling code here:
        funGenerateString(btnd.getActionCommand());
    }//GEN-LAST:event_btndActionPerformed

    private void btnnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnnActionPerformed

        funGenerateString(btnn.getActionCommand());
    }//GEN-LAST:event_btnnActionPerformed

    private void btnjActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnjActionPerformed
        // TODO add your handling code here:
        funGenerateString(btnj.getActionCommand());
    }//GEN-LAST:event_btnjActionPerformed

    private void btnmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmActionPerformed

        funGenerateString(btnm.getActionCommand());
    }//GEN-LAST:event_btnmActionPerformed

    private void btnkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnkActionPerformed
        // TODO add your handling code here:
        funGenerateString(btnk.getActionCommand());
    }//GEN-LAST:event_btnkActionPerformed

    private void btncommaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncommaActionPerformed
        funGenerateString(btncomma.getActionCommand());
    }//GEN-LAST:event_btncommaActionPerformed

    private void btnlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlActionPerformed
        // TODO add your handling code here:
        funGenerateString(btnl.getActionCommand());
    }//GEN-LAST:event_btnlActionPerformed

    private void btndotMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btndotMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btndotMouseClicked

    private void btndotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndotActionPerformed
        funGenerateString(btndot.getActionCommand());
    }//GEN-LAST:event_btndotActionPerformed

    private void btnforwardslaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnforwardslaceActionPerformed
        funGenerateString(btnforwardslace.getActionCommand());
    }//GEN-LAST:event_btnforwardslaceActionPerformed

    private void btnRigthSqureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRigthSqureActionPerformed
        funGenerateString(btnRigthSqure.getActionCommand());
    }//GEN-LAST:event_btnRigthSqureActionPerformed

    private void btncloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btncloseMouseClicked
        // TODO add your handling code here:
        setVisible(false);
    }//GEN-LAST:event_btncloseMouseClicked

    private void btncloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncloseActionPerformed

        setVisible(false);
    }//GEN-LAST:event_btncloseActionPerformed

    private void btnsemecolonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsemecolonActionPerformed
        funGenerateString(btnsemecolon.getActionCommand());
    }//GEN-LAST:event_btnsemecolonActionPerformed

    private void btnbackslaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbackslaceActionPerformed
        funGenerateString(btnbackslace.getActionCommand());
    }//GEN-LAST:event_btnbackslaceActionPerformed

    private void btnaltMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnaltMouseClicked

    }//GEN-LAST:event_btnaltMouseClicked

    private void btnaltActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaltActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnaltActionPerformed

    private void btnleftSquareActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnleftSquareActionPerformed
        funGenerateString(btnleftSquare.getActionCommand());
    }//GEN-LAST:event_btnleftSquareActionPerformed

    private void btnCapsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapsActionPerformed
        // TODO add your handling code here:
        JToggleButton btn = (JToggleButton) evt.getSource();
        if (btn.isSelected()) {
            capsON();

        } else {
            capOff();
        }
    }//GEN-LAST:event_btnCapsActionPerformed

    private void btnleftShiftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnleftShiftActionPerformed
        try {
            Toolkit.getDefaultToolkit().beep();
            if (btnleftShift.isSelected()) {

                btnleftShift.setFont(btnleftShift.getFont().deriveFont(Font.BOLD, 14));
                btnrightshift.setFont(btnrightshift.getFont().deriveFont(Font.BOLD, 14));
                btnrightshift.setSelected(true);
                capsON();
                pressShiftbtnChangeCaption();
            } else {
                btnleftShift.setFont(btnleftShift.getFont().deriveFont(Font.BOLD, 12));
                btnrightshift.setFont(btnrightshift.getFont().deriveFont(Font.BOLD, 12));
                //btnrightshift.setFont(btnrightshift.getFont().deriveFont(Font.LAYOUT_LEFT_TO_RIGHT));
                capOff();

                relesedShiftbtnChangeCaption();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnleftShiftActionPerformed

    private void btnrightshiftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnrightshiftActionPerformed
        try {
            Toolkit.getDefaultToolkit().beep();
            if (btnrightshift.isSelected()) {
                btnleftShift.setFont(btnleftShift.getFont().deriveFont(Font.BOLD, 14));
                btnrightshift.setFont(btnrightshift.getFont().deriveFont(Font.BOLD, 14));
                btnleftShift.setSelected(true);
                capsON();
                pressShiftbtnChangeCaption();
            } else {
                btnrightshift.setFont(btnrightshift.getFont().deriveFont(Font.BOLD, 12));
                btnleftShift.setFont(btnleftShift.getFont().deriveFont(Font.BOLD, 12));
                //btnrightshift.setFont(btnrightshift.getFont().deriveFont(Font.LAYOUT_LEFT_TO_RIGHT));
                capOff();
                relesedShiftbtnChangeCaption();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnrightshiftActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn0;
    private javax.swing.JButton btn1;
    private javax.swing.JButton btn2;
    private javax.swing.JButton btn3;
    private javax.swing.JButton btn4;
    private javax.swing.JButton btn5;
    private javax.swing.JButton btn6;
    private javax.swing.JButton btn7;
    private javax.swing.JButton btn8;
    private javax.swing.JButton btn9;
    private javax.swing.JToggleButton btnCaps;
    private javax.swing.JButton btnCtrl;
    private javax.swing.JButton btnRigthSqure;
    private javax.swing.JButton btnSpace;
    private javax.swing.JButton btnTab;
    private javax.swing.JButton btnTilda;
    private javax.swing.JButton btna;
    private javax.swing.JButton btnalt;
    private javax.swing.JButton btnapostropes;
    private javax.swing.JButton btnb;
    private javax.swing.JButton btnbackSpace;
    private javax.swing.JButton btnbackslace;
    private javax.swing.JButton btnc;
    private javax.swing.JButton btnclose;
    private javax.swing.JButton btncomma;
    private javax.swing.JButton btnd;
    private javax.swing.JButton btndot;
    private javax.swing.JButton btne;
    private javax.swing.JButton btnenter;
    private javax.swing.JButton btnequal;
    private javax.swing.JButton btnf;
    private javax.swing.JButton btnforwardslace;
    private javax.swing.JButton btng;
    private javax.swing.JButton btnh;
    private javax.swing.JButton btnhypen;
    private javax.swing.JButton btni;
    private javax.swing.JButton btnj;
    private javax.swing.JButton btnk;
    private javax.swing.JButton btnl;
    private javax.swing.JToggleButton btnleftShift;
    private javax.swing.JButton btnleftSquare;
    private javax.swing.JButton btnm;
    private javax.swing.JButton btnn;
    private javax.swing.JButton btno;
    private javax.swing.JButton btnp;
    private javax.swing.JButton btnq;
    private javax.swing.JButton btnr;
    private javax.swing.JToggleButton btnrightshift;
    private javax.swing.JButton btns;
    private javax.swing.JButton btnsemecolon;
    private javax.swing.JButton btnt;
    private javax.swing.JButton btnu;
    private javax.swing.JButton btnv;
    private javax.swing.JButton btnw;
    private javax.swing.JButton btnx;
    private javax.swing.JButton btny;
    private javax.swing.JButton btnz;
    private javax.swing.JLabel lblBodyImg;
    private javax.swing.JPanel panelMain;
    // End of variables declaration//GEN-END:variables
}
