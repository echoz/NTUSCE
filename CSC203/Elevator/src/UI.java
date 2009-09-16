/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.swing.*;
import java.awt.*;
import java.util.*;
import javax.swing.event.*;
import javax.swing.JApplet;
/**
 *
 * @author jeremy
 */
public class UI extends JFrame implements Observer, ChangeListener {
    private Lift elevator;
    private ButtonPanel btnPanel;
    private JLabel titleLabel;
    private JLabel statusLabel;
    private JSlider speedSlider;
    private int floorVisitCount;

    
    public static void main(String[] args) {
        UI frame = new UI();
        frame.init();
        frame.setTitle("Elevator Simulation");
        frame.setSize(800,600);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
    
   public void init() {
       floorVisitCount = 0;
        // setup
        this.setLayout(new BorderLayout());

        this.titleLabel = new JLabel("Name: Jeremy Foo Jie You Group: SS1", SwingConstants.CENTER);
        this.add(this.titleLabel, BorderLayout.NORTH);

        this.statusLabel = new JLabel("Loading...", SwingConstants.CENTER);
        this.add(this.statusLabel,BorderLayout.SOUTH);

        this.speedSlider = new JSlider(JSlider.VERTICAL, 2,20,10);
        this.speedSlider.setLabelTable(this.speedSlider.createStandardLabels(1, 2));
        this.speedSlider.setPaintLabels(true);
        this.speedSlider.setPaintTicks(true);
        this.speedSlider.setPaintTrack(true);
        this.speedSlider.setSnapToTicks(true);
        this.speedSlider.addChangeListener(this);
        this.add(this.speedSlider, BorderLayout.EAST);

        String[] titles = {"F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8"};
        this.btnPanel = new ButtonPanel(titles);

        // add buttons
        this.add(this.btnPanel, BorderLayout.WEST);

        // add elevator
        this.elevator = new Lift(titles.length, 50);
        this.elevator.addObserver(this);
        this.add(this.elevator.getPanel(), BorderLayout.CENTER);

        // done for application
        this.elevator.loadPassenger(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("domo.png")));

        // done for JApplet
        //this.elevator.loadPassenger(getImage(getCodeBase(), "domo.png"));
    }

    public void stateChanged(ChangeEvent e) {
        elevator.getTimer().setDelay(100/this.speedSlider.getValue());
    }

    public boolean checkFloorsToGoUp(int current, boolean up, Lift lift) {
        int downFloorDist = this.numberOfFloorsFrom(current, false, lift);
        int upFloorDist = this.numberOfFloorsFrom(current, true, lift);

        System.out.println("current: " + current + " down: " + downFloorDist + " up: " + upFloorDist);

        if (floorVisitCount > 2) {
            floorVisitCount = 0;
            return false;
        } else {
            if (downFloorDist == -1) return true;
            if (upFloorDist == -1) return false;


            if (downFloorDist < upFloorDist) {
                return false;
            } else {
                return true;
            }
        }
        

        
        /* old algorithm
        if (up) {
            if (current == lift.getFloors()) {
                return false;
            } else {
                for (int i=current;i<lift.getFloors();i++) {
                    if (btnPanel.getButtonState(i)) {
                        return true;
                    }
                }
                return false;
            }
        } else {
            if (current == 1) {
                return true;
            } else {
                for (int i=current-2;i>=0;i--) {
                    if (btnPanel.getButtonState(i)) {
                        return false;
                    }
                }
                return true;
            }
        }
        */
    }

    public int numberOfFloorsFrom(int current, boolean up, Lift lift) {
        if (up) {
            for (int i=current-1;i<lift.getFloors();i++) {
                if (btnPanel.getButtonState(i)) {
                    return i-current+1;
                }
            }
        } else {
            for (int i=current-1;i>=0;i--) {
                if (btnPanel.getButtonState(i)) {
                    return current-i-1;
                }
            }
        }
        return -1;
    }

    public void update(Observable o, Object arg) {
        if (arg != null) {
            Notification notify = (Notification)arg;

            if (notify.getType() == NotificationType.NotificationSTOPWAITING) {
                this.elevator.closeDoors();
                this.btnPanel.toggleButtonState(notify.getFloor()-1);
                floorVisitCount++;
                elevator.setUp(this.checkFloorsToGoUp(notify.getFloor(), elevator.isUp(), elevator));
            } else if (notify.getType() == NotificationType.NotificationSTARTWAITING) {
                this.elevator.openDoors();
            } else if (notify.getType() == NotificationType.NotificationMOVINGDOWN) {
                this.statusLabel.setText("Moving Down");
            } else if (notify.getType() == NotificationType.NotificationMOVINGUP) {
                this.statusLabel.setText("Moving Up");
            } else if (notify.getType() == NotificationType.NotificationATFLOOR) {
                if (btnPanel.getButtonState(notify.getFloor()-1)) {
                    elevator.startWait();

                    String suffix = "th";

                    if (((notify.getFloor() > 0) && (notify.getFloor() < 10)) || (notify.getFloor() > 20)) {
                        if (Integer.toString(notify.getFloor()).endsWith("1")) {
                            suffix = "st";
                        } else if (Integer.toString(notify.getFloor()).endsWith("2")) {
                            suffix = "nd";
                        } else if (Integer.toString(notify.getFloor()).endsWith("3")) {
                            suffix = "rd";
                        }
                    }

                    this.statusLabel.setText("Waiting for passengers at the " + notify.getFloor() + suffix + " floor.");

                }
            }
        }
    }
}
