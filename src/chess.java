import java.awt.*;
public class chess extends Frame{
    Image imgnormal = Toolkit.getDefaultToolkit().getImage("res/normal.png");
    Image imgsafe = Toolkit.getDefaultToolkit().getImage("res/safe0.png");
    Image imgflag = Toolkit.getDefaultToolkit().getImage("res/flag1.png");
    Image imgbomb = Toolkit.getDefaultToolkit().getImage("res/mine1.png");
    Image imgbombtrigerred = Toolkit.getDefaultToolkit().getImage("res/mine2.png");
    public boolean isBomb() {
        return Bomb;
    }

    public void setBomb(boolean bomb) {
        Bomb = bomb;
    }

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
    private boolean bombtriggered = false;
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

    public void setBombtriggered(boolean bombtriggered) {
        this.bombtriggered = bombtriggered;
    }

    @Override
    public String toString() {
        return "chess{" +
                "bomb=" + Bomb +
                ", x=" + rx +
                ", y=" + ry +
                '}';
    }

    public void render(Graphics g){
        //isrendered = g.drawImage(imgsafe,100,100,100,100,this);
        if (pressed){
            if (Bomb && bombtriggered){
                //System.out.println("t");
                g.drawImage(imgbombtrigerred,INITX+WIDTH*rx,INITY+HEIGHT*ry,WIDTH,HEIGHT,this);
            } else if (!bombtriggered && Bomb) {
                g.drawImage(imgbomb,INITX+WIDTH*rx,INITY+HEIGHT*ry,WIDTH,HEIGHT,this);
            } else
                g.drawImage(imgsafe,INITX+WIDTH*rx,INITY+HEIGHT*ry,WIDTH,HEIGHT,this);
        }
        else if (flagged) {
            g.drawImage(imgflag,INITX+WIDTH*rx,INITY+HEIGHT*ry,WIDTH,HEIGHT,this);
        } else
            g.drawImage(imgnormal,INITX+WIDTH*rx,INITY+HEIGHT*ry,WIDTH,HEIGHT,this);
    }

}
