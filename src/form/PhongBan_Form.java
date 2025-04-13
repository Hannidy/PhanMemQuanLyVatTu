package form;

import dao.PhongBanDAO;
import entity.model_PhongBan;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

public class PhongBan_Form extends TabbedForm {

    public DefaultTableModel tbl_ModelPhongBan;
    public PhongBanDAO pbdao = new PhongBanDAO();
    public List<model_PhongBan> list_PB = new ArrayList<model_PhongBan>();

    private String selectedTenPhongBan = "";
    private String selectedDiaChi = "";
    private String selectedMatruongPhong = "";

    private static final String LOG_FILE = "phongban_log.txt";
    // Danh sách lưu trữ thông báo
    private List<String> actionLogs = new ArrayList<>();
    // Biến đếm số lượng thông báo
    private int notificationCount = 0;
    private static final long TWENTY_FOUR_HOURS = 24 * 60 * 60 * 1000; // 24 tiếng tính bằng milliseconds

    public PhongBan_Form() {
        initComponents();
        initcomboBox();
        tbl_ModelPhongBan = (DefaultTableModel) tbl_phongBan.getModel();
        fillToTablePhongBan();
        addBellButtonAction();
        searchFilter();
        addSearchFilter();
        addSearchButtonAction();
    }

    public void initcomboBox() {
        cbo_timKiem.addItem("Mã phòng ban");
        cbo_timKiem.addItem("Tên phòng ban");
        cbo_timKiem.addItem("Địa chỉ");
        cbo_timKiem.addItem("Mã trưởng phòng");
    }

    public void fillToTablePhongBan() {
        try {
            // Xóa toàn bộ dữ liệu cũ trước khi thêm mới
            tbl_ModelPhongBan.setRowCount(0);

            // Lấy danh sách vật tư từ database
            list_PB = pbdao.selectAll();
            if (list_PB != null) {
                for (model_PhongBan pb : list_PB) {
                    tbl_ModelPhongBan.addRow(new Object[]{
                        pb.getMaphongBan(), // Chỉ lấy Mã Vật Tư
                        pb.getTenphongBan(), // Chỉ lấy Tên Vật Tư
                        pb.getDiaChi(), // Chỉ lấy Mã Loại Vật Tư
                        pb.getMatruongPhong()
                    });
                }
            }
        } catch (Exception e) { // In lỗi để dễ debug
            // In lỗi để dễ debug
            JOptionPane.showMessageDialog(null, "Lỗi truy vấn dữ liệu: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deletePhongBan() {
        int rows = tbl_phongBan.getSelectedRow();
        int[] selectedRows = tbl_phongBan.getSelectedRows(); // Lấy tất cả các dòng được chọn

        if (selectedRows.length == 0) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Chọn ít nhất một dòng để xóa!");
            return;
        }

        // Lấy dữ liệu từ JTable với 4 cột
        String maPhongBan = tbl_phongBan.getValueAt(rows, 0).toString();
        String tenPhongBan = tbl_phongBan.getValueAt(rows, 1).toString().trim();
        String diaChi = tbl_phongBan.getValueAt(rows, 2).toString().trim();
        String matruongPhong = tbl_phongBan.getValueAt(rows, 3).toString().trim();

        boolean confirm = Message.confirm("Bạn có chắc chắn muốn xóa " + selectedRows.length + " phòng ban?");
        if (!confirm) {
            return;
        }

        try {
            List<String> danhSachXoa = new ArrayList<>(); // Lưu các vật tư bị xóa để ghi vào thông báo

            for (int i = selectedRows.length - 1; i >= 0; i--) { // Xóa từ dưới lên để tránh lỗi chỉ số
                int row = selectedRows[i];
                pbdao.delete(maPhongBan); // Xóa từng vật tư
                danhSachXoa.add(maPhongBan); // Thêm vào danh sách để ghi nhận thông báo
            }

            fillToTablePhongBan(); // Cập nhật lại bảng sau khi xóa
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Đã xóa " + selectedRows.length + " phòng ban!");

            String log = String.format("Xóa|%s|%s|%s|%s|%s",
                    maPhongBan, tenPhongBan, diaChi, matruongPhong,
                    new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(new Date()));
            writeLogToFile(log);
            notificationCount++; // Tăng số thông báo
            updateBellIcon(); // Cập nhật giao diện chuông

        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Không thể xóa phòng ban!");
        }
    }

    public void updatePhongBan() {
        int row = tbl_phongBan.getSelectedRow();
        if (row < 0) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Chọn một dòng để cập nhật!");
            return;
        }

        // Lấy dữ liệu từ JTable với 4 cột
        String maPhongBan = tbl_phongBan.getValueAt(row, 0).toString();
        String tenPhongBan = tbl_phongBan.getValueAt(row, 1).toString().trim();
        String diaChi = tbl_phongBan.getValueAt(row, 2).toString().trim();
        String matruongPhong = tbl_phongBan.getValueAt(row, 3).toString().trim();

        // Kiểm tra nếu có ô nào bị bỏ trống
        if (tenPhongBan.isEmpty() || diaChi.isEmpty() || matruongPhong.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        // Tạo đối tượng Phòng Ban mới
        model_PhongBan pb = new model_PhongBan();
        pb.setMaphongBan(maPhongBan);
        pb.setTenphongBan(tenPhongBan);
        pb.setDiaChi(diaChi);
        pb.setMatruongPhong(matruongPhong);

        // Xác nhận cập nhật
        boolean confirm = Message.confirm("Bạn có chắc chắn muốn cập nhật phòng ban có mã '" + maPhongBan + "'?");
        if (confirm) {
            try {
                pbdao.update(pb); // Cập nhật vào CSDL
                fillToTablePhongBan(); // Cập nhật lại bảng để hiển thị dữ liệu mới
                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Cập nhật phòng ban thành công!");

                // Ghi log (đồng bộ với updateVatTu)
                String log = String.format("Cập nhật|%s|%s|%s|%s|%s",
                        maPhongBan, tenPhongBan, diaChi, matruongPhong, // Dùng matruongPhong thay cho maLoaiVT
                        new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(new Date()));
                writeLogToFile(log);
                notificationCount++; // Tăng số thông báo
                updateBellIcon(); // Cập nhật giao diện chuông

            } catch (Exception e) {
                Message.error("Lỗi: " + e.getMessage());
                Notifications.getInstance().show(Notifications.Type.INFO, "Cập nhật phòng ban thất bại!");

                // Ghi log khi thất bại (thêm để đồng bộ với các hàm khác)
                String log = String.format("Cập nhật thất bại|%s|%s|%s|%s|%s",
                        maPhongBan, tenPhongBan, diaChi, matruongPhong,
                        new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(new Date()));
                writeLogToFile(log);
                notificationCount++; // Tăng số thông báo
                updateBellIcon(); // Cập nhật giao diện chuông
            }
        }
    }

    private void addBellButtonAction() {
        for (ActionListener al : btn_bell.getActionListeners()) {
            btn_bell.removeActionListener(al);
        }

        btn_bell.addActionListener(e -> {
            JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(btn_bell), "Lịch sử hành động");
            dialog.setSize(600, 400);
            dialog.setLocationRelativeTo(null);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            String[] columns = {"Hành động", "Mã phòng ban", "Tên phòng ban", "Địa chỉ", "Mã trưởng phòng", "Thời gian"};
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
                if (parts.length == 6) {
                    model.addRow(new Object[]{
                        parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]
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
            if (parts.length == 6) {
                try {
                    Date logTime = dateFormat.parse(parts[5]); // Parse thời gian từ log
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
                    case "Mã phòng ban":
                        columnIndex = 0;
                        break;
                    case "Tên phòng ban":
                        columnIndex = 1;
                        break;
                    case "Địa chỉ":
                        columnIndex = 2;
                        break;
                    case "Mã trưởng phòng":
                        columnIndex = 3;
                        break;
                }

                if (keyword.isEmpty()) {
                    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) tbl_phongBan.getModel());
                    tbl_phongBan.setRowSorter(sorter);
                    sorter.setRowFilter(null);
                    return;
                }

                // ✅ Lọc tự động (không giới hạn mã phải theo định dạng gì)
                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) tbl_phongBan.getModel());
                tbl_phongBan.setRowSorter(sorter);
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(keyword), columnIndex));
            }
        });
    }

    public void searchFilter() {
        String keyword = txt_timKiem.getText().trim().toLowerCase();
        DefaultTableModel model = (DefaultTableModel) tbl_phongBan.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        tbl_phongBan.setRowSorter(sorter);

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
                case "Mã phòng ban":
                    columnIndex = 0;
                    break;
                case "Tên phòng ban":
                    columnIndex = 1;
                    break;
                case "Địa chỉ":
                    columnIndex = 2;
                    break;
                case "Mã trưởng phòng":
                    columnIndex = 3;
                    break;
            }

            if (columnIndex == -1) {
                Notifications.getInstance().show(Notifications.Type.INFO, "Tiêu chí tìm kiếm không hợp lệ!");
                return;
            }

            DefaultTableModel model = (DefaultTableModel) tbl_phongBan.getModel();
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
            tbl_phongBan.setRowSorter(sorter);

            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(keyword), columnIndex));
        });
    }

    public Set<String> getDanhSachMaPhongBan() {
        Set<String> dsMaLoai = new HashSet<>();
        for (int i = 0; i < tbl_phongBan.getRowCount(); i++) {
            String maLoai = tbl_phongBan.getValueAt(i, 0).toString();
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
        tbl_phongBan = new javax.swing.JTable();
        btn_xoa = new javax.swing.JButton();
        btn_sua = new javax.swing.JButton();
        btn_them = new javax.swing.JButton();
        cbo_timKiem = new javax.swing.JComboBox<>();
        btn_bell = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Phòng Ban");

        btn_timKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawer/icon/search.png"))); // NOI18N

        tbl_phongBan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã Phòng Ban", "Tên Phòng Ban", "Địa Chỉ ", "Mã Trưởng Phòng"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_phongBan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_phongBanMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_phongBan);

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

        btn_bell.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawer/image/icon.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 934, Short.MAX_VALUE)
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                        .addComponent(btn_xoa)
                        .addComponent(btn_sua)
                        .addComponent(btn_them)
                        .addComponent(cbo_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tbl_phongBanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_phongBanMouseClicked
        // TODO add your handling code here:
        int selectedRow = tbl_phongBan.getSelectedRow(); // Lấy dòng đang chọn

        if (selectedRow != -1) { // Kiểm tra có dòng được chọn không
            String tenPhongBan = tbl_phongBan.getValueAt(selectedRow, 1).toString();
            String DiaChi = tbl_phongBan.getValueAt(selectedRow, 2).toString();
            String maTP = tbl_phongBan.getValueAt(selectedRow, 3).toString();

            // Lưu vào biến toàn cục để truyền vào JDialo
            selectedTenPhongBan = tenPhongBan;
            selectedDiaChi = DiaChi;
            selectedMatruongPhong = maTP;
        }
    }//GEN-LAST:event_tbl_phongBanMouseClicked

    private void btn_themActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_themActionPerformed
        // TODO add your handling code here:
        DiaLog_PhongBan dialog = new DiaLog_PhongBan(null, true, this);
        dialog.setData(selectedTenPhongBan, selectedDiaChi, selectedMatruongPhong);
        dialog.setVisible(true);
    }//GEN-LAST:event_btn_themActionPerformed

    private void btn_xoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_xoaActionPerformed
        // TODO add your handling code here:
        deletePhongBan();
    }//GEN-LAST:event_btn_xoaActionPerformed

    private void btn_suaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_suaActionPerformed
        // TODO add your handling code here:
        updatePhongBan();
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
    private javax.swing.JTable tbl_phongBan;
    private javax.swing.JTextField txt_timKiem;
    // End of variables declaration//GEN-END:variables
}
