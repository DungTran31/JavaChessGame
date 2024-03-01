import javax.swing.SwingUtilities;

import controller.Controller;


public class Chess {
	public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Controller game = new Controller();
                game.start();
            }
        });
    }
}
