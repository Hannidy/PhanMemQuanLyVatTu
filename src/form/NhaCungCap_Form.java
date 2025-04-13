package form;

import dao.NhaCungCapDAO;
import entity.model_NhaCungCap;
import entity.model_VatTu;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import tabbed.TabbedForm;
import raven.toast.Notifications;
import util.Message;

public class NhaCungCap_Form extends TabbedForm {

    private DefaultTableModel tbl_ModelNhaCungCap;
    private NhaCungCapDAO nccdao = new NhaCungCapDAO();
    private List<model_NhaCungCap> list_NhaCungCap = new ArrayList<model_NhaCungCap>();

    private String selectedtenNCC = "";
    private String selectedSDT = "";
    private String selectedemail = "";
    private String selecteddiachi = "";

    private static final String LOG_FILE = "nhacungcap_log.txt";
    // Danh sách lưu trữ thông báo
    private List<String> actionLogs = new ArrayList<>();
    // Biến đếm số lượng thông báo
    private int notificationCount = 0;
    private static final long TWENTY_FOUR_HOURS = 24 * 60 * 60 * 1000; // 24 tiếng tính bằng milliseconds

    public NhaCungCap_Form() {
        initComponents();
        initcomboBox();
        addSearchFilter();
        addSearchButtonAction();
        tbl_ModelNhaCungCap = (DefaultTableModel) tbl_nhacungCap.getModel();
        fillToTableNhaCungCap();
        addBellButtonActionNCC();
    }

    public void initcomboBox() {
        cbo_timKiem.addItem("Mã nhà cung cấp");
        cbo_timKiem.addItem("Tên nhà cung cấp");
        cbo_timKiem.addItem("Số điện thoại");
        cbo_timKiem.addItem("Email");
        cbo_timKiem.addItem("Địa chỉ");
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
        int rows = tbl_nhacungCap.getSelectedRow();
        int[] selectedRows = tbl_nhacungCap.getSelectedRows(); // Lấy tất cả các dòng được chọn

        if (selectedRows.length == 0) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Chọn ít nhất một dòng để xóa!");
            return;
        }

        String maNCC = tbl_nhacungCap.getValueAt(rows, 0).toString();
        String tenNCC = tbl_nhacungCap.getValueAt(rows, 1).toString().trim();
        String SDT = tbl_nhacungCap.getValueAt(rows, 2).toString().trim();
        String email = tbl_nhacungCap.getValueAt(rows, 3).toString().trim();
        String diaChi = tbl_nhacungCap.getValueAt(rows, 4).toString().trim();

        boolean confirm = Message.confirm("Bạn có chắc chắn muốn xóa " + selectedRows.length + " nhà cung cấp?");
        if (!confirm) {
            return;
        }

        try {
            List<String> danhSachXoa = new ArrayList<>(); // Lưu các vật tư bị xóa để ghi vào thông báo

            for (int i = selectedRows.length - 1; i >= 0; i--) { // Xóa từ dưới lên để tránh lỗi chỉ số
                int row = selectedRows[i];
                //String maNCCtodelete = tbl_nhacungCap.getValueAt(row, 0).toString();
                nccdao.delete(maNCC); // Xóa từng vật tư
                danhSachXoa.add(maNCC); // Thêm vào danh sách để ghi nhận thông báo
            }

            fillToTableNhaCungCap(); // Cập nhật lại bảng sau khi xóa
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Đã xóa " + selectedRows.length + " nhà cung cấp!");

            String log = String.format("Xóa|%s|%s|%s|%s|%s|%s",
                    maNCC, tenNCC, SDT, email, diaChi,
                    new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(new Date()));
            writeLogToFile(log);
            notificationCount++; // Tăng số thông báo
            updateBellIcon(); // Cập nhật giao diện chuông

        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Không thể xóa nhà cung cấp!");
        }
    }

    public void updateNhaCungCap() {
        int row = tbl_nhacungCap.getSelectedRow();
        if (row < 0) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Chọn một dòng để cập nhật!");
            return;
        }

        // Lấy dữ liệu từ JTable chỉ với 3 cột
        String maNCC = tbl_nhacungCap.getValueAt(row, 0).toString();
        String tenNCC = tbl_nhacungCap.getValueAt(row, 1).toString().trim();
        String SDT = tbl_nhacungCap.getValueAt(row, 2).toString().trim();
        String email = tbl_nhacungCap.getValueAt(row, 3).toString().trim();
        String diaChi = tbl_nhacungCap.getValueAt(row, 4).toString().trim();

        // Kiểm tra nếu có ô nào bị bỏ trống
        if (tenNCC.isEmpty() || maNCC.isEmpty() || SDT.isEmpty() || email.isEmpty() || diaChi.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        // Tạo đối tượng Vật Tư mới
        model_NhaCungCap ncc = new model_NhaCungCap();
        ncc.setManhacungCap(maNCC);
        ncc.setTennhacungCap(tenNCC);
        ncc.setSodienThoai(SDT);
        ncc.setEmail(email);
        ncc.setDiaChi(diaChi);

        // Xác nhận cập nhật
        boolean confirm = Message.confirm("Bạn có chắc chắn muốn cập nhật nhà cung cấp có mã '" + maNCC + "'?");
        if (confirm) {
            try {
                nccdao.update(ncc); // Cập nhật vào CSDL
                fillToTableNhaCungCap(); // Cập nhật lại bảng để hiển thị dữ liệu mới
                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Cập nhật nhà cung cấp thành công!");

                // 🔔 Ghi log vào file
                String log = String.format("Cập nhật|%s|%s|%s|%s|%s|%s",
                        maNCC, tenNCC, SDT, email, diaChi,
                        new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(new Date()));
                writeLogToFile(log);
                notificationCount++; // Tăng số thông báo
                updateBellIcon(); // Cập nhật giao diện chuông

            } catch (Exception e) {
                Message.error("Lỗi: " + e.getMessage());
                Notifications.getInstance().show(Notifications.Type.INFO, "Cập nhật nhà cung cấp thất bại!");
            }
        }
    }

    private void addBellButtonActionNCC() {
        for (ActionListener al : btn_bell.getActionListeners()) {
            btn_bell.removeActionListener(al);
        }

        btn_bell.addActionListener(e -> {
            JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(btn_bell), "Lịch sử hành động");
            dialog.setSize(600, 400);
            dialog.setLocationRelativeTo(null);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            String[] columns = {"Hành động", "Mã nhà cung cấp", "Tên nhà cung cấp", "Số điện thoại", "Email", "Địa chỉ", "Thời gian"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            JTable logTable = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(logTable);
            dialog.add(scrollPane);

            List<String> logs = readLogsFromFile(); // Đọc và tự động xóa log cũ
            if (logs.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.INFO, "Chưa có hành động nào!");
                dialog.dispose();
                return;
            }

            for (String log : logs) {
                String[] parts = log.split("\\|");
                if (parts.length == 7) {
                    model.addRow(new Object[]{
                        parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]
                    });
                }
            }

            // Cập nhật notificationCount dựa trên số dòng log còn lại
            notificationCount = logs.size();
            updateBellIcon();

            dialog.setVisible(true);
        });
    }

    private List<String> readLogsFromFile() {
        List<String> logs = new ArrayList<>();
        List<String> logsToKeep = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        Date currentTime = new Date();

        // Đọc file log
        try (BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    logs.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            // File chưa tồn tại
        } catch (IOException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi khi đọc log: " + e.getMessage());
        }

        // Lọc các log cũ hơn 24 tiếng
        for (String log : logs) {
            String[] parts = log.split("\\|");
            if (parts.length == 7) {
                try {
                    Date logTime = dateFormat.parse(parts[6]); // Parse thời gian từ log
                    long timeDiff = currentTime.getTime() - logTime.getTime();
                    if (timeDiff <= TWENTY_FOUR_HOURS) {
                        logsToKeep.add(log); // Giữ lại log nếu chưa quá 24 tiếng
                    }
                } catch (Exception e) {
                    // Nếu parse thời gian lỗi, bỏ qua dòng này
                    System.err.println("Lỗi parse thời gian log: " + log);
                }
            }
        }

        // Ghi lại file log với các dòng còn lại
        if (logs.size() != logsToKeep.size()) { // Chỉ ghi nếu có dòng bị xóa
            writeLogsToFile(logsToKeep);
        }

        return logsToKeep;
    }

    private void writeLogsToFile(List<String> logs) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE))) {
            for (String log : logs) {
                writer.write(log);
                writer.newLine();
            }
        } catch (IOException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi khi ghi log: " + e.getMessage());
        }
    }

    private void writeLogToFile(String log) {
        // Ghi log mới và kiểm tra xóa log cũ
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(log);
            writer.newLine();
        } catch (IOException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi khi ghi log: " + e.getMessage());
        }
        // Sau khi ghi log mới, kiểm tra và xóa log cũ
        readLogsFromFile();
    }

    private void updateBellIcon() {
        if (notificationCount > 0) {
            btn_bell.setToolTipText("Có " + notificationCount + " hành động");
        } else {
            btn_bell.setToolTipText("Không có hành động nào");
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
                    case "Mã nhà cung cấp":
                        columnIndex = 0;
                        break;
                    case "Tên nhà cung cấp":
                        columnIndex = 1;
                        break;
                    case "Số điện thoại":
                        columnIndex = 2;
                        break;
                    case "Email":
                        columnIndex = 3;
                        break;
                    case "Địa chỉ":
                        columnIndex = 4;
                        break;
                }

                if (keyword.isEmpty()) {
                    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) tbl_nhacungCap.getModel());
                    tbl_nhacungCap.setRowSorter(sorter);
                    sorter.setRowFilter(null);
                    return;
                }

                // ✅ Lọc tự động (không giới hạn mã phải theo định dạng gì)
                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) tbl_nhacungCap.getModel());
                tbl_nhacungCap.setRowSorter(sorter);
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(keyword), columnIndex));
            }
        });
    }

    public void searchFilter() {
        String keyword = txt_timKiem.getText().trim().toLowerCase();
        DefaultTableModel model = (DefaultTableModel) tbl_nhacungCap.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        tbl_nhacungCap.setRowSorter(sorter);

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

            int columnIndex;
            switch (selectedCriteria) {
                case "Mã nhà cung cấp":
                    columnIndex = 0;
                    break;
                case "Tên nhà cung cấp":
                    columnIndex = 1;
                    break;
                case "Số điện thoại":
                    columnIndex = 2;
                    break;
                case "Email":
                    columnIndex = 3;
                    break;
                case "Địa chỉ":
                    columnIndex = 4;
                    break;
                default:
                    Notifications.getInstance().show(Notifications.Type.INFO, "Tiêu chí tìm kiếm không hợp lệ!");
                    return;
            }

            DefaultTableModel model = (DefaultTableModel) tbl_nhacungCap.getModel();
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
            tbl_nhacungCap.setRowSorter(sorter);

            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(keyword), columnIndex));
        });
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
        cbo_timKiem = new javax.swing.JComboBox<>();
        btn_bell = new javax.swing.JButton();

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

        btn_bell.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawer/image/icon.png"))); // NOI18N

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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_bell))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btn_timKiem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cbo_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 183, Short.MAX_VALUE)
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(btn_bell))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_them)
                        .addComponent(btn_xoa)
                        .addComponent(btn_sua)
                        .addComponent(cbo_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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

    @Override
    public boolean formClose() {
        return true;

    }

    @Override
    public void formOpen() {
        System.out.println("Duy Dep Trai");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_bell;
    private javax.swing.JButton btn_sua;
    private javax.swing.JButton btn_them;
    private javax.swing.JButton btn_timKiem;
    private javax.swing.JButton btn_xoa;
    private javax.swing.JComboBox<String> cbo_timKiem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_nhacungCap;
    private javax.swing.JTextField txt_timKiem;
    // End of variables declaration//GEN-END:variables
}
