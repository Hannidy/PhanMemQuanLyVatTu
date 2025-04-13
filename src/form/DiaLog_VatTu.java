package form;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import dao.VatTuDAO;
import entity.model_VatTu;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import raven.toast.Notifications;

/**
 *
 * @author RubyNgoc
 */
public class DiaLog_VatTu extends javax.swing.JDialog {

    private DefaultTableModel tbl_ModelVatTu;
    private VatTuDAO vtdao = new VatTuDAO();
    private List<model_VatTu> list_VatTu = new ArrayList<model_VatTu>();
    private VatTu_Form pnVatTuRef;
    private static final String LOG_FILE = "vattu_log.txt";

    /**
     * Creates new form DaiLog_VatTu
     *
     * @param parent
     * @param modal
     * @param parentPanel
     * @param dsMaLoai
     */
    public DiaLog_VatTu(java.awt.Frame parent, boolean modal, VatTu_Form parentPanel, Set<String> dsMaLoai) {
        super(parent, modal);
        initComponents();
        this.pnVatTuRef = parentPanel; // Gán tham chiếu đến pnVatTu
        setLocationRelativeTo(null);
        // Cập nhật lại combobox;
        cbo_maloaivatTu.removeAllItems();
        for (String maLoai : dsMaLoai) {
            cbo_maloaivatTu.addItem(maLoai);
        }
        cbo_maloaivatTu.revalidate();
        cbo_maloaivatTu.repaint();
    }

    public void setData(String tenVT, String maLoaiVatTu) {
        txt_tenvatTu.setText(tenVT);
        cbo_maloaivatTu.setSelectedItem(maLoaiVatTu);
    }

    public void setMaLoaiData(Set<String> dsMaLoai) { // Hàm truyền dữ liệu vào combobox
        cbo_maloaivatTu.removeAllItems(); // Xóa dữ liệu cũ nếu có
        for (String maLoai : dsMaLoai) {
            cbo_maloaivatTu.addItem(maLoai);
        }
    }

    public void fillToTableVatTu() {
        try {
            // Xóa toàn bộ dữ liệu cũ trước khi thêm mới
            tbl_ModelVatTu.setRowCount(0);

            // Lấy danh sách vật tư từ database
            list_VatTu = vtdao.selectAll();
            if (list_VatTu != null) {
                for (model_VatTu vt : list_VatTu) {
                    tbl_ModelVatTu.addRow(new Object[]{
                        vt.getMavatTu(), // Chỉ lấy Mã Vật Tư
                        vt.getTenVatTu(), // Chỉ lấy Tên Vật Tư
                        vt.getMaloaivatTu()// Chỉ lấy Mã Loại Vật Tư
                    });
                }
            }
        } catch (Exception e) { // In lỗi để dễ debug
            // In lỗi để dễ debug
            JOptionPane.showMessageDialog(null, "Lỗi truy vấn dữ liệu: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void lamMoi() {
        pnVatTuRef.fillToTableVatTu();
        this.txt_tenvatTu.setText("");
        this.cbo_maloaivatTu.setSelectedItem("");
    }

    public void addVatTu() {
        boolean isValid = true;

        resetBorder(txt_tenvatTu);

        String tenVatTu = txt_tenvatTu.getText().trim();
        if (tenVatTu.isEmpty()) {
            setErrorBorder(txt_tenvatTu);
            isValid = false;
        }

        if (!isValid) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập đủ thông tin!");
            return;
        }

        if (vtdao.isTenVatTuExist(tenVatTu)) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Tên vật tư đã tồn tại!");
            setErrorBorder(txt_tenvatTu);
            return;
        }

        model_VatTu vt = new model_VatTu();
        vt.setTenVatTu(tenVatTu);
        vt.setMaloaivatTu((String) cbo_maloaivatTu.getSelectedItem());

        try {
            // Sinh mã vật tư trước khi insert
            String maVT = vtdao.selectMaxId();
            vt.setMavatTu(maVT); // Gán mã vào vt
            vtdao.insert(vt); // Thêm vào CSDL

            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Thêm vật tư thành công!");

            // Ghi log
            String log = String.format("Thêm|%s|%s|%s|%s",
                    maVT,
                    tenVatTu,
                    vt.getMaloaivatTu(),
                    new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(new Date()));
            writeLogToFile(log);

            if (pnVatTuRef != null) {
                pnVatTuRef.fillToTableVatTu();
               
            }

            new Timer(700, e -> dispose()).start();

        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Thêm vật tư thất bại!");
            String log = String.format("Thêm thất bại|%s|%s|%s|%s",
                    vt.getMavatTu() != null ? vt.getMavatTu() : "N/A",
                    tenVatTu,
                    vt.getMaloaivatTu(),
                    new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(new Date()));
            writeLogToFile(log);
            if (pnVatTuRef != null) {
               
            }
        }
    }
    
    //Ghi vào file
    private void writeLogToFile(String log) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(log);
            writer.newLine();
        } catch (IOException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi khi ghi log: " + e.getMessage());
        }
    }


    // Đặt viền đỏ cho JTextField khi có lỗi
    private void setErrorBorder(JTextField field) {
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.RED)); // Gạch đỏ dưới
    }

    // Đặt lại viền mặc định cho JTextField khi nhập đúng
    private void resetBorder(JTextField field) {
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(200, 200, 200))); // Viền xám nhạt
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txt_tenvatTu = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cbo_maloaivatTu = new javax.swing.JComboBox<>();
        btn_them = new javax.swing.JButton();
        btn_lamMoi = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Vật Tư");

        jLabel2.setText("Tên Vật Tư:");

        jLabel3.setText("Mã Loại Vật Tư:");

        cbo_maloaivatTu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btn_them.setText("Thêm");
        btn_them.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_themActionPerformed(evt);
            }
        });

        btn_lamMoi.setText("Làm Mới");
        btn_lamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_lamMoiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_tenvatTu)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(cbo_maloaivatTu, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(86, 86, 86)
                .addComponent(btn_them)
                .addGap(18, 18, 18)
                .addComponent(btn_lamMoi)
                .addContainerGap(79, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(txt_tenvatTu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(cbo_maloaivatTu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 124, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_them)
                    .addComponent(btn_lamMoi))
                .addGap(39, 39, 39))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_themActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_themActionPerformed

        addVatTu();
    }//GEN-LAST:event_btn_themActionPerformed

    private void btn_lamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_lamMoiActionPerformed

        lamMoi();
    }//GEN-LAST:event_btn_lamMoiActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FlatRobotoFont.install();
        FlatLaf.registerCustomDefaultsSource("themes");
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        FlatMacDarkLaf.setup();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DiaLog_VatTu dialog = new DiaLog_VatTu(new javax.swing.JFrame(), true, null, null);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_lamMoi;
    private javax.swing.JButton btn_them;
    private javax.swing.JComboBox<String> cbo_maloaivatTu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField txt_tenvatTu;
    // End of variables declaration//GEN-END:variables
}
