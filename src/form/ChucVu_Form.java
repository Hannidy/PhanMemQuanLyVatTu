package form;

import dao.ChucVuDAO;
import entity.model_ChucVu;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import raven.toast.Notifications;
import tabbed.TabbedForm;
import util.Message;

public class ChucVu_Form extends TabbedForm {

    public DefaultTableModel tbl_ModelChucVu;
    private ChucVuDAO cvdao = new ChucVuDAO();
    private List<model_ChucVu> list_ChucVu = new ArrayList<model_ChucVu>();
    private String selectedtenCV = "";

    public ChucVu_Form() {
        initComponents();
        tbl_ModelChucVu = (DefaultTableModel) tbl_chucVu.getModel();
        fillToTableChucVu();
    }

    public void fillToTableChucVu() {
        try {
            // Xóa toàn bộ dữ liệu cũ trước khi thêm mới
            tbl_ModelChucVu.setRowCount(0);

            // Lấy danh sách vật tư từ database
            list_ChucVu = cvdao.selectAll();
            if (list_ChucVu != null) {
                for (model_ChucVu cv : list_ChucVu) {
                    tbl_ModelChucVu.addRow(new Object[]{
                        cv.getMaChucVu(), // Chỉ lấy Mã Vật Tư
                        cv.getTenChucVu(), // Chỉ lấy Tên Vật Tư                
                    });
                }
            }
        } catch (Exception e) { // In lỗi để dễ debug
            // In lỗi để dễ debug
            JOptionPane.showMessageDialog(null, "Lỗi truy vấn dữ liệu: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteChucVu() {
        int[] selectedRows = tbl_chucVu.getSelectedRows(); // Lấy tất cả các dòng được chọn

        if (selectedRows.length == 0) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Chọn ít nhất một dòng để xóa!");
            return;
        }

        boolean confirm = Message.confirm("Bạn có chắc chắn muốn xóa " + selectedRows.length + " Chức Vụ?");
        if (!confirm) {
            return;
        }

        try {
            List<String> danhSachXoa = new ArrayList<>(); // Lưu các vật tư bị xóa để ghi vào thông báo

            for (int i = selectedRows.length - 1; i >= 0; i--) { // Xóa từ dưới lên để tránh lỗi chỉ số
                int row = selectedRows[i];
                String maChucVu = tbl_chucVu.getValueAt(row, 0).toString();
                cvdao.delete(maChucVu); // Xóa từng vật tư
                danhSachXoa.add(maChucVu); // Thêm vào danh sách để ghi nhận thông báo
            }

            fillToTableChucVu(); // Cập nhật lại bảng sau khi xóa
            //thongke();
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Đã xóa " + selectedRows.length + " chức vụ!");

            // 🔔 Cập nhật thông báo chuông sau khi hoàn tất tất cả các xóa
            for (String maChucVu : danhSachXoa) {
                //themThongBao("Xóa", maChucVu);
            }

        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Không thể xóa chức vụ!");
        }
    }

    private void updateChucVu() {
        int row = tbl_chucVu.getSelectedRow();
        if (row < 0) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Chọn một dòng để cập nhật!");
            return;
        }

        // Lấy dữ liệu từ JTable chỉ với 3 cột
        String maCV = tbl_chucVu.getValueAt(row, 0).toString();
        String tenCV = tbl_chucVu.getValueAt(row, 1).toString().trim();

        // Kiểm tra nếu có ô nào bị bỏ trống
        if (tenCV.isEmpty() || maCV.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        // Tạo đối tượng Vật Tư mới
        model_ChucVu cv = new model_ChucVu();
        cv.setMaChucVu(maCV);
        cv.setTenChucVu(tenCV);

        // Xác nhận cập nhật
        boolean confirm = Message.confirm("Bạn có chắc chắn muốn cập nhật vật tư có mã '" + maCV + "'?");
        if (confirm) {
            try {
                cvdao.update(cv); // Cập nhật vào CSDL
                fillToTableChucVu(); // Cập nhật lại bảng để hiển thị dữ liệu mới
                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Cập nhật chức vụ thành công!");

                // 🔔 Ghi nhận thông báo vào hệ thống chuông
                //themThongBao("Cập nhật", tenCV);
            } catch (Exception e) {
                Message.error("Lỗi: " + e.getMessage());
                Notifications.getInstance().show(Notifications.Type.INFO, "Cập nhật chức vụ thất bại!");
            }
        }
    }
    
    
    public Set<String> getDanhSachMaChucvu() {
        Set<String> dsMaLoai = new HashSet<>();
        for (int i = 0; i < tbl_chucVu.getRowCount(); i++) {
            String maLoai = tbl_chucVu.getValueAt(i, 0).toString();
            dsMaLoai.add(maLoai);
        }
        return dsMaLoai;
    }
    
    
    
    
    
    
    
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        btn_timKiem = new javax.swing.JButton();
        txt_timKiem = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_chucVu = new javax.swing.JTable();
        btn_xoa = new javax.swing.JButton();
        btn_sua = new javax.swing.JButton();
        btn_them = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Chức Vụ");

        btn_timKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawer/icon/search.png"))); // NOI18N

        tbl_chucVu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Mã Chức Vụ", "Tên Chức Vụ"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_chucVu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_chucVuMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_chucVu);

        btn_xoa.setText("Xóa");
        btn_xoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_xoaActionPerformed(evt);
            }
        });

        btn_sua.setText("Sửa");
        btn_sua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_suaActionPerformed(evt);
            }
        });

        btn_them.setText("Thêm");
        btn_them.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_themActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btn_timKiem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 277, Short.MAX_VALUE)
                        .addComponent(btn_them)
                        .addGap(18, 18, 18)
                        .addComponent(btn_xoa)
                        .addGap(18, 18, 18)
                        .addComponent(btn_sua)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_xoa)
                        .addComponent(btn_them)
                        .addComponent(btn_sua)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tbl_chucVuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_chucVuMouseClicked
        // TODO add your handling code here:
        int selectedRow = tbl_chucVu.getSelectedRow(); // Lấy dòng đang chọn

        if (selectedRow != -1) { // Kiểm tra có dòng được chọn không
            String tenCV = tbl_chucVu.getValueAt(selectedRow, 1).toString();

            // Lưu vào biến toàn cục để truyền vào JDialo
            selectedtenCV = tenCV;
        }
    }//GEN-LAST:event_tbl_chucVuMouseClicked

    private void btn_xoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_xoaActionPerformed
        deleteChucVu();
    }//GEN-LAST:event_btn_xoaActionPerformed

    private void btn_suaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_suaActionPerformed
        updateChucVu();
    }//GEN-LAST:event_btn_suaActionPerformed

    private void btn_themActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_themActionPerformed
        // TODO add your handling code here:
        DiaLog_ChucVu dialog = new DiaLog_ChucVu(null, true, this);
        dialog.setdata(selectedtenCV);
        dialog.setVisible(true);
    }//GEN-LAST:event_btn_themActionPerformed

    @Override
    public boolean formClose() {
        return true;
    }

    @Override
    public void formOpen() {
        System.out.println("Duy Dep Trai");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_sua;
    private javax.swing.JButton btn_them;
    private javax.swing.JButton btn_timKiem;
    private javax.swing.JButton btn_xoa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_chucVu;
    private javax.swing.JTextField txt_timKiem;
    // End of variables declaration//GEN-END:variables
}
