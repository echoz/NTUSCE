/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.*;
import javax.swing.Timer;
import java.awt.event.*;
import java.awt.Image;
/**
 *
 * @author jeremy
 */
public class Lift extends Observable implements ActionListener {
    private LiftView panel;
    private int height;
    private int width;
    private int xpos;
    private int ypos;
    private Timer timer;
    private int wait = 0;
    private int floors;
    private boolean up = true;
    private boolean waiting = false;

    public Lift(int floors, int width) {
        super();
        this.floors = floors;
        panel = new LiftView(floors, width);
        timer = new Timer(10, this);
        timer.setInitialDelay(1000);
        timer.start();
    }

    public boolean isUp() { return up; }
    public void setUp(boolean up) { this.up = up; }

    public int getFloors() { return floors; }
    public void setFloors(int floors) { this.floors = floors; }
    
    public LiftView getPanel() { return panel; }
    public void setPanel(LiftView panel) { this.panel = panel; }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }

    public int getXpos() { return xpos; }
    public void setXpos(int xpos) { this.xpos = xpos; }

    public int getYpos() { return ypos; }
    public void setYpos(int ypos) { this.ypos = ypos; }

    public int getWait() { return wait; }
    public void setWait(int wait) { this.wait = wait; }
    public void startWait() {
        this.wait = 100;
        this.waiting = true;
        this.setChanged();
        Notification notify = new Notification("STARTWAITING", panel.getFloorFromYpos(panel.getYpos()), NotificationType.NotificationSTARTWAITING);
        this.notifyObservers(notify);
    }
    
    public void openDoors() {
        this.panel.setDooropen(true);
    }
    public void closeDoors() {
        this.panel.setDooropen(false);
    }

    public void loadPassenger(Image passenger) {
        this.panel.setPassenger(passenger);
    }

    public Timer getTimer() { return timer; }
    public void setTimer(Timer timer) { this.timer = timer; }

    public void actionPerformed(ActionEvent e) {
        Notification notify = null;

        if (wait > 0) {
            wait--;
        } else {
            if (waiting) {
                waiting = false;
                this.setChanged();
                notify = new Notification("STOPWAITING", panel.getFloorFromYpos(panel.getYpos()), NotificationType.NotificationSTOPWAITING);
                this.notifyObservers(notify);
            }
            
            if (up) {
                panel.setYpos(panel.getYpos() - 1);
            } else {
                panel.setYpos(panel.getYpos() + 1);
            }

            if ((int)panel.getYPosFromFloor(this.floors) >= panel.getYpos()) {
                up = false;
            } else if ((int)panel.getYPosFromFloor(1) <= panel.getYpos()) {
                up = true;
            }
            
            this.setChanged();
            if (up) {
                notify = new Notification("MOVINGUP", 0, NotificationType.NotificationMOVINGUP);
            } else {
                notify = new Notification("MOVINGDOWN", 0, NotificationType.NotificationMOVINGDOWN);
            }                         
            this.notifyObservers(notify);

            for (int i=1;i<floors+1;i++) {
                if ((int)panel.getYPosFromFloor(i) == panel.getYpos()) {
                    this.setChanged();
                    notify = new Notification("ATFLOOR", i, NotificationType.NotificationATFLOOR);
                    this.notifyObservers(notify);
                }
            }
        }
        panel.repaint();
    }
    
    
}
