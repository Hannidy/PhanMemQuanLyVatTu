
package tabbed;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import net.miginfocom.swing.MigLayout;

public class TabbedItem extends JToggleButton {

    public TabbedForm getComponent() {
        
        return component;
    }

    public String getTabbedName() {
        return tabbedName;
    }

    private final TabbedForm component;
    private final String tabbedName;

    public TabbedItem(String name, TabbedForm component) {
        this.tabbedName = name;
        this.component = component;
        init(name);


        // Style
        setPreferredSize(new Dimension(130, 32));
        setFont(new Font("Segoe UI", Font.PLAIN, 13));
        putClientProperty(FlatClientProperties.STYLE, "arc:10; background:null; borderWidth:0;");
    }

    private void init(String name) {
        setLayout(new GridBagLayout());

        JLabel label = new JLabel(name);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));

        JButton cmd = new JButton(new FlatSVGIcon("drawer/icon/close.svg", 0.2f));
        cmd.addActionListener((ActionEvent ae) -> {
            WindowsTabbed.getInstance().removeTab(this);
        });
        cmd.putClientProperty(FlatClientProperties.STYLE,
            "margin:2,2,2,2; borderWidth:0; focusWidth:0; innerFocusWidth:0; background:null; arc:999;");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 4, 0, 4);
        add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(cmd, gbc);
    }
      


//    private void init(String name) {
//        setLayout(new MigLayout("", "[]10[]"));
//        putClientProperty(FlatClientProperties.STYLE, ""
//                + "borderWidth:0;"
//                + "focusWidth:0;"
//                + "innerFocusWidth:0;"
//                + "background:null;"
//                + "arc:10;"
//                + "margin:2,8,2,5");
//        JButton cmd = new JButton(new FlatSVGIcon("drawer/icon/close.svg", 0.2f));
//        cmd.addActionListener((ae) -> {
//            WindowsTabbed.getInstance().removeTab(this);
//        });
//        cmd.putClientProperty(FlatClientProperties.STYLE, ""
//                + "margin:3,3,3,3;"
//                + "borderWidth:0;"
//                + "focusWidth:0;"
//                + "innerFocusWidth:0;"
//                + "background:null;"
//                + "arc:999;");
//        add(new JLabel(name));
//        add(cmd, BorderLayout.EAST);
//    }
    
    
//    private void init(String name) {
//    setLayout(new MigLayout("insets 0", "[]10[]"));  // rõ ràng layout
//    putClientProperty(FlatClientProperties.STYLE, ""
//            + "borderWidth:0;"
//            + "focusWidth:0;"
//            + "innerFocusWidth:0;"
//            + "background:null;"
//            + "arc:10;"
//            + "margin:2,8,2,5");
//
//    JLabel label = new JLabel(name);
//    JButton cmd = new JButton(new FlatSVGIcon("drawer/icon/close.svg", 0.2f));
//    cmd.addActionListener((ae) -> {
//        WindowsTabbed.getInstance().removeTab(this);
//    });
//    cmd.putClientProperty(FlatClientProperties.STYLE, ""
//            + "margin:3,3,3,3;"
//            + "borderWidth:0;"
//            + "focusWidth:0;"
//            + "innerFocusWidth:0;"
//            + "background:null;"
//            + "arc:999;");
//
//    add(label, "gapright 5");
//    add(cmd);
//}
    
    

    @Override
    public void paint(Graphics grphcs) {
        super.paint(grphcs);
        if (!isSelected() && getParent().getComponentZOrder(this) != getParent().getComponentCount() - 1) {
            Graphics2D g2 = (Graphics2D) grphcs.create();
            FlatUIUtils.setRenderingHints(g2);
            g2.setColor(UIManager.getColor("Component.borderColor"));
            float m = UIScale.scale(5);
            float s = UIScale.scale(1);
            g2.fill(new Rectangle2D.Double(getWidth() - s, m, s, getHeight() - m * 2));
            g2.dispose();
        }
    }
}
