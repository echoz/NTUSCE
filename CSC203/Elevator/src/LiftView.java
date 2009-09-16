/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.*;
import javax.swing.*;
/**
 *
 * @author jeremy
 */
public class LiftView extends JPanel {
    private int wd;
    private int floors;
    private int ypos;
    private boolean initial = true;
    private boolean dooropen = false;
    private Image passenger;

    public LiftView(int floors, int width) {
        super();
        this.wd = width;
        this.floors = floors;
        
    }
    public int getYpos() { return ypos; }
    public void setYpos(int ypos) { this.ypos = ypos; }

    public Image getPassenger() { return passenger; }
    public void setPassenger(Image passenger) { this.passenger = passenger; }

    public boolean isDooropen() { return dooropen; }
    public void setDooropen(boolean dooropen) { this.dooropen = dooropen; }

    public int getYPosFromFloor(int i) {
        double boundsHeight = this.getBounds().getHeight();

        if ((i > floors) || (i < 1)) {
            return -1;
        } else {
            return (int)(boundsHeight/floors * (floors-i));
        }
        
    }

    public int getFloorFromYpos(int ypos) {
        for (int i=1;i<floors+1;i++) {
            if (this.getYPosFromFloor(i) == ypos) {
                return i;
            }
        }
        return 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        this.drawBackground(g);
        if (this.initial) {
            this.ypos = (int)this.getYPosFromFloor(1);
            this.initial = false;
        }
        this.drawLift(g, ypos, dooropen);

    }

    private void drawLift(Graphics g, int y, boolean dooropen) {
        double boundsWidth = this.getBounds().getWidth();
        double boundsHeight = this.getBounds().getHeight();
        
        int xpos = ((int)boundsWidth/2) - (wd/2);
        //bacground 3D-ish effect
        g.setColor(Color.black);
        g.drawRect(xpos, y, wd, (int)boundsHeight/this.floors-1);

        if (dooropen) {
            g.drawImage(passenger, xpos, y, wd, (int)boundsHeight/this.floors-1, null);

            g.setColor(Color.gray);
            g.fillRect(xpos, y, 12, (int)boundsHeight/this.floors-1);

            g.fillRect(xpos+wd-10, y, 10, (int)boundsHeight/this.floors-1);
        } else {
            // door color
            g.setColor(Color.yellow);
            g.fillRect(xpos, y, wd, (int)boundsHeight/this.floors-1);

            //door line
            g.setColor(Color.gray);
            g.drawLine(xpos + wd/2, y, xpos + wd/2, y + ((int)boundsHeight/this.floors)-1);
        }
    }

    private void drawBackground(Graphics g) {
        double boundsWidth = this.getBounds().getWidth();
        double boundsHeight = this.getBounds().getHeight();

        for (int i=0;i<floors;i++) {
            if ((i%2) == 0) {
                g.setColor(Color.gray);
            } else {
                g.setColor(Color.white);                
            }

            g.drawRect(0, i*(int)(boundsHeight/floors), (int)boundsWidth-1, (int)(boundsHeight/floors));
        }
    }
}
