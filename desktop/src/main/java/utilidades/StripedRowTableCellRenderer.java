package utilidades;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

public class StripedRowTableCellRenderer extends DefaultTableCellRenderer {
    // i didn't like this color
    //private static final Color STRIPE = UIManager.getColor("textHighlight");

    // choose whatever color you prefer
    //private static final Color STRIPE = new Color(0.929f, 0.953f, 0.996f);
    //private static final Color WHITE = UIManager.getColor("Table.background");
    private static final Color STRIPE = new Color(175, 175, 175);
    private static final Color WHITE = new Color(140, 140, 140);

    private final JCheckBox ckb = new JCheckBox();

    public StripedRowTableCellRenderer() {
        setOpaque(true); //MUST do this for background to show up.
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        JComponent c = (JComponent)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (!isSelected)
        {
            if (row % 2 == 0)
            {
                c.setBackground(WHITE);
            }
            else
            {
                c.setBackground(STRIPE);
            }
        }

        if (value instanceof Boolean) { // Boolean
            ckb.setSelected(((Boolean) value));
            ckb.setHorizontalAlignment(JLabel.CENTER);
            ckb.setBackground(super.getBackground());
            if (isSelected || hasFocus) {
                ckb.setBackground(table.getSelectionBackground());
            }
            return ckb;
        }


        return c;
    }
}
