/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author pc9
 */

enum NotificationType {
    NotificationATFLOOR, NotificationMOVINGUP, NotificationMOVINGDOWN, NotificationSTOPWAITING, NotificationSTARTWAITING
}

public class Notification {
    private String msg;
    private int floor;
    private NotificationType type;

    public Notification(String msg, int floor, NotificationType type) {
        this.msg = msg;
        this.floor = floor;
        this.type = type;
    }
    
    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    
}
