/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package form;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import dao.NhaCungCapDAO;
import entity.model_NhaCungCap;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import raven.toast.Notifications;

/**
 *
 * @author RubyNgoc
 */
public class DiaLog_NhaCungCap extends javax.swing.JDialog {

    private DefaultTableModel tbl_ModelNhaCungCap;
    private NhaCungCapDAO nccdao = new NhaCungCapDAO();
    private List<model_NhaCungCap> list_NhaCungCap = new ArrayList<model_NhaCungCap>();
    private NhaCungCap_Form pnNCCRef;

    private String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static final String LOG_FILE = "nhacungcap_log.txt";
    // Danh sách lưu trữ thông báo
    private List<String> actionLogs = new ArrayList<>();
    // Biến đếm số lượng thông báo
    private int notificationCount = 0;
    private static final long TWENTY_FOUR_HOURS = 24 * 60 * 60 * 1000; // 24 tiếng tính bằng milliseconds

    /**
     * Creates new form DiaLog_NhaCungCap
     */
    public DiaLog_NhaCungCap(java.awt.Frame parent, boolean modal, NhaCungCap_Form parentPanel) {
        super(parent, modal);
        initComponents();
        this.pnNCCRef = parentPanel;
        this.setLocationRelativeTo(null);
    }

    public void setData(String tenNCC, String SDT, String email, String diaChi) {
        txt_tennhacungCap.setText(tenNCC);
        txt_sodienThoai.setText(SDT);
        txt_email.setText(email);
        txt_diaChi.setText(diaChi);
    }

    public void lamMoi() {
        pnNCCRef.fillToTableNhaCungCap();
        this.txt_tennhacungCap.setText("");
        this.txt_email.setText("");
        this.txt_sodienThoai.setText("");
        this.txt_diaChi.setText("");

    }

    public void fillToTableNhaCungCap() {
        try {
            // Xóa toàn bộ dữ liệu cũ trước khi thêm mới
            tbl_ModelNhaCungCap.setRowCount(0);

            // Lấy danh sách nhà cung cấp từ database
            List<model_NhaCungCap> list_NhaCungCap = nccdao.selectAll();
            if (list_NhaCungCap != null) {
                for (model_NhaCungCap ncc : list_NhaCungCap) {
                    tbl_ModelNhaCungCap.addRow(new Object[]{
                        ncc.getManhacungCap(),
                        ncc.getTennhacungCap(),
                        ncc.getSodienThoai(),
                        ncc.getEmail(),
                        ncc.getDiaChi()
                    });
                }
            }
        } catch (Exception e) { // In lỗi ra console để dễ debug
            // In lỗi ra console để dễ debug
            Notifications.getInstance().show(Notifications.Type.INFO, "Lỗi truy vấn dữ liệu: ");
        }
    }

    private void addNhaCungCap() {
        boolean isValid = true;
        boolean hasError = false;
        boolean hasSpecificError = false; // Biến để theo dõi lỗi cụ thể

        // Reset viền trước khi kiểm tra
        resetBorder(txt_tennhacungCap);
        resetBorder(txt_sodienThoai);
        resetBorder(txt_email);
        resetBorder(txt_diaChi);

        // Kiểm tra từng field
        String tenNCC = txt_tennhacungCap.getText().trim();
        if (tenNCC.isEmpty()) {
            setErrorBorder(txt_tennhacungCap);
            isValid = false;
        }

        String SDT = txt_sodienThoai.getText().trim();
        if (SDT.isEmpty()) {
            setErrorBorder(txt_sodienThoai);
            isValid = false;
        } else if (!SDT.matches("0\\d{9}")) { // Kiểm tra số điện thoại bắt đầu bằng 0 và có 10 chữ số
            setErrorBorder(txt_sodienThoai);
            Notifications.getInstance().show(Notifications.Type.INFO, "Số điện thoại phải bắt đầu bằng 0 và có đúng 10 chữ số!");
            isValid = false;
            hasSpecificError = true;
        }

        String email = txt_email.getText().trim();
        if (email.isEmpty()) {
            setErrorBorder(txt_email);
            isValid = false;
        } else if (!email.matches(emailRegex)) {
            setErrorBorder(txt_email);
            Notifications.getInstance().show(Notifications.Type.INFO, "Email không đúng định dạng!");
            isValid = false;
            hasSpecificError = true;
        }

        String diachi = txt_diaChi.getText().trim();
        if (diachi.isEmpty()) {
            setErrorBorder(txt_diaChi);
            isValid = false;
        }

        // Nếu có lỗi và không có lỗi cụ thể, hiển thị thông báo tổng quát
        if (!isValid && !hasSpecificError) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập đầy đủ thông tin!");
            return;
        } else if (!isValid) {
            return; // Có lỗi cụ thể, không hiển thị thêm thông báo
        }

        // 🔎 Kiểm tra tên nhà cung cấp
        if (nccdao.isEmailNhaCungCapExist(tenNCC)) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Tên nhà cung cấp đã tồn tại!");
            setErrorBorder(txt_tennhacungCap);
            hasError = true;
        }

        // 🔎 Kiểm tra email
        if (nccdao.isEmailNhaCungCapExist(email)) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Email đã tồn tại!");
            setErrorBorder(txt_email);
            hasError = true;
        }

        // 🔎 Kiểm tra SDT
        if (nccdao.isSDTNhaCungCapExist(SDT)) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Số điện thoại đã tồn tại!");
            setErrorBorder(txt_sodienThoai);
            hasError = true;
        }

        // Nếu có bất kỳ lỗi nào thì dừng lại
        if (hasError) {
            return;
        }
        // Nếu hợp lệ, tiếp tục thêm nhà cung cấp
        model_NhaCungCap ncc = new model_NhaCungCap();
        ncc.setTennhacungCap(tenNCC);
        ncc.setSodienThoai(SDT);
        ncc.setEmail(email);
        ncc.setDiaChi(diachi);

        try {
            // Sinh mã nhà cung cấp trước khi insert (nếu cần)
            String maNCC = nccdao.selectMaxId(); // Giả định nccdao có hàm selectMaxId() tương tự
            ncc.setManhacungCap(maNCC); // Gán mã vào ncc
            nccdao.insert(ncc); // Thêm vào CSDL

            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Thêm nhà cung cấp thành công!");

            // Ghi log (đồng bộ với addVatTu)
            String log = String.format("Thêm|%s|%s|%s|%s|%s|%s",
                    maNCC,
                    tenNCC,
                    SDT,
                    email,
                    diachi, // Thay cho maLoaiVatTu, vì không có trường tương ứng
                    new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(new Date()));
            writeLogToFile(log);

            // Cập nhật bảng
            if (pnNCCRef != null) {
                pnNCCRef.fillToTableNhaCungCap();

            }

            // Đợi thông báo hiển thị xong rồi đóng form (đồng bộ thời gian với addVatTu)
            new Timer(700, e -> dispose()).start();

        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Thêm nhà cung cấp thất bại!");
            String log = String.format("Thêm thất bại|%s|%s|%s|%s|%s|%s",
                    ncc.getManhacungCap() != null ? ncc.getManhacungCap() : "N/A",
                    tenNCC,
                    SDT,
                    email,
                    diachi,
                    new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(new Date()));
            writeLogToFile(log);
            if (pnNCCRef != null) {
                pnNCCRef.fillToTableNhaCungCap();

            }
        }
    }

    private void writeLogToFile(String log) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(log);
            writer.newLine();
        } catch (IOException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi khi ghi log: " + e.getMessage());
        }
    }

    private void setErrorBorder(JTextField field) {
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.RED)); // Gạch đỏ dưới
    }

    // Đặt lại viền mặc định cho JTextField khi nhập đúng
    private void resetBorder(JTextField field) {
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(200, 200, 200))); // Viền xám nhạt
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txt_tennhacungCap = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txt_sodienThoai = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txt_email = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txt_diaChi = new javax.swing.JTextField();
        btn_them = new javax.swing.JButton();
        btn_lamMoi = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Nhà Cung Cấp");

        jLabel2.setText("Tên Nhà Cung Cấp:");

        jLabel3.setText("Số Điện Thoại:");

        jLabel4.setText("Email:");

        jLabel5.setText("Địa Chỉ:");

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
                    .addComponent(txt_tennhacungCap)
                    .addComponent(txt_sodienThoai)
                    .addComponent(txt_email)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(txt_diaChi))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(78, 78, 78)
                .addComponent(btn_them)
                .addGap(18, 18, 18)
                .addComponent(btn_lamMoi)
                .addContainerGap(87, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(txt_tennhacungCap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(txt_sodienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(txt_email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(txt_diaChi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_them)
                    .addComponent(btn_lamMoi))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_themActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_themActionPerformed
        // TODO add your handling code here:
        addNhaCungCap();
    }//GEN-LAST:event_btn_themActionPerformed

    private void btn_lamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_lamMoiActionPerformed
        // TODO add your handling code here:
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
                DiaLog_NhaCungCap dialog = new DiaLog_NhaCungCap(new javax.swing.JFrame(), true, null);
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField txt_diaChi;
    private javax.swing.JTextField txt_email;
    private javax.swing.JTextField txt_sodienThoai;
    private javax.swing.JTextField txt_tennhacungCap;
    // End of variables declaration//GEN-END:variables
}
