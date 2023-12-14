import java.awt.*;
public class chess extends Frame{
    Image imgnormal = Toolkit.getDefaultToolkit().getImage("res/normal.png");
    Image imgsafe = Toolkit.getDefaultToolkit().getImage("res/safe0.png");

    public boolean isIspressed() {
        return ispressed;
    }

    public void setIspressed(boolean ispressed) {
        this.ispressed = ispressed;
    }

    public static final int INITX = (int) (640/2-4.5*24);
    public static final int INITY = 40;
    public static final int WIDTH = 24;
    public static final int HEIGHT = 24;
    private boolean isBomb;
    private boolean ispressed = false;
    private boolean isrendered = false;
    private int rx;

    private int ry;

    public chess(int rx, int ry){
        this.rx = rx;
        this.ry = ry;
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
        if (ispressed)

            isrendered = g.drawImage(imgsafe,INITX+WIDTH*rx,INITY+HEIGHT*ry,WIDTH,HEIGHT,this);
        else
            isrendered = g.drawImage(imgnormal,INITX+WIDTH*rx,INITY+HEIGHT*ry,WIDTH,HEIGHT,this);
    }

}
