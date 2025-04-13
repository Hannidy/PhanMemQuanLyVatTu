
package form;

import dao.VatTuLoiDAO;
import entity.model_VatTuLoi;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import tabbed.TabbedForm;
import form.BaoHanh_from;
import java.util.ArrayList;
import java.util.Arrays;
public class VatTuLoi_BaoHanh_Form extends TabbedForm {
    private DefaultTableModel tbl_model;
    private Map<String, String> trangThaiMap = new HashMap<>();
    private BaoHanh_from baoHanhForm;
    private List<Object[]> danhSachGuiBaoHanh = new ArrayList<>();
    private static VatTuLoi_BaoHanh_Form instance;
    
    public VatTuLoi_BaoHanh_Form() {
        initComponents();
        instance = this;  // Lưu instance
        cbo_TimKiem.setModel(new DefaultComboBoxModel<>(new String[]{
            "Mã kho", "Mã vật tư"
        }));
        fillTable();
//        initTable();
        addSearchFilter();
        addSearchButtonAction();
    }
    
    public static VatTuLoi_BaoHanh_Form getInstance() {
    return instance;
}
    
   private void fillTable() {
    tbl_model = (DefaultTableModel) tbl_VatTuLoi.getModel();
    tbl_model.setRowCount(0); // Xóa sạch bảng

    try {
        VatTuLoiDAO dao = new VatTuLoiDAO();
        List<model_VatTuLoi> list = dao.selectAll(); // Lấy dữ liệu từ CSDL

        for (model_VatTuLoi vt : list) {
            tbl_model.addRow(new Object[]{
                vt.getMaKho(),
                vt.getMaVatTu(),
                vt.getMaNhaCungCap(),  // Hiển thị MaNhaCungCap
                vt.getNhaCungCap(),    // Hiển thị TenNhaCungCap
                vt.getTrangThai()
            });
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu vật tư lỗi!", "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}


    public void getBaoHanhTableModel(BaoHanh_from baoHanhForm) {
    this.baoHanhForm = baoHanhForm;
    }
public void initTable() {
    tbl_model = (DefaultTableModel) tbl_VatTuLoi.getModel();
    tbl_VatTuLoi.setModel(tbl_model);

    // Tạo ComboBox trạng thái
    String[] trangThaiOptions = {"Hàng lỗi", "Đang chờ duyệt", "Đã được bảo hành"};
    JComboBox<String> comboBoxTrangThai = new JComboBox<>(trangThaiOptions);

    // Gán ComboBox làm CellEditor cho cột trạng thái (index 3)
    TableColumn column = tbl_VatTuLoi.getColumnModel().getColumn(4);
    column.setCellEditor(new DefaultCellEditor(comboBoxTrangThai));

    // Thêm listener để kiểm tra trạng thái
    tbl_VatTuLoi.getModel().addTableModelListener(e -> {
        if (e.getColumn() == 4) {
            int row = e.getFirstRow();
            String newStatus = tbl_VatTuLoi.getValueAt(row, 4).toString();
            if (!Arrays.asList(trangThaiOptions).contains(newStatus)) {
                JOptionPane.showMessageDialog(this, "Trạng thái không hợp lệ!");
                tbl_VatTuLoi.setValueAt("Hàng lỗi", row, 4); // Reset về trạng thái mặc định
            }
        }
    });
}

private void luuTrangThaiVaoFile() {
    try (BufferedWriter bw = new BufferedWriter(new FileWriter("trangthai.txt"))) {
        for (Map.Entry<String, String> entry : trangThaiMap.entrySet()) {
            bw.write(entry.getKey() + ";" + entry.getValue());
            bw.newLine();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
private void docTrangThaiTuFile() {
    File file = new File("trangthai.txt");
    if (!file.exists()) return;

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(";");
            if (parts.length == 2) {
                trangThaiMap.put(parts[0], parts[1]);
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
private void xoaVatTuLoi() {
    int selectedRow = tbl_VatTuLoi.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn vật tư để xóa!");
        return;
    }

    // Chuyển từ chỉ số hiển thị (view) sang model
    int modelRow = tbl_VatTuLoi.convertRowIndexToModel(selectedRow);
    String maVatTu = tbl_VatTuLoi.getValueAt(selectedRow, 1).toString();

    int confirm = JOptionPane.showConfirmDialog(
        this,
        "Bạn có chắc muốn xóa vật tư này?",
        "Xác nhận xóa",
        JOptionPane.YES_NO_OPTION
    );

    if (confirm == JOptionPane.YES_OPTION) {
        try {
            VatTuLoiDAO dao = new VatTuLoiDAO();
            dao.delete(maVatTu); // Xóa trong cơ sở dữ liệu
            DefaultTableModel model = (DefaultTableModel) tbl_VatTuLoi.getModel();
            model.removeRow(modelRow); // Xóa trên giao diện
            JOptionPane.showMessageDialog(this, "Xóa vật tư thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa vật tư!");
        }
    }
}

public void addSearchFilter() {
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

        private void autoSearch() {
            String selectedCriteria = (String) cbo_TimKiem.getSelectedItem();
            String keyword = txt_timKiem.getText() != null ? txt_timKiem.getText().trim() : "";
            int columnIndex = -1;

            if (selectedCriteria == null) return;

            switch (selectedCriteria) {
                case "Mã kho":
                    columnIndex = 0;
                    break;
                case "Mã vật tư":
                    columnIndex = 1;
                    break;
                case "Nhà cung cấp":
                    columnIndex = 2;
                    break;
                case "Trạng thái":
                    columnIndex = 3;
                    break;
                default:
                    return;
            }

            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) tbl_VatTuLoi.getModel());
            tbl_VatTuLoi.setRowSorter(sorter);
            if (keyword.isEmpty()) {
                sorter.setRowFilter(null);
                return;
            }
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(keyword), columnIndex));
        }
    });
}
private void addSearchButtonAction() {
    btn_TimKiem.addActionListener(e -> {
        String selectedCriteria = (String) cbo_TimKiem.getSelectedItem();
        String keyword = txt_timKiem.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa tìm kiếm!");
            return;
        }

        int columnIndex = -1;
        switch (selectedCriteria) {
            case "Mã kho":
                columnIndex = 0;
                break;
            case "Mã vật tư":
                columnIndex = 1;
                break;
            case "Nhà cung cấp":
                columnIndex = 2;
                break;
            case "Trạng thái":
                columnIndex = 3;
                break;
        }

        if (columnIndex == -1) {
            JOptionPane.showMessageDialog(this, "Tiêu chí tìm kiếm không hợp lệ!");
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tbl_VatTuLoi.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        tbl_VatTuLoi.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(keyword), columnIndex));
    });
}
public void guiVatTuBaoHanh(JTable tableVatTuLoi) {
    DefaultTableModel modelLoi = (DefaultTableModel) tableVatTuLoi.getModel();
    int selectedRow = tableVatTuLoi.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(null, "Vui lòng chọn một dòng để gửi bảo hành!");
        return;
    }

    Object maKhoObj = modelLoi.getValueAt(selectedRow, 0);
    Object maVatTuObj = modelLoi.getValueAt(selectedRow, 1);
    Object maNhaCungCapObj = modelLoi.getValueAt(selectedRow, 2); // MaNhaCungCap
    Object tenNhaCungCapObj = modelLoi.getValueAt(selectedRow, 3); // TenNhaCungCap
    Object trangThaiObj = modelLoi.getValueAt(selectedRow, 4);

    if (maKhoObj == null || maVatTuObj == null || maNhaCungCapObj == null || tenNhaCungCapObj == null || trangThaiObj == null) {
        JOptionPane.showMessageDialog(null, "Dữ liệu không hợp lệ, vui lòng kiểm tra lại!");
        return;
    }

    String trangThai = trangThaiObj.toString();
    if (!trangThai.equalsIgnoreCase("Hàng lỗi")) {
        JOptionPane.showMessageDialog(null, "Chỉ có thể gửi vật tư có trạng thái 'Hàng lỗi'!");
        return;
    }

    // Lưu vào danh sách tạm
    Object[] dongMoi = new Object[]{maKhoObj, maVatTuObj, maNhaCungCapObj, tenNhaCungCapObj, "Đang chờ duyệt"};
    danhSachGuiBaoHanh.add(dongMoi);

    // Cập nhật trạng thái trong bảng
    modelLoi.setValueAt("Đang chờ duyệt", selectedRow, 4);

    // Debug: In dữ liệu vừa thêm
    System.out.println("Đã thêm vào danhSachGuiBaoHanh: MaKho=" + maKhoObj + ", MaVatTu=" + maVatTuObj + ", MaNhaCungCap=" + maNhaCungCapObj + ", TenNhaCungCap=" + tenNhaCungCapObj + ", TrangThai=Đang chờ duyệt");

    // Mở form Bảo hành và truyền dữ liệu
    if (baoHanhForm == null) {
        baoHanhForm = new BaoHanh_from();
    }
    DefaultTableModel modelBaoHanh = (DefaultTableModel) baoHanhForm.getTbl_BaoHanh().getModel();
    modelBaoHanh.setRowCount(0); // Xóa dữ liệu cũ
    modelBaoHanh.setColumnIdentifiers(new String[]{"Mã Kho", "Mã Vật Tư", "Nhà Cung Cấp", "Trạng Thái"});
    for (Object[] dong : danhSachGuiBaoHanh) {
        modelBaoHanh.addRow(new Object[]{dong[0], dong[1], dong[3], dong[4]}); // Hiển thị TenNhaCungCap (dong[3])
    }
    baoHanhForm.setVisible(true);
}
    

public List<Object[]> getDanhSachGuiBaoHanh() {
    return danhSachGuiBaoHanh;
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_VatTuLoi = new javax.swing.JTable();
        btn_Gui = new javax.swing.JButton();
        btn_TimKiem = new javax.swing.JButton();
        txt_timKiem = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cbo_TimKiem = new javax.swing.JComboBox<>();
        btn_Xoa = new javax.swing.JButton();

        tbl_VatTuLoi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã kho", "Mã vật tư", "Mã Nhà Cung Cấp", "Nhà Cung Cấp", "Trạng Thái"
            }
        ));
        jScrollPane1.setViewportView(tbl_VatTuLoi);

        btn_Gui.setText("Gửi");
        btn_Gui.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_GuiActionPerformed(evt);
            }
        });

        btn_TimKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawer/icon/search.png"))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setText("Vật tư lỗi bảo hành");

        cbo_TimKiem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mã vật tư", "nhà cung cấp" }));

        btn_Xoa.setText("Xóa");
        btn_Xoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_XoaActionPerformed(evt);
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
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btn_TimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cbo_TimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 608, Short.MAX_VALUE)
                                .addComponent(btn_Xoa)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_Gui)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_Gui)
                            .addComponent(cbo_TimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_Xoa))
                        .addGap(4, 4, 4))
                    .addComponent(btn_TimKiem))
                .addGap(27, 27, 27)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 490, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btn_GuiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_GuiActionPerformed
        getBaoHanhTableModel(baoHanhForm); // gán cho class hiện tại
        guiVatTuBaoHanh(tbl_VatTuLoi);
    }//GEN-LAST:event_btn_GuiActionPerformed

    private void btn_XoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_XoaActionPerformed
        xoaVatTuLoi();
    }//GEN-LAST:event_btn_XoaActionPerformed

    @Override
    public boolean formClose() {
        return true;
        
    }

    @Override
    public void formOpen() {
        System.out.println("Duy Dep Trai");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Gui;
    private javax.swing.JButton btn_TimKiem;
    private javax.swing.JButton btn_Xoa;
    private javax.swing.JComboBox<String> cbo_TimKiem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_VatTuLoi;
    private javax.swing.JTextField txt_timKiem;
    // End of variables declaration//GEN-END:variables
}
