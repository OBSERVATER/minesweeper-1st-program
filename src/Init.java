public class Init {
    public static LaunchFrame lr;
    public static void main(String[] args) {
        lr = new LaunchFrame();
        MenuBarLayout layout = new MenuBarLayout(lr);
        lr.win();
    }
}
