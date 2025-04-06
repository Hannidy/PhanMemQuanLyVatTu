package form;

import dao.KhoDAO;
import entity.model_Kho;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import tabbed.TabbedForm;
import util.Message;
import raven.toast.Notifications;

public class Kho_Form extends TabbedForm {

    private DefaultTableModel tbl_ModelKho;
    private KhoDAO kDAO = new KhoDAO();
    private List<model_Kho> list_Kho = new ArrayList<model_Kho>();

    private String selectedtenKho = "";  // Biến để lấy dữ liệu dòng
    private String selectedmaLoaiVatTu = "";  // Biến để lấy dữ liệu dòng
    private String selecteddiaChi = "";     // Biến để lấy dữ liệu dòng

    public Kho_Form() {
        initComponents();
        tbl_ModelKho = (DefaultTableModel) tbl_Kho.getModel();
        fillToTableKho();
    }

    public void fillToTableKho() {
        try {
            // Xóa toàn bộ dữ liệu cũ trước khi thêm mới
            tbl_ModelKho.setRowCount(0);

            // Lấy danh sách kho từ database
            list_Kho = kDAO.selectAll();
            if (list_Kho != null) {
                for (model_Kho k : list_Kho) {
                    tbl_ModelKho.addRow(new Object[]{
                        k.getMaKho(), // Mã Kho
                        k.getTenKho(), // Tên Kho
                        k.getMaloaivatTu(), // Mã Loại Vật Tư
                        k.getDiaChi()// Địa Chỉ
                    });
                }
            }
        } catch (Exception e) { // In lỗi để dễ debug
            // In lỗi để dễ debug
            JOptionPane.showMessageDialog(null, "Lỗi truy vấn dữ liệu: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteKho() {
        int[] selectedRows = tbl_Kho.getSelectedRows(); // Lấy tất cả các dòng được chọn

        if (selectedRows.length == 0) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Chọn ít nhất một dòng để xóa!");
            return;
        }

        boolean confirm = Message.confirm("Bạn có chắc chắn muốn xóa " + selectedRows.length + " vật tư?");
        if (!confirm) {
            return;
        }

        try {
            List<String> danhSachXoa = new ArrayList<>(); // Lưu các vật tư bị xóa để ghi vào thông báo

            for (int i = selectedRows.length - 1; i >= 0; i--) { // Xóa từ dưới lên để tránh lỗi chỉ số
                int row = selectedRows[i];
                String maVatTu = tbl_Kho.getValueAt(row, 0).toString();
                kDAO.delete(maVatTu); // Xóa từng vật tư
                danhSachXoa.add(maVatTu); // Thêm vào danh sách để ghi nhận thông báo
            }

            fillToTableKho(); // Cập nhật lại bảng sau khi xóa
            //thongke();
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Đã xóa " + selectedRows.length + " vật tư!");

            // 🔔 Cập nhật thông báo chuông sau khi hoàn tất tất cả các xóa
            for (String maKho : danhSachXoa) {
                //themThongBao("Xóa", maKho);
            }

        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Không thể xóa vật tư!");
        }
    }

    private void updateKho() {
        int row = tbl_Kho.getSelectedRow();
        if (row < 0) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Chọn một dòng để cập nhật!");
            return;
        }

        // Lấy dữ liệu từ JTable
        String maKho = tbl_Kho.getValueAt(row, 0).toString();
        String tenKho = tbl_Kho.getValueAt(row, 1).toString();
        String maLoaiVT = tbl_Kho.getValueAt(row, 2).toString();
        String diaChi = tbl_Kho.getValueAt(row, 3).toString();

        // Kiểm tra nếu có ô nào bị bỏ trống
        if (tenKho.isEmpty() || maLoaiVT.isEmpty() || diaChi.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        // Tạo đối tượng Kho mới
        model_Kho k = new model_Kho();
        k.setMaKho(maKho);
        k.setTenKho(tenKho);
        k.setMaloaivatTu(maLoaiVT);
        k.setDiaChi(diaChi);

        // Xác nhận cập nhật
        boolean confirm = Message.confirm("Bạn có chắc chắn muốn cập nhật kho có mã '" + maKho + "'?");
        if (confirm) {
            try {
                kDAO.update(k); // Cập nhật vào CSDL
                fillToTableKho(); // Cập nhật lại bảng để hiển thị dữ liệu mới
                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Cập nhật kho thành công!");

                // 🔔 Ghi nhận thông báo vào hệ thống chuông
                //themThongBao("Cập nhật", tenKho);
            } catch (SQLException e) {
                Message.error("Lỗi: " + e.getMessage());
                Notifications.getInstance().show(Notifications.Type.INFO, "Cập nhật kho thất bại!");
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        btn_timKiem = new javax.swing.JButton();
        txt_timKiem = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_Kho = new javax.swing.JTable();
        btn_them = new javax.swing.JButton();
        btn_xoa = new javax.swing.JButton();
        btn_sua = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setText("Kho");

        btn_timKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawer/icon/search.png"))); // NOI18N

        tbl_Kho.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã Kho", "Tên Kho", "Mã Loại Vật Tư", "Địa Chỉ"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_Kho.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_KhoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_Kho);

        btn_them.setText("Thêm");
        btn_them.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_themActionPerformed(evt);
            }
        });

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
                    .addComponent(txt_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_them)
                        .addComponent(btn_xoa)
                        .addComponent(btn_sua)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btn_themActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_themActionPerformed
        // TODO add your handling code here:
        LoaiVatTu_Form lvt = new LoaiVatTu_Form();
        Set<String> dsMaLoai = lvt.getDanhSachMaLoai();
        
        
        DiaLog_Kho dialog = new DiaLog_Kho(null, true, this, dsMaLoai);
        dialog.setData(selectedtenKho, selectedmaLoaiVatTu, selecteddiaChi);
        dialog.setMaLoaiData(dsMaLoai);
        dialog.setVisible(true);
    }//GEN-LAST:event_btn_themActionPerformed

    private void tbl_KhoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_KhoMouseClicked
        // TODO add your handling code here:
        int selectedRow = tbl_Kho.getSelectedRow(); // Lấy dòng đang chọn

        if (selectedRow != -1) { // Kiểm tra có dòng được chọn không
            String tenKho = tbl_Kho.getValueAt(selectedRow, 1).toString();
            String maLoaiVatTu = tbl_Kho.getValueAt(selectedRow, 2).toString();
            String diaChi = tbl_Kho.getValueAt(selectedRow, 3).toString();

            // Lưu vào biến toàn cục để truyền vào JDialo
            selectedtenKho = tenKho;
            selectedmaLoaiVatTu = maLoaiVatTu;
            selecteddiaChi = diaChi;
        }
    }//GEN-LAST:event_tbl_KhoMouseClicked

    private void btn_xoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_xoaActionPerformed
        // TODO add your handling code here:
        deleteKho();
    }//GEN-LAST:event_btn_xoaActionPerformed

    private void btn_suaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_suaActionPerformed
        // TODO add your handling code here:
        updateKho();
    }//GEN-LAST:event_btn_suaActionPerformed

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
    private javax.swing.JTable tbl_Kho;
    private javax.swing.JTextField txt_timKiem;
    // End of variables declaration//GEN-END:variables
}
