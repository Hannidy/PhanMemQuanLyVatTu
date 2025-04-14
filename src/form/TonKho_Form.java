package form;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import tabbed.TabbedForm;
import dao.TonKhoDAO;
import entity.model_TonKho;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import raven.toast.Notifications;

public class TonKho_Form extends TabbedForm {

    private List<model_TonKho> list_TonKho = new ArrayList<>();
    public DefaultTableModel tblModelTonKho;
    TonKhoDAO tk = new TonKhoDAO();

    public TonKho_Form() {
        initComponents();
        cbo_TimKiem.setModel(new DefaultComboBoxModel<>(new String[]{
            "Mã kho", "Mã vật tư", "Vị trí"
        }));
        fillToTableTonKho();
        addSearchFilter();
        addSearchButtonAction();
    }

    public void fillToTableTonKho() {
        try {
            if (tblModelTonKho == null) {
                tblModelTonKho = (DefaultTableModel) tbl_TonKho.getModel();
            }

            tblModelTonKho.setRowCount(0); // Xóa dữ liệu cũ

            list_TonKho = tk.selectAll();
            System.out.println("Danh sách tồn kho: " + list_TonKho);

            if (list_TonKho != null) {
                for (model_TonKho tk : list_TonKho) {
                    tblModelTonKho.addRow(new Object[]{
                        tk.getMaKho(),
                        tk.getMaVatTu(),
                        tk.getSoLuong(),
                        tk.getDonVi(),
                        tk.getViTri()
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi truy vấn dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void capNhatSoLuongTonKho() {
        try {
            // Cập nhật vào DB
            tk.selectAll();

            // Cập nhật lại list từ DB
            list_TonKho = tk.selectAll();

            // Load lại bảng
            fillToTableTonKho();

            JOptionPane.showMessageDialog(null, "Cập nhật số lượng thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật số lượng: " + e.getMessage());
        }
    }

    public void cicki() {
        int row = tbl_TonKho.getSelectedRow();
        if (row >= 0) {
            String maVatTu = tbl_TonKho.getValueAt(row, 0).toString();
            String khoXuat = tbl_TonKho.getValueAt(row, 1).toString();

            // Gọi form phiếu luân chuyển và truyền dữ liệu
            phieuluanchuyen phieuChuyen = new phieuluanchuyen(maVatTu, khoXuat);
            phieuChuyen.setDefaultCloseOperation(phieuluanchuyen.DISPOSE_ON_CLOSE);
            phieuChuyen.setVisible(true);
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

                if (selectedCriteria == null) {
                    return;
                }

                switch (selectedCriteria) {
                    case "Mã kho":
                        columnIndex = 0;
                        break;
                    case "Mã vật tư":
                        columnIndex = 1;
                        break;
                    case "Vị trí":
                        columnIndex = 6;
                        break;
                    default:
                        // Bỏ qua các tiêu chí không được tìm (số lượng, tồn tối thiểu, tối đa)
                        return;
                }

                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) tbl_TonKho.getModel());
                tbl_TonKho.setRowSorter(sorter);

                if (keyword.isEmpty()) {
                    sorter.setRowFilter(null);
                    return;
                }

                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(keyword), columnIndex));
            }
        });
    }

    public void searchFilter() {
        String keyword = txt_timKiem.getText().trim().toLowerCase();
        DefaultTableModel model = (DefaultTableModel) tbl_TonKho.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        tbl_TonKho.setRowSorter(sorter);

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
            String selectedCriteria = (String) cbo_TimKiem.getSelectedItem();
            String keyword = txt_timKiem.getText().trim();

            if (keyword.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập từ khóa tìm kiếm!");
                return;
            }

            int columnIndex = -1;

            switch (selectedCriteria) {
                case "Mã kho":
                    columnIndex = 0;
                    break;
                case "Mã vật tự":
                    columnIndex = 1;
                    break;
                case "Vị trí":
                    columnIndex = 2;
                    break;
            }

            if (columnIndex == -1) {
                Notifications.getInstance().show(Notifications.Type.INFO, "Tiêu chí tìm kiếm không hợp lệ!");
                return;
            }

            if (selectedCriteria.equals("Mã kho") && keyword.matches("\\w+")) {
                Notifications.getInstance().show(Notifications.Type.INFO, "Mã không hợp lệ!");
                return;
            }

            if ((selectedCriteria.equals("Mã vật tư")) && !keyword.matches("\\w+")) {
                Notifications.getInstance().show(Notifications.Type.INFO, "Mã không hợp lệ!");
                return;
            }

            DefaultTableModel model = (DefaultTableModel) tbl_TonKho.getModel();
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
            tbl_TonKho.setRowSorter(sorter);

            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(keyword), columnIndex));
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedForm1 = new tabbed.TabbedForm();
        jLabel1 = new javax.swing.JLabel();
        btn_timKiem = new javax.swing.JButton();
        txt_timKiem = new javax.swing.JTextField();
        btn_Chuyen = new javax.swing.JButton();
        btn_KiemKe1 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        cbo_TimKiem = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_TonKho = new javax.swing.JTable();

        setPreferredSize(new java.awt.Dimension(946, 642));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Tồn kho");

        btn_timKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawer/icon/search.png"))); // NOI18N

        btn_Chuyen.setText("Chuyển Kho");
        btn_Chuyen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ChuyenActionPerformed(evt);
            }
        });

        btn_KiemKe1.setText("Kiểm kê");
        btn_KiemKe1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_KiemKe1ActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawer/icon/search.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tabbedForm1Layout = new javax.swing.GroupLayout(tabbedForm1);
        tabbedForm1.setLayout(tabbedForm1Layout);
        tabbedForm1Layout.setHorizontalGroup(
            tabbedForm1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabbedForm1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabbedForm1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabbedForm1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(tabbedForm1Layout.createSequentialGroup()
                        .addComponent(btn_timKiem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cbo_TimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 229, Short.MAX_VALUE)
                        .addComponent(btn_Chuyen)
                        .addGap(18, 18, 18)
                        .addComponent(btn_KiemKe1)))
                .addContainerGap())
        );
        tabbedForm1Layout.setVerticalGroup(
            tabbedForm1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabbedForm1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabbedForm1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addGroup(tabbedForm1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(tabbedForm1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_Chuyen)
                        .addComponent(btn_KiemKe1))
                    .addComponent(btn_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(tabbedForm1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbo_TimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        tbl_TonKho.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã kho", "Mã vật tư", "Số lượng", "Đơn vị", "Vị trí "
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
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
        tbl_TonKho.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_TonKhoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_TonKho);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedForm1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbedForm1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 462, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tbl_TonKhoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_TonKhoMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_tbl_TonKhoMouseClicked

    private void btn_ChuyenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ChuyenActionPerformed
        // TODO add your handling code here:
        cicki();
//       phieuluanchuyen pl = new phieuluanchuyen(null, null);

//        pl.setVisible(true);
    }//GEN-LAST:event_btn_ChuyenActionPerformed

    private void btn_KiemKe1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_KiemKe1ActionPerformed
        // TODO add your handling code here:
        kiemke kk = new kiemke();
        kk.setDefaultCloseOperation(kiemke.DISPOSE_ON_CLOSE);
        kk.setVisible(true);

    }//GEN-LAST:event_btn_KiemKe1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        capNhatSoLuongTonKho();
    }//GEN-LAST:event_jButton1ActionPerformed

    @Override
    public boolean formClose() {
        return true;

    }

    @Override
    public void formOpen() {
        System.out.println("Duy Đẹp Trai");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Chuyen;
    private javax.swing.JButton btn_KiemKe1;
    private javax.swing.JButton btn_timKiem;
    private javax.swing.JComboBox<String> cbo_TimKiem;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private tabbed.TabbedForm tabbedForm1;
    private javax.swing.JTable tbl_TonKho;
    private javax.swing.JTextField txt_timKiem;
    // End of variables declaration//GEN-END:variables
}
