
package form;

import com.formdev.flatlaf.FlatClientProperties;
import dao.DonViTinhDAO;
import entity.model_DonViTinh;
import java.util.ArrayList;
import dao.LichSuHoatDongDAO;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import tabbed.TabbedForm;
import util.Message;
import raven.toast.Notifications;

public class DonViTinh_Form extends TabbedForm {
public Set<String> getDanhSachNhomVatTu() {
    Set<String> ds = new HashSet<>();
    List<model_DonViTinh> list = dvtDAO.selectAll();
    for (model_DonViTinh dvt : list) {
        ds.add(dvt.getNhomVatTu());
    }
    return ds;
}
    private DefaultTableModel tbl_ModelDonViTinh;
    private LichSuHoatDongDAO lshdDao = new LichSuHoatDongDAO();
    private DonViTinhDAO dvtDAO = new DonViTinhDAO();
    private List<model_DonViTinh> list_DonViTinh = new ArrayList<>();
private String selectedTenDonVi;
private String selectedNhomVatTu;


    public DonViTinh_Form() {
        initComponents();
        initSearchComboBox();
        addSearchFilterDonViTinh();
        addSearchButtonActionDonViTinh();
        tbl_ModelDonViTinh = (DefaultTableModel) tbl_DonViTinh.getModel();
        fillToTableDonViTinh();
        styleUI();
    }

    private void initSearchComboBox() {
    cbo_timKiem.removeAllItems();
    cbo_timKiem.addItem("Mã đơn vị");
    cbo_timKiem.addItem("Tên đơn vị");
    cbo_timKiem.addItem("Nhóm vật tư");
}

    public void styleUI() {
    txt_timKiem.putClientProperty(FlatClientProperties.STYLE, ""
            + "arc:999;"
            + "borderWidth:1;"
            + "focusWidth:1;"
            + "innerFocusWidth:0;");
}

    public void fillToTableDonViTinh() {
    try {
        // Xóa toàn bộ dữ liệu cũ trước khi thêm mới
        tbl_ModelDonViTinh.setRowCount(0);

        // Lấy danh sách đơn vị tính từ database
        List<model_DonViTinh> list_DonViTinh = dvtDAO.selectAll();
        if (list_DonViTinh != null) {
            for (model_DonViTinh dvt : list_DonViTinh) {
                tbl_ModelDonViTinh.addRow(new Object[]{
                    dvt.getMaDonVi(),     // Mã đơn vị
                    dvt.getTenDonVi(),    // Tên đơn vị
                    dvt.getNhomVatTu()    // Nhóm vật tư
                });
            }
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "❌ Lỗi truy vấn dữ liệu: " + e.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
    public void deleteDonViTinh() {
        int[] selectedRows = tbl_DonViTinh.getSelectedRows(); // Lấy tất cả các dòng được chọn

        if (selectedRows.length == 0) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Chọn ít nhất một dòng để xóa!");
            return;
        }

        boolean confirm = Message.confirm("Bạn có chắc chắn muốn xóa " + selectedRows.length + " đơn vị tính?");
        if (!confirm) {
            return;
        }

        try {
            List<Integer> danhSachXoa = new ArrayList<>(); // Lưu các mã đơn vị bị xóa

            for (int i = selectedRows.length - 1; i >= 0; i--) { // Xóa từ dưới lên
                int row = selectedRows[i];
                int maDonVi = Integer.parseInt(tbl_DonViTinh.getValueAt(row, 0).toString());
                dvtDAO.delete(maDonVi); // Gọi DAO xóa
                danhSachXoa.add(maDonVi);
            }

            fillToTableDonViTinh(); // Refresh lại bảng
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "✅ Đã xóa " + selectedRows.length + " đơn vị tính!");

            // Ghi vào bảng LICHSUHOATDONG
            lshdDao.saveThaoTac("Xóa", "Đơn Vị Tính", "Xóa " + selectedRows.length + " đơn vị tính: " + danhSachXoa.toString());

            // 🔔 Ghi log nếu cần
            for (Integer ma : danhSachXoa) {
                // themThongBao("Xóa", String.valueOf(ma));
            }

        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.INFO, "❌ Không thể xóa đơn vị tính!");

            // Ghi vào bảng LICHSUHOATDONG khi thất bại
            lshdDao.saveThaoTac("Xóa thất bại", "Đơn Vị Tính", "Xóa " + selectedRows.length + " đơn vị tính thất bại");
        }
    }

    public void updateDonViTinh() {
        int row = tbl_DonViTinh.getSelectedRow();
        if (row < 0) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Chọn một dòng để cập nhật!");
            return;
        }

        // Lấy dữ liệu từ JTable chỉ với 3 cột
        int maDonVi = Integer.parseInt(tbl_DonViTinh.getValueAt(row, 0).toString());
        String tenDonVi = tbl_DonViTinh.getValueAt(row, 1).toString().trim();
        String nhomVatTu = tbl_DonViTinh.getValueAt(row, 2).toString().trim();

        // Kiểm tra nếu có ô nào bị bỏ trống
        if (tenDonVi.isEmpty() || nhomVatTu.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        // Tạo đối tượng Đơn Vị Tính mới
        model_DonViTinh dvt = new model_DonViTinh();
        dvt.setMaDonVi(maDonVi);
        dvt.setTenDonVi(tenDonVi);
        dvt.setNhomVatTu(nhomVatTu);

        // Xác nhận cập nhật
        boolean confirm = Message.confirm("Bạn có chắc chắn muốn cập nhật đơn vị tính có mã '" + maDonVi + "'?");
        if (confirm) {
            try {
                dvtDAO.update(dvt); // Cập nhật vào CSDL
                fillToTableDonViTinh(); // Cập nhật lại bảng
                Notifications.getInstance().show(Notifications.Type.SUCCESS, "✅ Cập nhật đơn vị tính thành công!");

                // Ghi vào bảng LICHSUHOATDONG
                lshdDao.saveThaoTac("Sửa", "Đơn Vị Tính", "Cập nhật đơn vị tính: Mã: " + maDonVi + ", Tên: " + tenDonVi);
            } catch (Exception e) {
                Message.error("Lỗi: " + e.getMessage());
                Notifications.getInstance().show(Notifications.Type.INFO, "❌ Cập nhật đơn vị tính thất bại!");

                // Ghi vào bảng LICHSUHOATDONG khi thất bại
                lshdDao.saveThaoTac("Sửa thất bại", "Đơn Vị Tính", "Cập nhật đơn vị tính thất bại: Mã: " + maDonVi + ", Tên: " + tenDonVi);
            }
        }
    }
    
    public void addSearchFilterDonViTinh() {  // Gắn listener theo dõi thay đổi của txt_timKiem
    txt_timKiem.getDocument().addDocumentListener(new DocumentListener() {
        public void insertUpdate(DocumentEvent e) {
            autoSearch();
        }

        public void removeUpdate(DocumentEvent e) {
            autoSearch();
        }

        public void changedUpdate(DocumentEvent e) {
            autoSearch();
        }

        private void autoSearch() {  // Lọc theo combobox
            String selectedCriteria = (String) cbo_timKiem.getSelectedItem();
            String keyword = txt_timKiem.getText().trim();

            int columnIndex = -1;
            switch (selectedCriteria) {
                case "Mã đơn vị":
                    columnIndex = 0;
                    break;
                case "Tên đơn vị":
                    columnIndex = 1;
                    break;
                case "Nhóm vật tư":
                    columnIndex = 2;
                    break;
            }

            if (keyword.isEmpty()) {
                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) tbl_DonViTinh.getModel());
                tbl_DonViTinh.setRowSorter(sorter);
                sorter.setRowFilter(null);
                return;
            }

            // ⚠️ Nếu tìm theo tên đơn vị mà nhập toàn số thì cảnh báo
            if (selectedCriteria.equals("Tên đơn vị") && keyword.matches("\\d+")) {
                Notifications.getInstance().show(Notifications.Type.INFO, "Tên đơn vị không thể là số!");
                return;
            }

            // ✅ Lọc tự động (không phân biệt hoa thường)
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) tbl_DonViTinh.getModel());
            tbl_DonViTinh.setRowSorter(sorter);
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(keyword), columnIndex));
        }
    });
}
    public void searchFilterDonViTinh() {
    String keyword = txt_timKiem.getText().trim().toLowerCase();
    DefaultTableModel model = (DefaultTableModel) tbl_DonViTinh.getModel();
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
    tbl_DonViTinh.setRowSorter(sorter);

    sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
        @Override
        public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
            for (int i = 0; i < 3; i++) { // Cột 0: Mã đơn vị, 1: Tên đơn vị, 2: Nhóm vật tư
                String value = entry.getStringValue(i).toLowerCase();
                if (value.contains(keyword)) {
                    return true;
                }
            }
            return false;
        }
    });
}
    
    private void addSearchButtonActionDonViTinh() {
    btn_timKiem.addActionListener(e -> {
        String selectedCriteria = (String) cbo_timKiem.getSelectedItem();
        String keyword = txt_timKiem.getText().trim();

        if (keyword.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập từ khóa tìm kiếm!");
            return;
        }

        int columnIndex = -1;

        switch (selectedCriteria) {
            case "Mã đơn vị":
                columnIndex = 0;
                break;
            case "Tên đơn vị":
                columnIndex = 1;
                break;
            case "Nhóm vật tư":
                columnIndex = 2;
                break;
        }

        if (columnIndex == -1) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Tiêu chí tìm kiếm không hợp lệ!");
            return;
        }

        if (selectedCriteria.equals("Tên đơn vị") && keyword.matches("\\d+")) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Tên đơn vị không thể là số!");
            return;
        }

        if ((selectedCriteria.equals("Mã đơn vị") || selectedCriteria.equals("Nhóm vật tư")) && !keyword.matches("\\w+")) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Giá trị nhập không hợp lệ!");
            return;
        }
        if (selectedCriteria.equals("Nhóm vật tư") && keyword.matches("\\d+")) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Nhóm vật tư không thể là số!");
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tbl_DonViTinh.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        tbl_DonViTinh.setRowSorter(sorter);

        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(keyword), columnIndex));
    });
}


    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cbo_timKiem = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_DonViTinh = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btn_timKiem = new javax.swing.JButton();
        txt_timKiem = new javax.swing.JTextField();
        btn_xoa = new javax.swing.JButton();
        btn_them = new javax.swing.JButton();
        btn_sua = new javax.swing.JButton();

        tbl_DonViTinh.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Mã đơn vị", "Tên đơn vị", "Nhóm vật tư"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_DonViTinh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_DonViTinhMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_DonViTinh);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setText("Đơn vị tính");

        btn_timKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawer/icon/search.png"))); // NOI18N
        btn_timKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_timKiemActionPerformed(evt);
            }
        });

        btn_xoa.setText("Xóa");
        btn_xoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_xoaActionPerformed(evt);
            }
        });

        btn_them.setText("Thêm");
        btn_them.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_themActionPerformed(evt);
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
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btn_timKiem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cbo_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 557, Short.MAX_VALUE)
                        .addComponent(btn_them)
                        .addGap(18, 18, 18)
                        .addComponent(btn_xoa)
                        .addGap(18, 18, 18)
                        .addComponent(btn_sua)))
                .addContainerGap())
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1300, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btn_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbo_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_xoa)
                        .addComponent(btn_sua)
                        .addComponent(btn_them)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 672, Short.MAX_VALUE)
                .addGap(18, 18, 18))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tbl_DonViTinhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_DonViTinhMouseClicked

    }//GEN-LAST:event_tbl_DonViTinhMouseClicked

    private void btn_timKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_timKiemActionPerformed
      
    }//GEN-LAST:event_btn_timKiemActionPerformed

    private void btn_xoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_xoaActionPerformed
            deleteDonViTinh();    }//GEN-LAST:event_btn_xoaActionPerformed

    private void btn_themActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_themActionPerformed
    Set<String> dsNhomVatTu = this.getDanhSachNhomVatTu(); // Đã định nghĩa hàm này ở trên

    DiaLog_DonViTinh dialog = new DiaLog_DonViTinh(null, true, this, dsNhomVatTu);
    dialog.setNhomVatTuData(dsNhomVatTu);
    dialog.setData(selectedTenDonVi, selectedNhomVatTu); 
    dialog.setVisible(true);

    }//GEN-LAST:event_btn_themActionPerformed

    private void btn_suaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_suaActionPerformed
        updateDonViTinh();
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
    private javax.swing.JComboBox<String> cbo_timKiem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_DonViTinh;
    private javax.swing.JTextField txt_timKiem;
    // End of variables declaration//GEN-END:variables
}
