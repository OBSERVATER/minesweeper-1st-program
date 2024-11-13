public class Init {
    public static LaunchFrame lr;
    public static int[][] diff = {{9,9,10},{16,16,40},{30,16,99},{}};
    public static void main(String[] args) {
        lr = new LaunchFrame();
        MenuBarLayout layout = new MenuBarLayout(lr);
        lr.win();
    }
}
