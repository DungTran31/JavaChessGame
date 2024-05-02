import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import view.LoginForm;

public class Chess2 extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginForm loginForm = new LoginForm();
            
                loginForm.setVisible(true);
            }
        });
    }
}