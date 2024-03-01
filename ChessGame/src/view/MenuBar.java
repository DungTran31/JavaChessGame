package view;

import java.awt.Color;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenuBar extends JMenuBar {
    private JMenu jMenu_Game = new JMenu();
    private JMenuItem jMenuItem_New1P = new JMenuItem();
    private JMenuItem jMenuItem_New2P = new JMenuItem();
    private JMenuItem jMenuItem_Undo = new JMenuItem();
    private JMenuItem jMenuItem_Close = new JMenuItem();

    public MenuBar() {
        this.buildMenuBar();
    }

    private void buildMenuBar() {
        jMenu_Game.setText("Options");
        jMenuItem_New1P.setText("New AI game");
        jMenu_Game.add(jMenuItem_New1P);
        jMenuItem_New2P.setText("New game");
        jMenu_Game.add(jMenuItem_New2P);
        jMenuItem_Undo.setText("Undo");
        jMenu_Game.add(jMenuItem_Undo);
        jMenuItem_Close.setText("Close");
        jMenu_Game.add(jMenuItem_Close);

        add(jMenu_Game);
        this.setBackground(Color.decode("#e6e6e6"));
    }

    public JMenuItem getjMenuItem_New1P() {
        return jMenuItem_New1P;
    }

    public JMenuItem getjMenuItem_New2P() {
        return jMenuItem_New2P;
    }

    public JMenuItem getjMenuItem_Undo() {
        return jMenuItem_Undo;
    }

    public JMenuItem getjMenuItem_Close() {
        return jMenuItem_Close;
    }
}
