
package form;

import com.formdev.flatlaf.FlatClientProperties;
import dao.NhanVienDAO;
import entity.model_NhanVien;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import dao.LichSuHoatDongDAO;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import raven.toast.Notifications;
import tabbed.TabbedForm;
import util.Message;

public class NhanVien_Form extends TabbedForm {
    
    public DefaultTableModel tbl_ModelNhanVien;
    private NhanVienDAO nvdao = new NhanVienDAO();
    private List<model_NhanVien> list_NhanVien = new ArrayList<model_NhanVien>();
    private LichSuHoatDongDAO lshdDao = new LichSuHoatDongDAO();

    private String selectedTenNhanVien = "";  // Biến để lấy dữ liệu dòng
    private String selectedmaChucvu = "";  // Biến để lấy dữ liệu dòng
    private String selectedmaPhongBan = "";
    private String selectedemail = "";
    private String selectedsoDienthoai = "";
    private String selectedtrangThai = "";
    
    
    public NhanVien_Form() {
        initComponents();
        initSearchComboBox();
        addSearchFilter();
        addSearchButtonAction();
        tbl_ModelNhanVien = (DefaultTableModel) tbl_NhanVien.getModel();
        fillToTableNhanVien();
        styleUI();
    }
    
    public void styleUI() {
        txt_TimKiem.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:999;"
                + "borderWidth:1;"
                + "focusWidth:1;"
                + "innerFocusWidth:0;");

    }
    // du lieu vao combo box
    private void initSearchComboBox() {
        cbo_TimKiem.removeAllItems();
        cbo_TimKiem.addItem("Mã Nhân Viên");
        cbo_TimKiem.addItem("Tên Nhân Viên");
        cbo_TimKiem.addItem("Mã Chức Vụ");
        cbo_TimKiem.addItem("Mã Phòng Ban");
        cbo_TimKiem.addItem("Email");
        cbo_TimKiem.addItem("Số Điện Thoại");
        cbo_TimKiem.addItem("Trạng Thái");
    }
    
     public void fillToTableNhanVien() {
        try {
            // Xóa toàn bộ dữ liệu cũ trước khi thêm mới
            tbl_ModelNhanVien.setRowCount(0);

            // Lấy danh sách vật tư từ database
            list_NhanVien = nvdao.selectAll();
            if (list_NhanVien!= null) {
                for (model_NhanVien nv : list_NhanVien) {
                    tbl_ModelNhanVien.addRow(new Object[]{
                        nv.getMaNhanVien(),
                        nv.getTenNhanVien(),
                        nv.getMaChucVu(),
                        nv.getMaPhongBan(),
                        nv.getEmail(),
                        nv.getSoDienthoai(),
                        nv.getTrangThai()
                   });
                }
            }
        } catch (Exception e) { // In lỗi để dễ debug
            // In lỗi để dễ debug
            JOptionPane.showMessageDialog(null, "Lỗi truy vấn dữ liệu: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
     public void addSearchFilter() {  // Gắn một listener (trình theo dõi) vào txt_timKiem
        txt_TimKiem.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                autoSearch();
            }

            public void removeUpdate(DocumentEvent e) {
                autoSearch();
            }

            public void changedUpdate(DocumentEvent e) {
                autoSearch();
            }

            private void autoSearch() {  // Tìm kiếm theo combobox
                String selectedCriteria = (String) cbo_TimKiem.getSelectedItem();
                String keyword = txt_TimKiem.getText().trim();

                int columnIndex = -1;
                switch (selectedCriteria) {
                    case "Mã Nhân Viên":
                        columnIndex = 0;
                        break;
                    case "Tên Nhân Viên":
                        columnIndex = 1;
                        break;
                    case "Mã Chức Vụ":
                        columnIndex = 2;
                        break;
                    case "Mã Phòng Ban":
                        columnIndex = 3;
                        break;
                    case "Email":
                        columnIndex = 4;
                        break;
                    case "Số Điện Thoại":
                        columnIndex = 5;
                        break;
                    case "Trạng Thái":
                        columnIndex = 6;
                        break;
                }

                if (keyword.isEmpty()) {
                    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) tbl_NhanVien.getModel());
                    tbl_NhanVien.setRowSorter(sorter);
                    sorter.setRowFilter(null);
                    return;
                }

                // ⚠️ Chỉ kiểm tra nếu đang tìm theo TÊN mà lại nhập toàn số (hiếm gặp)
                if (selectedCriteria.equals("Tên Nhân Viên") && keyword.matches("\\d+")) {
                    Notifications.getInstance().show(Notifications.Type.INFO, "Tên Nhân Viên không thể là số!");
                    return;
                }
                
                // ✅ Lọc tự động (không giới hạn mã phải theo định dạng gì)
                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) tbl_NhanVien.getModel());
                tbl_NhanVien.setRowSorter(sorter);
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(keyword), columnIndex));
            }
        });
    }
     
     private void addSearchButtonAction() {
        btn_TimKiem.addActionListener(e -> {
            String selectedCriteria = (String) cbo_TimKiem.getSelectedItem();
            String keyword = txt_TimKiem.getText().trim();

            if (keyword.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập từ khóa tìm kiếm!");
                return;
            }

            int columnIndex = -1;

            switch (selectedCriteria) {
                    case "Mã Nhân Viên":
                        columnIndex = 0;
                        break;
                    case "Tên Nhân Viên":
                        columnIndex = 1;
                        break;
                    case "Mã Chức Vụ":
                        columnIndex = 2;
                        break;
                    case "Mã Phòng Ban":
                        columnIndex = 3;
                        break;
                    case "Email":
                        columnIndex = 4;
                        break;
                    case "Số Điện Thoại":
                        columnIndex = 5;
                        break;
                    case "Trạng Thái":
                        columnIndex = 6;
                        break;
                }

            if (columnIndex == -1) {
                Notifications.getInstance().show(Notifications.Type.INFO, "Tiêu chí tìm kiếm không hợp lệ!");
                return;
            }

            if (selectedCriteria.equals("Tên Nhân viên") && keyword.matches("\\d+")) {
                Notifications.getInstance().show(Notifications.Type.INFO, "Tên Nhân Viên không thể là số!");
                return;
            }

            if ((selectedCriteria.equals("Mã Nhân Viên") || selectedCriteria.equals("Mã Chức Vụ") || selectedCriteria.equals("Mã Phòng Ban")) && !keyword.matches("\\w+")){
                Notifications.getInstance().show(Notifications.Type.INFO, "Mã không hợp lệ!");
                return;
            }

            DefaultTableModel model = (DefaultTableModel) tbl_NhanVien.getModel();
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
            tbl_NhanVien.setRowSorter(sorter);

            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(keyword), columnIndex));
        });
    }
     
     public void searchFilter() {
        String keyword = txt_TimKiem.getText().trim().toLowerCase();
        DefaultTableModel model = (DefaultTableModel) tbl_NhanVien.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        tbl_NhanVien.setRowSorter(sorter);

        sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
            @Override
            public boolean include(RowFilter.Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                for (int i = 0; i < 3; i++) { // Cột 0: Mã vật tư, 1: Tên, 2: Mã loại
                    String value = entry.getStringValue(i).toLowerCase();
                    if (value.contains(keyword)) {
                        return true;
                    }
                }
                return false;
            }
        });
    }
     
    public void deleteNhanVien() {
        int[] selectedRows = tbl_NhanVien.getSelectedRows();

        if (selectedRows.length == 0) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Chọn ít nhất một dòng để xóa!");
            return;
        }

        boolean confirm = Message.confirm("Bạn có chắc chắn muốn xóa " + selectedRows.length + " Nhân Viên?");
        if (!confirm) {
            return;
        }

        try {
            List<String> danhSachXoa = new ArrayList<>();

            for (int i = selectedRows.length - 1; i >= 0; i--) {
                int row = selectedRows[i];
                String maNhanVien = tbl_NhanVien.getValueAt(row, 0).toString();
                nvdao.delete(maNhanVien);
                danhSachXoa.add(maNhanVien);
            }

            fillToTableNhanVien();
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Đã xóa " + selectedRows.length + " Nhân Viên!");

            // Ghi vào bảng LICHSUHOATDONG
            lshdDao.saveThaoTac("Xóa", "Nhân Viên", "Xóa " + selectedRows.length + " nhân viên, mã: " + String.join(", ", danhSachXoa));

        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Không thể xóa nhân viên!");
        }
    }

    public void updateNhanVien() {
        int row = tbl_NhanVien.getSelectedRow();
        if (row < 0) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Chọn một dòng để cập nhật!");
            return;
        }

        String maNV = tbl_NhanVien.getValueAt(row, 0).toString();
        String tenNV = tbl_NhanVien.getValueAt(row, 1).toString().trim();
        String maCV = tbl_NhanVien.getValueAt(row, 2).toString().trim();
        String maPB = tbl_NhanVien.getValueAt(row, 3).toString().trim();
        String email = tbl_NhanVien.getValueAt(row, 4).toString().trim();
        String sdt = tbl_NhanVien.getValueAt(row, 5).toString().trim();
        String trangthai = tbl_NhanVien.getValueAt(row, 6).toString().trim();

        if (tenNV.isEmpty() || maCV.isEmpty() || maPB.isEmpty() || email.isEmpty() || sdt.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        model_NhanVien nv = new model_NhanVien();
        nv.setMaNhanVien(maNV);
        nv.setTenNhanVien(tenNV);
        nv.setMaChucVu(maCV);
        nv.setMaPhongBan(maPB);
        nv.setEmail(email);
        nv.setSoDienthoai(sdt);
        nv.setTrangThai(trangthai);

        boolean confirm = Message.confirm("Bạn có chắc chắn muốn cập nhật nhân viên có mã '" + maNV + "'?");
        if (confirm) {
            try {
                nvdao.update(nv);
                fillToTableNhanVien();
                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Cập nhật nhân viên thành công!");

                // Ghi vào bảng LICHSUHOATDONG
                lshdDao.saveThaoTac("Sửa", "Nhân Viên", "Sửa thông tin nhân viên với mã " + maNV);

            } catch (Exception e) {
                Message.error("Lỗi: " + e.getMessage());
                Notifications.getInstance().show(Notifications.Type.INFO, "Cập nhật nhân viên thất bại!");
            }
        }
    }
      

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_NhanVien = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btn_Them = new javax.swing.JButton();
        btn_Xoa = new javax.swing.JButton();
        btn_Sua = new javax.swing.JButton();
        txt_TimKiem = new javax.swing.JTextField();
        btn_TimKiem = new javax.swing.JButton();
        cbo_TimKiem = new javax.swing.JComboBox<>();

        tbl_NhanVien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã Nhân Viên ", "Tên Nhân Viên", "Mã Chức Vụ ", "Mã Phòng Ban", "Email", "Số Điện Thoại", "Trạng Thái"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false, false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_NhanVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_NhanVienMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_NhanVien);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setText("Nhân Viên ");

        btn_Them.setText("Thêm");
        btn_Them.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ThemActionPerformed(evt);
            }
        });

        btn_Xoa.setText("Xóa");
        btn_Xoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_XoaActionPerformed(evt);
            }
        });

        btn_Sua.setText("Sửa");
        btn_Sua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SuaActionPerformed(evt);
            }
        });

        btn_TimKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawer/icon/search.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btn_TimKiem)
                        .addGap(18, 18, 18)
                        .addComponent(txt_TimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cbo_TimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 380, Short.MAX_VALUE)
                        .addComponent(btn_Them, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_Xoa, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(btn_Sua, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_TimKiem, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btn_Xoa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_TimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbo_TimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_Them, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_Sua, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 481, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tbl_NhanVienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_NhanVienMouseClicked
        int selectedRow = tbl_NhanVien.getSelectedRow(); // Lấy dòng đang chọn

        if (selectedRow != -1) { // Kiểm tra có dòng được chọn không
            String tenNV = tbl_NhanVien.getValueAt(selectedRow, 1).toString();
            String maCV = tbl_NhanVien.getValueAt(selectedRow, 2).toString();
            String maPB = tbl_NhanVien.getValueAt(selectedRow, 3).toString();
            String email = tbl_NhanVien.getValueAt(selectedRow, 4).toString();
            String sdt = tbl_NhanVien.getValueAt(selectedRow, 5).toString();
            String trangthai = tbl_NhanVien.getValueAt(selectedRow, 6).toString();

            // Lưu vào biến toàn cục để truyền vào JDialo
            selectedTenNhanVien = tenNV;  
            selectedmaChucvu = maCV;  
            selectedmaPhongBan = maPB;
            selectedemail = email;
            selectedsoDienthoai = sdt;
            selectedtrangThai = trangthai;
    }
        
    }//GEN-LAST:event_tbl_NhanVienMouseClicked

    private void btn_XoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_XoaActionPerformed
        deleteNhanVien();
    }//GEN-LAST:event_btn_XoaActionPerformed

    private void btn_SuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SuaActionPerformed
        updateNhanVien();
    }//GEN-LAST:event_btn_SuaActionPerformed

    private void btn_ThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ThemActionPerformed
        ChucVu_Form cvf = new ChucVu_Form();
        PhongBan_Form pbf = new PhongBan_Form();
        
        Set<String> dsmaCV = cvf.getDanhSachMaChucvu();
        Set<String> dsmaPB = pbf.getDanhSachMaPhongBan();

        // Tạo dialog và truyền dữ liệu
        Dialog_NhanVien dialog = new Dialog_NhanVien(null, true, this, dsmaCV , dsmaPB );
        dialog.setDanhSachChucVu(dsmaCV);
        dialog.setDanhsachPhongBan(dsmaPB);
        dialog.setData( selectedTenNhanVien , selectedmaChucvu  , selectedmaPhongBan , selectedemail , selectedsoDienthoai, selectedtrangThai );
        dialog.setVisible(true);
    }//GEN-LAST:event_btn_ThemActionPerformed

    @Override
    public boolean formClose() {
       return true ;
        }

    @Override
    public void formOpen() {
        System.out.println("Duy Dep Trai");
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Sua;
    private javax.swing.JButton btn_Them;
    private javax.swing.JButton btn_TimKiem;
    private javax.swing.JButton btn_Xoa;
    private javax.swing.JComboBox<String> cbo_TimKiem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_NhanVien;
    private javax.swing.JTextField txt_TimKiem;
    // End of variables declaration//GEN-END:variables
}
