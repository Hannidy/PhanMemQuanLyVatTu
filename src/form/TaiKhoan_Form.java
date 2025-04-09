
package form;

import tabbed.TabbedForm;
import com.formdev.flatlaf.FlatClientProperties;
import dao.TaiKhoanDAO;
import entity.model_TaiKhoan;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import tabbed.TabbedForm;
import util.Message;
import raven.toast.Notifications;

public class TaiKhoan_Form extends TabbedForm {
    
    public DefaultTableModel tbl_ModelTaiKhoan;
    
    private TaiKhoanDAO tkdao = new TaiKhoanDAO();
    private List<model_TaiKhoan> list_TaiKhoan = new ArrayList<model_TaiKhoan>();

    private String selectedTenTK = "";  // Biến để lấy dữ liệu dòng
    private String selectedMatkhau = "";  // Biến để lấy dữ liệu dòng
    private String selectedmaNhanvien =  "";
    private String selectedtrangThai = "";
    
    public TaiKhoan_Form() {
        initComponents();
        initSearchComboBox();
        addSearchButtonAction();
        addSearchFilter();
        searchFilter();
        init();
        initTable();
        fillToTableTaiKhoan();
        styleUI();
    }
    
    public void initTable() {
    // Sau khi set tbl_ModelTaiKhoan cho bảng:
    tbl_TaiKhoan.setModel(tbl_ModelTaiKhoan);

    // Tạo ComboBox trạng thái
    String[] trangThaiOptions = {"Hoạt Động", "Đang Chờ Xử Lý", "Không Hoạt Động"};
    JComboBox<String> comboBoxTrangThai = new JComboBox<>(trangThaiOptions);

    // Gán ComboBox làm CellEditor cho cột trạng thái (index 3)
    TableColumn column = tbl_TaiKhoan.getColumnModel().getColumn(3);
    column.setCellEditor(new DefaultCellEditor(comboBoxTrangThai));
}

    
    // thêm dữ liệu vào combobox 
    private void initSearchComboBox() {
        cbo_timKiem.removeAllItems();
        cbo_timKiem.addItem("Tài Khoản");
        cbo_timKiem.addItem("Mật Khẩu");
        cbo_timKiem.addItem("Mã Nhân Viên");
        cbo_timKiem.addItem("Trạng Thái");
    }
    
    // chỉnh sửa UI
     public void styleUI() {
        txt_timKiem.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:999;"
                + "borderWidth:1;"
                + "focusWidth:1;"
                + "innerFocusWidth:0;");

    }
     
     public void init() {
        tbl_ModelTaiKhoan = (DefaultTableModel) tbl_TaiKhoan.getModel(); 
        fillToTableTaiKhoan();
    }
     
     // Hiển thị danh sách vật tư lên bảng
    public void fillToTableTaiKhoan() {
        try {
            // Xóa toàn bộ dữ liệu cũ trước khi thêm mới
            tbl_ModelTaiKhoan.setRowCount(0);

            // Lấy danh sách vật tư từ database
            list_TaiKhoan = tkdao.selectAll();
            if (list_TaiKhoan != null) {
                for (model_TaiKhoan tk : list_TaiKhoan) {
                    tbl_ModelTaiKhoan.addRow(new Object[]{
                        tk.getTaiKhoan(),
                        tk.getMatKhau(),
                        tk.getMaNhanVien(),
                        tk.getTrangThai()
                    });
                }
            }
        } catch (Exception e) { // In lỗi để dễ debug
            // In lỗi để dễ debug
            JOptionPane.showMessageDialog(null, "Lỗi truy vấn dữ liệu: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // duyệt tài khoản 
    public void updateTrangThai() {
        int row = tbl_TaiKhoan.getSelectedRow();
        if (row < 0) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Chọn một dòng để cập nhật!");
            return;
        }

        // Lấy dữ liệu từ JTable chỉ với 4 cột
        String taiKhoann = tbl_TaiKhoan.getValueAt(row, 0).toString();
        String matKhau = tbl_TaiKhoan.getValueAt(row, 1).toString().trim();
        String maNhanvien = tbl_TaiKhoan.getValueAt(row, 2).toString().trim();
        String trangthai = tbl_TaiKhoan.getValueAt(row, 3).toString().trim();

        // Kiểm tra nếu có ô nào bị bỏ trống
        if (trangthai.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        // Tạo đối tượng Vật Tư mới
        model_TaiKhoan tk = new model_TaiKhoan();
        tk.setTaiKhoan(taiKhoann);
        tk.setMatKhau(matKhau);
        tk.setMaNhanVien(maNhanvien);
        tk.setTrangThai(trangthai);

        // Xác nhận cập nhật
        boolean confirm = Message.confirm("Bạn có chắc chắn muốn duyệt tài khoản có mã '" + maNhanvien + "'?");
        if (confirm) {
            try {
                tkdao.update(tk); // Cập nhật vào CSDL
                fillToTableTaiKhoan(); // Cập nhật lại bảng để hiển thị dữ liệu mới
                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Duyệt tài khoản thành công!");

                // 🔔 Ghi nhận thông báo vào hệ thống chuông
                //themThongBao("Cập nhật", tenVT);
            } catch (Exception e) {
                Message.error("Lỗi: " + e.getMessage());
                Notifications.getInstance().show(Notifications.Type.INFO, "Duyệt tài khoản thất bại!");
            }
        }
    }
    
    // xóa tài khoản 
     public void deleteTaiKhoan() {
        int[] selectedRows = tbl_TaiKhoan.getSelectedRows(); // Lấy tất cả các dòng được chọn

        if (selectedRows.length == 0) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Chọn ít nhất một dòng để xóa!");
            return;
        }

        boolean confirm = Message.confirm("Bạn có chắc chắn muốn xóa " + selectedRows.length + " tài khoản ?");
        if (!confirm) {
            return;
        }

        try {
            List<String> danhSachXoa = new ArrayList<>(); // Lưu các tài khoản bị xóa để ghi vào thông báo

            for (int i = selectedRows.length - 1; i >= 0; i--) { // Xóa từ dưới lên để tránh lỗi chỉ số
                int row = selectedRows[i];
                String maNhanVien = tbl_TaiKhoan.getValueAt(row, 2).toString();
                tkdao.delete(maNhanVien); // Xóa từng tài khoản 
                danhSachXoa.add(maNhanVien); // Thêm vào danh sách để ghi nhận thông báo
            }

            fillToTableTaiKhoan(); // Cập nhật lại bảng sau khi xóa
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Đã xóa " + selectedRows.length + " tài khoản !");

            // 🔔 Cập nhật thông báo chuông sau khi hoàn tất tất cả các xóa
            for (String maNhanVien : danhSachXoa) {

            }

        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Không thể xóa tài khoản !");
        }
    }
     
    public void addSearchFilter() {  // Gắn một listener (trình theo dõi) vào txt_timKiem
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

            private void autoSearch() {  // Tìm kiếm theo combobox
                String selectedCriteria = (String) cbo_timKiem.getSelectedItem();
                String keyword = txt_timKiem.getText().trim();

                int columnIndex = -1;
                switch (selectedCriteria) {
                    case "Tài Khoản":
                        columnIndex = 0;
                        break;
                    case "Mật Khẩu":
                        columnIndex = 1;
                        break;
                    case "Mã Nhân Viên":
                        columnIndex = 2;
                        break;
                    case "Trạng Thái":
                        columnIndex = 3;
                        break;
                }

                if (keyword.isEmpty()) {
                    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) tbl_TaiKhoan.getModel());
                    tbl_TaiKhoan.setRowSorter(sorter);
                    sorter.setRowFilter(null);
                    return;
                }

                

                // ✅ Lọc tự động (không giới hạn mã phải theo định dạng gì)
                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) tbl_TaiKhoan.getModel());
                tbl_TaiKhoan.setRowSorter(sorter);
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(keyword), columnIndex));
            }
        });
    }

    public void searchFilter() {
        String keyword = txt_timKiem.getText().trim().toLowerCase();
        DefaultTableModel model = (DefaultTableModel) tbl_TaiKhoan.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        tbl_TaiKhoan.setRowSorter(sorter);

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

    private void addSearchButtonAction() {
        btn_timKiem.addActionListener(e -> {
            String selectedCriteria = (String) cbo_timKiem.getSelectedItem();
            String keyword = txt_timKiem.getText().trim();

            if (keyword.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập từ khóa tìm kiếm!");
                return;
            }

            int columnIndex = -1;

            switch (selectedCriteria) {
                    case "Tài Khoản":
                        columnIndex = 0;
                        break;
                    case "Mật Khẩu":
                        columnIndex = 1;
                        break;
                    case "Mã Nhân Viên":
                        columnIndex = 2;
                        break;
                    case "Trạng Thái":
                        columnIndex = 3;
                        break;
                }

            if (columnIndex == -1) {
                Notifications.getInstance().show(Notifications.Type.INFO, "Tiêu chí tìm kiếm không hợp lệ!");
                return;
            }

            

            if ((selectedCriteria.equals("Mã Nhân Viên")) && !keyword.matches("\\w+")) {
                Notifications.getInstance().show(Notifications.Type.INFO, "Mã không hợp lệ!");
                return;
            }

            DefaultTableModel model = (DefaultTableModel) tbl_TaiKhoan.getModel();
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
            tbl_TaiKhoan.setRowSorter(sorter);

            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(keyword), columnIndex));
        });
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_TaiKhoan = new javax.swing.JTable();
        btn_timKiem = new javax.swing.JButton();
        txt_timKiem = new javax.swing.JTextField();
        cbo_timKiem = new javax.swing.JComboBox<>();
        btn_XoaTK = new javax.swing.JButton();
        btn_DuyetTK = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setText("Tài Khoản");

        tbl_TaiKhoan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Tài Khoản", "Mật Khẩu", "Mã Nhân Viên", "Trạng Thái"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tbl_TaiKhoan);
        if (tbl_TaiKhoan.getColumnModel().getColumnCount() > 0) {
            tbl_TaiKhoan.getColumnModel().getColumn(1).setHeaderValue("Mật Khẩu");
        }

        btn_timKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawer/icon/search.png"))); // NOI18N

        btn_XoaTK.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_XoaTK.setText("Xóa Tài Khoản");
        btn_XoaTK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_XoaTKActionPerformed(evt);
            }
        });

        btn_DuyetTK.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_DuyetTK.setText("Duyệt Tài Khoản");
        btn_DuyetTK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_DuyetTKActionPerformed(evt);
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
                        .addComponent(btn_timKiem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbo_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 374, Short.MAX_VALUE)
                        .addComponent(btn_XoaTK, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_DuyetTK, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_timKiem)
                        .addComponent(btn_XoaTK, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_DuyetTK, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txt_timKiem)
                    .addComponent(cbo_timKiem))
                .addGap(29, 29, 29)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 453, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(10, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btn_DuyetTKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_DuyetTKActionPerformed
        updateTrangThai();
    }//GEN-LAST:event_btn_DuyetTKActionPerformed

    private void btn_XoaTKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_XoaTKActionPerformed
        deleteTaiKhoan();
    }//GEN-LAST:event_btn_XoaTKActionPerformed

    @Override
    public boolean formClose() {
        return true;
        
    }

    @Override
    public void formOpen() {
        System.out.println("Duy Dep Trai");
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_DuyetTK;
    private javax.swing.JButton btn_XoaTK;
    private javax.swing.JButton btn_timKiem;
    private javax.swing.JComboBox<String> cbo_timKiem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_TaiKhoan;
    private javax.swing.JTextField txt_timKiem;
    // End of variables declaration//GEN-END:variables
}
