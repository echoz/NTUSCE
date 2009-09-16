/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 *
 * @author jeremy
 */
public class ButtonPanel extends JPanel implements ActionListener {
    private JButton btnArray[];
    private boolean btnState[];

    public ButtonPanel(String[] buttonTitles) {
        int buttonCount = buttonTitles.length;
        btnArray = new JButton[buttonCount];
        btnState = new boolean[buttonCount];

        this.setLayout(new GridLayout(buttonCount,1));

        for (int i=(buttonCount-1);i>=0;i--) {
            btnArray[i] = new JButton(buttonTitles[i]);
            btnArray[i].addActionListener(this);
            btnArray[i].setActionCommand(Integer.toString(i));
            btnArray[i].setOpaque(true);
            this.add(btnArray[i]);

            btnState[i] = false;

            this.toggleButton(btnState[i], btnArray[i]);
        }
        
    }

    public boolean getButtonState(int i) {
        return btnState[i];
    }

    public void toggleButtonState(int i) {
        btnState[i] = !btnState[i];
        this.toggleButton(btnState[i], btnArray[i]);
    }

    public void actionPerformed(ActionEvent e) {
        int i = Integer.parseInt(e.getActionCommand());

        btnState[i] = !btnState[i];
        this.toggleButton(btnState[i], btnArray[i]);
    }

    private void toggleButton(boolean state, JButton btn) {
        if (state) {
            btn.setBackground(Color.red);
        } else {
            btn.setBackground(Color.green);
        }
    }

}
