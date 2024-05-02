package view;


import controller.Controller;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginForm extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 349825537547573382L;
	private JPanel contentPane;
    private JTextField textField_Username;
    private JPasswordField passwordField_Password;

    public LoginForm() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 453, 472);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("LOG IN");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblNewLabel.setBounds(158, 33, 116, 31);
        contentPane.add(lblNewLabel);

        JLabel Label_Username = new JLabel("Username");
        Label_Username.setFont(new Font("Tahoma", Font.PLAIN, 15));
        Label_Username.setBounds(32, 100, 67, 31);
        contentPane.add(Label_Username);

        JLabel Label_Password = new JLabel("Password");
        Label_Password.setFont(new Font("Tahoma", Font.PLAIN, 15));
        Label_Password.setBounds(32, 170, 67, 31);
        contentPane.add(Label_Password);

        textField_Username = new JTextField();
        textField_Username.setFont(new Font("Tahoma", Font.PLAIN, 15));
        textField_Username.setColumns(10);
        textField_Username.setBounds(109, 107, 293, 20);
        contentPane.add(textField_Username);

        passwordField_Password = new JPasswordField();
        passwordField_Password.setFont(new Font("Tahoma", Font.PLAIN, 15));
        passwordField_Password.setColumns(10);
        passwordField_Password.setBounds(109, 177, 293, 20);
        contentPane.add(passwordField_Password);

        JButton btn_LogIn = new JButton("Log In");
        btn_LogIn.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btn_LogIn.setBounds(32, 265, 370, 37);
        contentPane.add(btn_LogIn);

        JButton btn_SignUp = new JButton("Sign Up");
        btn_SignUp.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btn_SignUp.setBounds(32, 320, 370, 37);
        contentPane.add(btn_SignUp);

        // Xy lu su kien Log In
        btn_LogIn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = textField_Username.getText();
                String password = String.valueOf(passwordField_Password.getPassword());

                // check dang nhap
                if (checkLogin(username, password)) {
                    dispose(); // Dong cua so dang nhap
                    startChessGame(); // Khoi dong tro choi
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password");
                }
            }
        });

        //Xy lu su kien Sign Up
        btn_SignUp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();  
                new SignUpForm().setVisible(true); 
            }
        });

        // 
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    // Check dang nhap
    private boolean checkLogin(String username, String password) {
        Connection connection = database.Database.mycon();

        if (connection != null) {
            try {
                String query = "SELECT * FROM users WHERE UserName = ? AND UserPassword = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    return true;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return false;
    }

    // khoi doi lai tro choi
    private void startChessGame() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Controller().start();
            }
        });
    }
}