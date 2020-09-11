package componentes;

import javax.swing.*;
import java.awt.*;

public class ComponentTable {
    private Toolkit tk = Toolkit.getDefaultToolkit();
    private Dimension d = tk.getScreenSize();

    public void ConfigTabelaPrincipal(JTable table, JScrollPane pane){
        table.setSize((int) (d.width-(d.width/7))-60, d.height-270);
        table.setLocation(20, (int) (d.height/4.5));
        table.setBackground(new Color(136, 136, 136));
        table.setForeground(Color.black);
        pane.setSize((int) (d.width-(d.width/7))-60, d.height-270);
        pane.setLocation(20, (int) (d.height/4.5));
        pane.setBackground(new Color(136, 136, 136));
        pane.setForeground(Color.black);
    }


    public void ConfigTabela(JScrollPane table){
        table.setSize((int) (d.width/1.3), d.height/2);
        table.setLocation(10, (d.height/4));
        table.setBackground(new Color(136, 136, 136));
        table.setForeground(Color.black);
    }

}
