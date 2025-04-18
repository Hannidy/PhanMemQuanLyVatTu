
package component;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.miginfocom.swing.MigLayout;
import util.MethodUtil;

public class PasswordStrengthStatus extends javax.swing.JPanel {

    private JPasswordField passwordField;
    private DocumentListener documentListener;
    private JLabel label;
    private int type;
    public static int mucDoMatKhau = -1;

    public PasswordStrengthStatus() {
        init();
    }

    private void init() {
        putClientProperty(FlatClientProperties.STYLE, "" +
                "background:null");
        setLayout(new MigLayout("fill,insets 0", "3[100,fill,grow0][]", "[fill,grow 0]"));
        label = new JLabel("trống");
        label.setVisible(false);
        add(new LabelStatus());
        add(label);
    }

    private Color getStrengthColor(int type) {
        if (type == 1) {
            return Color.decode("#FF4D4D");
        } else if (type == 2) {
            return Color.decode("#FFB04D");
        } else {
            return Color.decode("#58C359");
        }
    }

    private void checkPassword(String password) {
        this.type = password.isEmpty() ? 0 : MethodUtil.checkPasswordStrength(password);
        if (type == 0) {
            label.setText("trống");
            label.setVisible(false);
        } else {
            label.setVisible(true);
            if (type == 1) {
                label.setText("Yếu");
                mucDoMatKhau = 1;
            } else if (type == 2) {
                label.setText("Trung Bình");
                mucDoMatKhau = 2;
            } else {
                label.setText("Mạnh");
                mucDoMatKhau = 3;
            }
            label.setForeground(getStrengthColor(type));
        }
        repaint();
    }

    public void initPasswordField(JPasswordField txt) {
        if (documentListener == null) {
            documentListener = new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    checkPassword(String.valueOf(txt.getPassword()));
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    checkPassword(String.valueOf(txt.getPassword()));
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    checkPassword(String.valueOf(txt.getPassword()));
                }
            };
        }
        if (passwordField != null) {
            passwordField.getDocument().removeDocumentListener(documentListener);
        }
        txt.getDocument().addDocumentListener(documentListener);
        passwordField = txt;
    }

    private class LabelStatus extends JLabel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int width = getWidth();
            int height = getHeight();
            int size = (int) (height * 0.3f);
            Graphics2D g2 = (Graphics2D) g.create();
            FlatUIUtils.setRenderingHints(g2);
            int gap = UIScale.scale(5);
            int w = (width - gap * 2) / 3;
            int y = (height - size) / 2;
            Color disableColor = Color.decode(FlatLaf.isLafDark() ? "#404040" : "#CECECE");
            if (type >= 1) {
                g2.setColor(getStrengthColor(1));
            } else {
                g2.setColor(disableColor);
            }
            FlatUIUtils.paintComponentBackground(g2, 0, y, w, size, 0, 999);
            if (type >= 2) {
                g2.setColor(getStrengthColor(2));
            } else {
                g2.setColor(disableColor);
            }
            FlatUIUtils.paintComponentBackground(g2, w + gap, y, w, size, 0, 999);
            if (type >= 3) {
                g2.setColor(getStrengthColor(3));
            } else {
                g2.setColor(disableColor);
            }
            FlatUIUtils.paintComponentBackground(g2, (w + gap) * 2, y, w, size, 0, 999);
            g2.dispose();
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1300, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
