package form;

import dao.NhaCungCapDAO;
import entity.model_NhaCungCap;
import form.BaoHanh_from;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import dao.LichSuHoatDongDAO;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import tabbed.TabbedForm;
import raven.toast.Notifications;
import util.Message;

public class NhaCungCap_Form extends TabbedForm {
    
    private VatTuLoi_BaoHanh_Form VTL;
    private DefaultTableModel tbl_ModelNhaCungCap;
    private NhaCungCapDAO nccdao = new NhaCungCapDAO();
    private List<model_NhaCungCap> list_NhaCungCap = new ArrayList<model_NhaCungCap>();
    private LichSuHoatDongDAO lshdDao = new LichSuHoatDongDAO();
     private JButton btnBaoHanh;
    private List<Object[]> danhSachGuiBaoHanh = new ArrayList<>();
    private String selectedtenNCC = "";
    private String selectedSDT = "";
    private String selectedemail = "";
    private String selecteddiachi = "";
    public NhaCungCap_Form() {
        initComponents();
        tbl_ModelNhaCungCap = (DefaultTableModel) tbl_nhacungCap.getModel();
        VTL = VatTuLoi_BaoHanh_Form.getInstance();
        if (VTL == null) {
            VTL = new VatTuLoi_BaoHanh_Form();
        }
        fillToTableNhaCungCap();
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
        } catch (Exception e) {
            // In lỗi ra console để dễ debug
            //showNotification("Lỗi truy vấn dữ liệu: ", true);

        }
    }
    public void deleteNhaCungCap() {
        try {
            int row = tbl_nhacungCap.getSelectedRow();
            if (row < 0) {
                Notifications.getInstance().show(Notifications.Type.INFO, "Bạn phải chọn một dòng để xóa!");
                return;
            }

            String maNhaCungCap = tbl_nhacungCap.getValueAt(row, 0).toString();
            String tenNhaCungCap = tbl_nhacungCap.getValueAt(row, 1).toString();

            int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa nhà cung cấp này?",
                    "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                nccdao.delete(maNhaCungCap);
                fillToTableNhaCungCap();
                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Xóa nhà cung cấp thành công!");

                // Ghi vào bảng LICHSUHOATDONG
                lshdDao.saveThaoTac("Xóa", "Nhà Cung Cấp", "Xóa nhà cung cấp: Mã: " + maNhaCungCap + ", Tên: " + tenNhaCungCap);
            }
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Lỗi khi xóa nhà cung cấp: " + e.getMessage());

            // Ghi vào bảng LICHSUHOATDONG khi thất bại
            int row = tbl_nhacungCap.getSelectedRow();
            String maNhaCungCap = row >= 0 ? tbl_nhacungCap.getValueAt(row, 0).toString() : "N/A";
            String tenNhaCungCap = row >= 0 ? tbl_nhacungCap.getValueAt(row, 1).toString() : "N/A";
            lshdDao.saveThaoTac("Xóa thất bại", "Nhà Cung Cấp", "Xóa nhà cung cấp thất bại: Mã: " + maNhaCungCap + ", Tên: " + tenNhaCungCap);
        }
    }

    public void updateNhaCungCap() {
        int row = tbl_nhacungCap.getSelectedRow();
        if (row < 0) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Chọn một dòng để cập nhật!");
            return;
        }

        String maNCC = tbl_nhacungCap.getValueAt(row, 0).toString();
        String tenNCC = tbl_nhacungCap.getValueAt(row, 1).toString().trim();
        String soDienThoai = tbl_nhacungCap.getValueAt(row, 2).toString().trim();
        String email = tbl_nhacungCap.getValueAt(row, 3).toString().trim();
        String diaChi = tbl_nhacungCap.getValueAt(row, 4).toString().trim();

        if (tenNCC.isEmpty() || soDienThoai.isEmpty() || email.isEmpty() || diaChi.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Email không hợp lệ!");
            return;
        }

        if (!soDienThoai.matches("\\d+")) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Số điện thoại chỉ được chứa chữ số!");
            return;
        }

        boolean confirm = Message.confirm("Bạn có chắc chắn muốn cập nhật nhà cung cấp có mã '" + maNCC + "'?");
        if (confirm) {
            try {
                model_NhaCungCap ncc = new model_NhaCungCap();
                ncc.setManhacungCap(maNCC);
                ncc.setTennhacungCap(tenNCC);
                ncc.setSodienThoai(soDienThoai);
                ncc.setEmail(email);
                ncc.setDiaChi(diaChi);

                nccdao.update(ncc);
                fillToTableNhaCungCap();
                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Cập nhật nhà cung cấp thành công!");

                // Ghi vào bảng LICHSUHOATDONG
                lshdDao.saveThaoTac("Sửa", "Nhà Cung Cấp", "Cập nhật nhà cung cấp: Mã: " + maNCC + ", Tên: " + tenNCC);
            } catch (Exception e) {
                Message.error("Lỗi: " + e.getMessage());
                Notifications.getInstance().show(Notifications.Type.INFO, "Cập nhật nhà cung cấp thất bại!");

                // Ghi vào bảng LICHSUHOATDONG khi thất bại
                lshdDao.saveThaoTac("Sửa thất bại", "Nhà Cung Cấp", "Cập nhật nhà cung cấp thất bại: Mã: " + maNCC + ", Tên: " + tenNCC);
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
        tbl_nhacungCap = new javax.swing.JTable();
        btn_them = new javax.swing.JButton();
        btn_xoa = new javax.swing.JButton();
        btn_sua = new javax.swing.JButton();
        btn_BaoHanh = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Nhà Cung Cấp");

        btn_timKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawer/icon/search.png"))); // NOI18N

        tbl_nhacungCap.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã Nhà Cung Cấp", "Tên Nhà Cung Cấp", "Số Điện Thoại", "Email", "Địa Chỉ"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_nhacungCap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_nhacungCapMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_nhacungCap);

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

        btn_BaoHanh.setText("Bảo hành");
        btn_BaoHanh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_BaoHanhActionPerformed(evt);
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 179, Short.MAX_VALUE)
                        .addComponent(btn_BaoHanh)
                        .addGap(18, 18, 18)
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
                        .addComponent(btn_them)
                        .addComponent(btn_xoa)
                        .addComponent(btn_sua)
                        .addComponent(btn_BaoHanh)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tbl_nhacungCapMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_nhacungCapMouseClicked
        // TODO add your handling code here:
        int selectedRow = tbl_nhacungCap.getSelectedRow(); // Lấy dòng đang chọn

        if (selectedRow != -1) { // Kiểm tra có dòng được chọn không
            String tenNCC = tbl_nhacungCap.getValueAt(selectedRow, 1).toString();
            String SDT = tbl_nhacungCap.getValueAt(selectedRow, 2).toString();
            String email = tbl_nhacungCap.getValueAt(selectedRow, 3).toString();
            String diachi = tbl_nhacungCap.getValueAt(selectedRow, 4).toString();

            // Lưu vào biến toàn cục để truyền vào JDialo
            selectedtenNCC = tenNCC;
            selectedSDT = SDT;
            selectedemail = email;
            selecteddiachi = diachi;
        }
    }//GEN-LAST:event_tbl_nhacungCapMouseClicked

    private void btn_themActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_themActionPerformed
        // TODO add your handling code here:
        DiaLog_NhaCungCap dialog = new DiaLog_NhaCungCap(null, true, this);
        dialog.setData(selectedtenNCC, selectedSDT, selectedemail, selecteddiachi);
        dialog.setVisible(true);
    }//GEN-LAST:event_btn_themActionPerformed

    private void btn_xoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_xoaActionPerformed
        // TODO add your handling code here:
        deleteNhaCungCap();
    }//GEN-LAST:event_btn_xoaActionPerformed

    private void btn_suaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_suaActionPerformed
        // TODO add your handling code here:
        updateNhaCungCap();
    }//GEN-LAST:event_btn_suaActionPerformed

    private void btn_BaoHanhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_BaoHanhActionPerformed
  // Tạo instance của BaoHanh_from và lấy model của bảng tbl_BaoHanh
    BaoHanh_from baoHanhForm = new BaoHanh_from(VTL.getDanhSachDangChoDuyet());
    DefaultTableModel modelBaoHanh = (DefaultTableModel) baoHanhForm.getTbl_BaoHanh().getModel();
    modelBaoHanh.setRowCount(0); // Xóa dữ liệu cũ

    // Lấy danh sách các dòng "Đang chờ duyệt" từ VatTuLoi_BaoHanh_Form
    List<Object[]> danhSach = VTL.getDanhSachDangChoDuyet();
    if (danhSach == null || danhSach.isEmpty()) {
        Notifications.getInstance().show(Notifications.Type.INFO, "Không có vật tư nào đang chờ duyệt!");
        baoHanhForm.dispose();
        return;
    }

    // Debug: In danh sách để kiểm tra
    System.out.println("Danh sách đang chờ duyệt:");
    for (Object[] dong : danhSach) {
      //  System.out.println("MaKho: " + dong[0] + ", MaVatTu: " + dong[1] + ", MaNhaCungCap: " + dong[2] + ", TenNhaCungCap: " + dong[3] + ", TrangThai: " + dong[4]);
    }

    // Thêm tất cả các dòng "Đang chờ duyệt" vào tbl_BaoHanh
    for (Object[] dong : danhSach) {
        if (dong.length >= 5 && "Đang chờ duyệt".equalsIgnoreCase(dong[4].toString())) {
            modelBaoHanh.addRow(new Object[]{dong[0], dong[1], dong[3], dong[4]}); // MaKho, MaVatTu, TenNhaCungCap, TrangThai
        }
    }

    // Kiểm tra xem bảng có dữ liệu không
    if (modelBaoHanh.getRowCount() == 0) {
        Notifications.getInstance().show(Notifications.Type.INFO, "Không có vật tư nào đang chờ duyệt!");
        baoHanhForm.dispose();
    } else {
        baoHanhForm.setVisible(true);
    }
   
    }//GEN-LAST:event_btn_BaoHanhActionPerformed

    @Override
    public boolean formClose() {
        return true;

    }

    @Override
    public void formOpen() {
        System.out.println("Duy Dep Trai");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_BaoHanh;
    private javax.swing.JButton btn_sua;
    private javax.swing.JButton btn_them;
    private javax.swing.JButton btn_timKiem;
    private javax.swing.JButton btn_xoa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_nhacungCap;
    private javax.swing.JTextField txt_timKiem;
    // End of variables declaration//GEN-END:variables
}
