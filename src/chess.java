import java.awt.*;
public class chess extends Frame{
    Image imgnormal = Toolkit.getDefaultToolkit().getImage("res/normal.png");
    Image imgsafe = Toolkit.getDefaultToolkit().getImage("res/safe0.png");
    Image imgflag = Toolkit.getDefaultToolkit().getImage("res/flag1.png");

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public static final int INITX = (int) (640/2-4.5*24);
    public static final int INITY = (int) (480/2-4.5*24);
    public static final int WIDTH = 24;
    public static final int HEIGHT = 24;
    private boolean Bomb;
    private boolean pressed = false;
    private boolean isrendered = false;
    private boolean flagged = false;
    private int rx;

    private int ry;

    public chess(int rx, int ry){
        this.rx = rx;
        this.ry = ry;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    @Override
    public String toString() {
        return "chess{" +
                "isrendered=" + isrendered +
                ", x=" + INITX+WIDTH*rx +
                ", y=" + INITY+HEIGHT*ry +
                '}';
    }

    public void render(Graphics g){
        //isrendered = g.drawImage(imgsafe,100,100,100,100,this);
        if (pressed){
            g.drawImage(imgsafe,INITX+WIDTH*rx,INITY+HEIGHT*ry,WIDTH,HEIGHT,this);
        }
        else if (flagged) {
            g.drawImage(imgflag,INITX+WIDTH*rx,INITY+HEIGHT*ry,WIDTH,HEIGHT,this);
        } else
            g.drawImage(imgnormal,INITX+WIDTH*rx,INITY+HEIGHT*ry,WIDTH,HEIGHT,this);
    }

}
