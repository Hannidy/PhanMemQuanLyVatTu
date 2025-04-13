
package form;

import dao.ChiTietPhieuNhapDAO;
import dao.DonViTinhDAO;
import dao.PhieuNhapDAO;
import dao.TonKhoDAO;
import entity.model_ChiTietPhieuNhap;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import entity.model_PhieuNhap;
import entity.model_TonKho;
import java.awt.event.MouseAdapter;
import util.JDBCHelper;
import util.Message;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JComboBox;
import tabbed.TabbedForm;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class PhieuNhap_Form extends TabbedForm {

    private DefaultTableModel tbl_ModelPhieuNhap;
    private DefaultTableModel tbl_ModelChiTietPhieuNhap;

    PhieuNhapDAO pndao = new PhieuNhapDAO();
    private ChiTietPhieuNhapDAO chitietDAO = new ChiTietPhieuNhapDAO();

    private Map<String, List<String>> nhomVatTuMap;
    private Map<String, Integer> donViTinhMap;

    private Map<String, String> donViMacDinhTheoNhom = Map.of(
        "Khối Lượng", "Tấn",
        "Chiều Dài", "Mét",
        "Thể Tích", "Lít",
        "Đơn Vị Đóng Gói", "Thùng"
    );

    public PhieuNhap_Form() {
        initComponents();
        setDefaultNgayNhap();
        setCalendarToVietnamese();

        // Khởi tạo bảng
        tbl_ModelPhieuNhap = (DefaultTableModel) tbl_PhieuNhap.getModel();
        tbl_ModelChiTietPhieuNhap = (DefaultTableModel) tbl_ChiTietPhieuNhap.getModel();

        // Load dữ liệu ban đầu
        loadNhomVatTu(); // Gọi luôn loadDonViTinh bên trong
        loadDonViTinh(); // Nếu muốn tách biệt

        fillToTablePhieuNhap();
        fillToTableChiTietPhieuNhap();

        taoMaPhieuNhapMoi();

        // Gắn sự kiện
        addEventHandlers();
        initEventHandlers();
    }

  void setCalendarToVietnamese() {
    Locale.setDefault(new Locale("vi", "VN")); // Đặt ngôn ngữ mặc định là Tiếng Việt
    txt_NgayNhap.setLocale(new Locale("vi", "VN")); // Thiết lập Locale cho JDateChooser
    }
    
    void setDefaultNgayNhap(){
        txt_NgayNhap.setDate(new Date());
//      java.util.Date ngayNhapUtil = txt_NgayNhap.getDate();
//      java.sql.Date ngayNhapSQL = new java.sql.Date(ngayNhapUtil.getTime());
//        Date ngayNhapJava = txt_NgayNhap.getDate();
//        txt_NgayNhap.setDate(ngayNhapJava);
      
    }
    
    // Hiển thị danh sách phiếu nhập lên bảng
  private void fillToTablePhieuNhap() {
    tbl_ModelPhieuNhap.setRowCount(0); // Xóa dữ liệu cũ trong bảng

    try {
        List<model_PhieuNhap> list_PhieuNhap = pndao.selectAll();

        if (list_PhieuNhap == null || list_PhieuNhap.isEmpty()) {
            System.out.println("⚠ Không có phiếu nhập nào trong CSDL.");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        for (model_PhieuNhap pn : list_PhieuNhap) {
            String ngayNhapFormatted = "";
            Date ngayNhap = pn.getNgayNhap();

            if (ngayNhap != null) {
                ngayNhapFormatted = sdf.format(ngayNhap); // Format lại kiểu ngày để hiển thị
            }

            Object[] row = new Object[]{
                pn.getMaPhieuNhap(),
                ngayNhapFormatted,
                pn.getMaKho(),
                pn.getMaNhaCungCap(),
                pn.getTrangThai()
            };

            tbl_ModelPhieuNhap.addRow(row);
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách phiếu nhập!");
    }
}
  
    
    public void clickHerePhieuNhap() {
    int row = tbl_PhieuNhap.getSelectedRow();
    if (row > -1) {
        try {
            String maPhieuNhap = tbl_PhieuNhap.getValueAt(row, 0).toString(); // Cột 0
            String ngayStr = tbl_PhieuNhap.getValueAt(row, 1).toString();     // Cột 1
            String maKho = tbl_PhieuNhap.getValueAt(row, 2).toString();      // Cột 2
            String maNCC = tbl_PhieuNhap.getValueAt(row, 3).toString();      // Cột 3

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Hoặc "dd-MM-yyyy" nếu đúng format DB bạn

            Date ngayNhap = sdf.parse(ngayStr);

            // Gán lại vào form
            txt_MaPhieuNhap.setText(maPhieuNhap);
            txt_MaKho1.setText(maKho);
            txt_MaNhaCungCap.setText(maNCC);
            txt_NgayNhap.setDate(ngayNhap);

        } catch (Exception e) {
            Message.error("Lỗi khi chuyển đổi dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    } else {
        Message.alert("Vui lòng chọn một dòng trong bảng!");
    }
}

    
public String taoMaPhieuNhapMoi() {
    List<model_PhieuNhap> ds = pndao.selectAll();
    int max = 0;
    for (model_PhieuNhap pn : ds) {
        String so = pn.getMaPhieuNhap().replace("PN", ""); // Lấy số
        try {
            max = Math.max(max, Integer.parseInt(so));
        } catch (Exception e) {}
    }
    return "PN" + String.format("%03d", max + 1); // Tạo mã mới: PN001, PN002, ...
}
    // Thêm phiếu nhập mới
private void addPhieuNhap() {
    if (
        txt_MaPhieuNhap.getText().isEmpty() ||
        txt_MaKho1.getText().isEmpty() ||
        txt_MaNhaCungCap.getText().isEmpty() ||
        txt_NgayNhap.getDate() == null
    ) {
        Message.alert("Vui lòng nhập đầy đủ thông tin!");
        return;
    }

    model_PhieuNhap pn = new model_PhieuNhap();
    pn.setMaPhieuNhap(txt_MaPhieuNhap.getText().trim());
    pn.setMaKho(txt_MaKho1.getText().trim());
    pn.setMaNhaCungCap(txt_MaNhaCungCap.getText().trim());
    pn.setNgayNhap(txt_NgayNhap.getDate());
    pn.setTrangThai("Chờ duyệt");

    try {
        // 1. Thêm vào database
        pndao.insert(pn);

        // 2. Cập nhật lại bảng hiển thị
        fillToTablePhieuNhap();

        // 3. Gán mã phiếu nhập vừa tạo sang phần chi tiết
        txt_MaPhieuNhapCTPN.setText(pn.getMaPhieuNhap());

        Message.alert("✅ Đã thêm phiếu nhập và cập nhật sang Chi Tiết Phiếu Nhập!");
    } catch (Exception e) {
        e.printStackTrace();
        Message.error("❌ Lỗi khi thêm phiếu nhập: " + e.getMessage());
    }
}


    // Xóa phiếu nhập
    private void deletePhieuNhap() {
        int row = tbl_PhieuNhap.getSelectedRow();
        if (row < 0) {
            Message.alert("Bạn phải chọn một dòng để xóa!");
            return;
        }

        String maPhieuNhap = tbl_PhieuNhap.getValueAt(row, 0).toString();
        boolean confirm = Message.confirm("Bạn có chắc chắn muốn xóa?");
        if (confirm) {
            try{
                pndao.delete(maPhieuNhap);
                fillToTablePhieuNhap();
                Message.alert("Xóa phiếu nhập thành công!");
            } catch(Exception e) {
                Message.error("Lỗi" + e.getMessage());
                Message.alert("Xóa phiếu nhập thất bại!");
            }
        }
    }

    // Cập nhật phiếu nhập
    private void updatePhieuNhap() {
        int row = tbl_PhieuNhap.getSelectedRow();
        if (row < 0) {
            Message.alert("Bạn phải chọn một dòng để sửa!");
            return;
        }

        if (
            txt_MaPhieuNhap.getText().isEmpty() ||
            txt_MaNhaCungCap.getText().isEmpty()
            ) {
            Message.alert("Vui lòng nhập đầy đủ thông tin!");
            return;
        }
            model_PhieuNhap pn = new model_PhieuNhap();
            pn.setMaPhieuNhap(tbl_PhieuNhap.getValueAt(row, 0).toString());
            pn.setNgayNhap(txt_NgayNhap.getDate());
            pn.setMaKho(txt_MaPhieuNhap.getText().trim());
            pn.setMaNhaCungCap(txt_MaNhaCungCap.getText().trim());

                boolean confirm = Message.confirm("Bạn có chắc chắn muốn cập nhật phiếu nhập có mã '" + pn.getMaPhieuNhap() + "' ?");
                if (confirm) {
                    try{
                    pndao.update(pn);
                    fillToTablePhieuNhap();
                    Message.alert("Cập nhật phiếu nhập thành công!");
                } catch(Exception e) {
                    Message.error("Lỗi: " + e.getMessage());
                    Message.alert("Cập nhật phiếu nhập thất bại!");
                }
                }else {
                    return;
                }
    }

    // Tìm kiếm phiếu nhập
    private void searchPhieuNhap() {
        String maPhieuNhap = txt_TimKiem.getText().trim();
        if (maPhieuNhap.isEmpty()) {
            Message.alert("Vui lòng nhập từ khóa để tìm kiếm!");
            return;
        }

        List<model_PhieuNhap> list_PhieuNhap = (List<model_PhieuNhap>) pndao.selectById(maPhieuNhap);
        tbl_ModelPhieuNhap.setRowCount(0); // Xóa dữ liệu cũ
        if (list_PhieuNhap.isEmpty()) {
            Message.alert("Không tìm thấy vật tư!");
        } else {
            for (model_PhieuNhap pn : list_PhieuNhap) {
                tbl_ModelPhieuNhap.addRow(
                        new Object[]{
                            pn.getMaPhieuNhap(),
                            pn.getNgayNhap(),
                            pn.getMaKho(),
                            pn.getMaNhaCungCap()
                    }
                );
            }
        }
    }
    
    private void refreshForm(){
        fillToTablePhieuNhap();
//        setDefaultNgayNhap();
        txt_MaPhieuNhap.setText("");
        txt_MaNhaCungCap.setText("");
        txt_TimKiem.setText("");
    }
   

    
    
    
   
    
    
    
    private void loadNhomVatTu() {
    nhomVatTuMap = new HashMap<>();

    // Khối lượng
    nhomVatTuMap.put("Khối Lượng", Arrays.asList("Tấn", "Kilogram", "Gram"));
    // Chiều dài
    nhomVatTuMap.put("Chiều Dài", Arrays.asList("Kilomet", "Mét", "Centimet"));
    // Thể tích
    nhomVatTuMap.put("Thể Tích", Arrays.asList("Mét khối", "Lít", "Mililit"));
    // Đóng gói
    nhomVatTuMap.put("Đơn Vị Đóng Gói", Arrays.asList("Kiện", "Thùng", "Hộp", "Cái"));

    cboNhomVatTu.removeAllItems();
    for (String nhom : nhomVatTuMap.keySet()) {
        cboNhomVatTu.addItem(nhom);
    }
}
     // Xử lý sự kiện
     // Sự kiện ComboBox
 private void addEventHandlers() {
    cboNhomVatTu.addActionListener(e -> {
        String selectedGroup = (String) cboNhomVatTu.getSelectedItem();
        if (selectedGroup == null) return;

        loadDonViTinh(); // Đảm bảo luôn có map mới

        List<String> donViList = nhomVatTuMap.get(selectedGroup);

        cboDonViGoc.removeAllItems();
        cboDonViDich.removeAllItems();

        if (donViList != null) {
            for (String donVi : donViList) {
                cboDonViGoc.addItem(donVi);
                cboDonViDich.addItem(donVi);
            }
        }

        loadLoaiVatTuTheoNhom(selectedGroup);
    });

    btnChuyenDoi.addActionListener(e -> chuyenDoiDonVi());
}
    
    // Load danh sách đơn vị tính từ DB
    private void loadDonViTinh() {
    donViTinhMap = new HashMap<>();
    String query = "SELECT MaDonVi, TenDonVi FROM DonViTinh";

    try (ResultSet rs = JDBCHelper.query(query)) {
        while (rs.next()) {
            String tenDonVi = rs.getString("TenDonVi").trim();
            int maDonVi = rs.getInt("MaDonVi");
            donViTinhMap.put(tenDonVi, maDonVi);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Lỗi khi tải đơn vị tính: " + e.getMessage());
    }

    System.out.println("✅ Đơn vị tải lên từ DB: " + donViTinhMap.keySet());
}
    
  private void chuyenDoiDonVi() {
    try {
        if (cboDonViGoc.getSelectedItem() == null || cboDonViDich.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn đầy đủ đơn vị gốc và đơn vị đích.");
            return;
        }

        String donViGoc = cboDonViGoc.getSelectedItem().toString().trim();
        String donViDich = cboDonViDich.getSelectedItem().toString().trim();

        if (!donViTinhMap.containsKey(donViGoc) || !donViTinhMap.containsKey(donViDich)) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy đơn vị trong dữ liệu.");
            return;
        }

        int maDonViGoc = donViTinhMap.get(donViGoc);
        int maDonViDich = donViTinhMap.get(donViDich);
        double soLuong = Double.parseDouble(txtSoLuong.getText().trim());

        // Truy vấn hệ số quy đổi
        double heSo = layHeSoQuyDoi(maDonViGoc, maDonViDich);

        double ketQua = soLuong * heSo;
        txtKetQua.setText(String.valueOf(ketQua));

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Vui lòng nhập đúng định dạng số.");
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Đã xảy ra lỗi: " + e.getMessage());
    }
}

private double layHeSoQuyDoi(int maGoc, int maDich) {
    double heSo = 1.0;
    String sql = "SELECT HeSo FROM QuyDoiDonVi WHERE DonViGoc = ? AND DonViDich = ?";

    try (ResultSet rs = JDBCHelper.query(sql, maGoc, maDich)) {
        if (rs.next()) {
            heSo = rs.getDouble("HeSo");
        } else {
            // Nếu không có chiều gốc → đích, thử tìm chiều ngược và chia
            try (ResultSet rsReverse = JDBCHelper.query(sql, maDich, maGoc)) {
                if (rsReverse.next()) {
                    heSo = 1.0 / rsReverse.getDouble("HeSo");
                } else {
                    JOptionPane.showMessageDialog(null, "Không tìm thấy hệ số quy đổi giữa hai đơn vị.");
                    heSo = 1.0;
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Lỗi truy vấn hệ số quy đổi: " + e.getMessage());
    }

    return heSo;
}

      // Lấy hệ số từ database
     
  private double layHeSoChuyenDoi(int maDonViGoc, int maDonViDich) throws SQLException {
    String query = "SELECT HeSo FROM QuyDoiDonVi WHERE MaDonViGoc = ? AND MaDonViDich = ?";
    try (ResultSet rs = JDBCHelper.query(query, maDonViGoc, maDonViDich)) {
        if (rs.next()) {
            return rs.getDouble("HeSo");
        } else {
            // Thử chiều ngược lại
            ResultSet rs2 = JDBCHelper.query(query, maDonViDich, maDonViGoc);
            if (rs2.next()) {
                return 1.0 / rs2.getDouble("HeSo");
            }
        }
    }
    throw new SQLException("Không tìm thấy hệ số chuyển đổi.");
}

private void loadLoaiVatTuTheoNhom(String nhomVatTu) {
    cboLoaiVatTu.removeAllItems();
    try {
        String sql = "SELECT TenLoaiVatTu FROM LOAIVATTU WHERE NhomVatTu = ?";
        ResultSet rs = JDBCHelper.query(sql, nhomVatTu);
        boolean found = false;
        while (rs.next()) {
            cboLoaiVatTu.addItem(rs.getString("TenLoaiVatTu"));
            found = true;
        }
        if (!found) {
            cboLoaiVatTu.addItem("Không có loại phù hợp");
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}

private void loadDonViTheoLoaiVatTu(String tenLoai, JComboBox<String> cboDonVi) {
    try {
        ResultSet rs = JDBCHelper.query("SELECT TenDonVi FROM DonViTinh WHERE NhomVatTu = ?", tenLoai);
        cboDonVi.removeAllItems();
        while (rs.next()) {
            cboDonVi.addItem(rs.getString("TenDonVi"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
private void chuyenDoiTheoLoaiVatTu(String donViGoc, String donViDich, double soLuong) {
    try {
        int maDVGoc = donViTinhMap.get(donViGoc);
        int maDVDich = donViTinhMap.get(donViDich);
        double heSo = layHeSoChuyenDoi(maDVGoc, maDVDich);
        double ketQua = soLuong * heSo;
        System.out.println("Kết quả chuyển đổi: " + ketQua);
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Không tìm thấy hệ số chuyển đổi!");
    }
}

public void setMaPhieuNhap(String maPhieu) {
    txt_MaPhieuNhap.setText(maPhieu);
}


   private void fillToTableChiTietPhieuNhap() {
    try {
        tbl_ModelChiTietPhieuNhap.setRowCount(0);
        List<model_ChiTietPhieuNhap> list = chitietDAO.selectAll();
        DonViTinhDAO dvtDAO = new DonViTinhDAO();

        for (model_ChiTietPhieuNhap ct : list) {
            String tenDonViGoc = dvtDAO.getTenDonViById(ct.getDonViGoc());
            String tenDonViDich = dvtDAO.getTenDonViById(ct.getDonViChuyenDoi());

            Object[] row = new Object[]{
                ct.getMaPhieuNhap(),       // ✅ Mã phiếu nhập - cột 0
                ct.getMaVatTu(),           // ✅ Mã vật tư - cột 1
                tenDonViGoc,               // Đơn vị gốc - cột 2
                tenDonViDich,              // Đơn vị chuyển đổi - cột 3
                ct.getSoLuongNhap(),       // ✅ Số lượng nhập - cột 4
                ct.getSoLuongQuyDoi()      // ✅ Số lượng quy đổi - cột 5
            };
            tbl_ModelChiTietPhieuNhap.addRow(row);
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "❌ Lỗi khi tải danh sách chi tiết phiếu nhập!");
    }
}

  
    
private void themChiTietPhieuNhap() {
    try {
        // 0. Kiểm tra các combo null
        if (cboNhomVatTu.getSelectedItem() == null ||
            cboDonViGoc.getSelectedItem() == null ||
            cboDonViDich.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "⚠ Vui lòng chọn đầy đủ Nhóm, Đơn vị gốc, Đơn vị đích.");
            return;
        }

        // 1. Lấy dữ liệu đầu vào
        String maPhieuNhap = txt_MaPhieuNhapCTPN.getText().trim();
        String maVatTu = txt_MaVatTu.getText().trim();
        String nhomVatTu = cboNhomVatTu.getSelectedItem().toString();
        String tenDonViGoc = cboDonViGoc.getSelectedItem().toString();
        String tenDonViDich = cboDonViDich.getSelectedItem().toString();

        if (maPhieuNhap.isEmpty() || maVatTu.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠ Mã phiếu nhập hoặc mã vật tư không được để trống!");
            return;
        }

        double soLuongNhap = Double.parseDouble(txt_soluongNhap.getText().trim());
        double soLuongQuyDoi = Double.parseDouble(txtKetQua.getText().trim());

        int maDonViGoc = donViTinhMap.getOrDefault(tenDonViGoc, -1);
        int maDonViDich = donViTinhMap.getOrDefault(tenDonViDich, -1);

        if (maDonViGoc == -1 || maDonViDich == -1) {
            JOptionPane.showMessageDialog(this, "⚠ Không tìm thấy mã đơn vị tương ứng!");
            return;
        }

        // 2. Lưu vào CTPN
       model_ChiTietPhieuNhap ct = new model_ChiTietPhieuNhap();
ct.setMaPhieuNhap(maPhieuNhap);             
ct.setMaVatTu(maVatTu);                     
ct.setDonViGoc(maDonViGoc);                
ct.setDonViChuyenDoi(maDonViDich);         
ct.setSoLuongNhap((float) soLuongNhap);     
ct.setSoLuongQuyDoi((float) soLuongQuyDoi); 

        new ChiTietPhieuNhapDAO().insert(ct);

        // 3. Xử lý tồn kho
        String donViMacDinh = donViMacDinhTheoNhom.getOrDefault(nhomVatTu, tenDonViDich);
        int maDonViMacDinh = donViTinhMap.getOrDefault(donViMacDinh, -1);
        double soLuongCanThem;

        if (!tenDonViDich.equalsIgnoreCase(donViMacDinh)) {
            double heSo = layHeSoQuyDoi(maDonViDich, maDonViMacDinh);
            soLuongCanThem = soLuongQuyDoi * heSo;
        } else {
            soLuongCanThem = soLuongQuyDoi;
        }

        String maKho = layMaKhoTuPhieuNhap(maPhieuNhap);
        if (maKho == null || maKho.isEmpty()) {
            JOptionPane.showMessageDialog(this, "❌ Không tìm thấy mã kho từ phiếu nhập!");
            return;
        }

        TonKhoDAO tonKhoDAO = new TonKhoDAO();
        List<String> dsKhoTon = tonKhoDAO.layTatCaKhoChuaMaVatTu(maVatTu);

        if (!dsKhoTon.contains(maKho)) {
            model_TonKho tk = new model_TonKho();
            tk.setMaKho(maKho);
            tk.setMavatTu(maVatTu);
            tk.setDonVi(donViMacDinh);
            tk.setSoLuong(String.valueOf((int) soLuongCanThem));
            tonKhoDAO.insert(tk);
        } else {
            String sql = "UPDATE TONKHO SET SoLuong = SoLuong + ? WHERE MaKho = ? AND MaVatTu = ?";
            JDBCHelper.update(sql, (int) soLuongCanThem, maKho, maVatTu);
        }

        JOptionPane.showMessageDialog(this, "✅ Thêm thành công và cập nhật tồn kho!");
        fillToTableChiTietPhieuNhap();

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "❌ Vui lòng nhập đúng định dạng số!");
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "❌ Lỗi khi thêm chi tiết: " + e.getMessage());
    }
}



public String layMaKhoTuPhieuNhap(String maPhieuNhap) {
    String sql = "SELECT MaKho FROM PHIEUNHAP WHERE MaPhieuNhap = ?";
    try (ResultSet rs = JDBCHelper.query(sql, maPhieuNhap)) {
        if (rs.next()) {
            return rs.getString("MaKho");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}
private void initEventHandlers() {
    tbl_ChiTietPhieuNhap.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            int selectedRow = tbl_ChiTietPhieuNhap.getSelectedRow();
            if (selectedRow >= 0) {
                fillFieldsFromChiTietTable(selectedRow);
            }
        }
    });
}
private void fillFieldsFromChiTietTable(int row) {
    try {
        for (int i = 0; i <= 5; i++) {
            Object value = tbl_ChiTietPhieuNhap.getValueAt(row, i);
            if (value == null || value.toString().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠ Dòng này thiếu dữ liệu, không thể xử lý!");
                return;
            }
        }

        String maPhieuNhap = tbl_ChiTietPhieuNhap.getValueAt(row, 0).toString();
        String maVatTu = tbl_ChiTietPhieuNhap.getValueAt(row, 1).toString();
        String tenDonViGoc = tbl_ChiTietPhieuNhap.getValueAt(row, 2).toString();
        String tenDonViDich = tbl_ChiTietPhieuNhap.getValueAt(row, 3).toString();
        String soLuong = tbl_ChiTietPhieuNhap.getValueAt(row, 4).toString();
        String soLuongQuyDoi = tbl_ChiTietPhieuNhap.getValueAt(row, 5).toString();

        // Set text field
        txt_MaPhieuNhapCTPN.setText(maPhieuNhap);
        txt_MaVatTu.setText(maVatTu);
        txt_soluongNhap.setText(soLuong);
        txtKetQua.setText(soLuongQuyDoi);
        cboDonViGoc.setSelectedItem(tenDonViGoc);
        cboDonViDich.setSelectedItem(tenDonViDich);

        // Lấy thông tin loại và nhóm
        try (
            ResultSet rsLoai = JDBCHelper.query("""
                SELECT lvt.TenLoaiVatTu 
                FROM VATTU vt 
                JOIN LOAIVATTU lvt ON vt.MaLoaiVatTu = lvt.MaLoaiVatTu 
                WHERE vt.MaVatTu = ?
            """, maVatTu);
            
            ResultSet rsNhom = JDBCHelper.query(
                "SELECT NhomVatTu FROM DonViTinh WHERE TenDonVi = ?", tenDonViGoc
            )
        ) {
            if (rsNhom.next()) {
                String nhomVatTu = rsNhom.getString("NhomVatTu");
                cboNhomVatTu.setSelectedItem(nhomVatTu);
                loadLoaiVatTuTheoNhom(nhomVatTu); // Load loại tương ứng
            }

            if (rsLoai.next()) {
                String loaiVatTu = rsLoai.getString("TenLoaiVatTu");
                cboLoaiVatTu.setSelectedItem(loaiVatTu);
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "❌ Lỗi khi load chi tiết dòng: " + e.getMessage());
    }
}







private String tenDonViFromMa(int maDonVi) {
    for (Map.Entry<String, Integer> entry : donViTinhMap.entrySet()) {
        if (entry.getValue() == maDonVi) {
            return entry.getKey();
        }
    }
    return "Không rõ"; // Nếu không tìm thấy
}
private String getNhomVatTuByTenDonVi(String tenDonVi) {
    String sql = "SELECT NhomVatTu FROM DonViTinh WHERE TenDonVi = ?";
    try (ResultSet rs = JDBCHelper.query(sql, tenDonVi)) {
        if (rs.next()) {
            return rs.getString("NhomVatTu");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}
// Map nhóm vật tư => đơn vị mặc định





    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tab1_PhieuNhap = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        txt_TimKiem = new javax.swing.JTextField();
        btn_TimKiem = new javax.swing.JButton();
        btn_Them = new javax.swing.JButton();
        btn_Xoa = new javax.swing.JButton();
        btn_Sua = new javax.swing.JButton();
        txt_NgayNhap = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txt_MaPhieuNhap = new javax.swing.JTextField();
        txt_MaNhaCungCap = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_PhieuNhap = new javax.swing.JTable();
        btn_LamMoi = new javax.swing.JButton();
        btn_XemCT2 = new javax.swing.JButton();
        btn_XuatExcel2 = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        txt_MaKho1 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        txt_MaPhieuNhapCTPN = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txt_MaVatTu = new javax.swing.JTextField();
        txt_soluongNhap = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_ChiTietPhieuNhap = new javax.swing.JTable();
        txt_TimKiemCTPN = new javax.swing.JTextField();
        btn_ThemCTPX = new javax.swing.JButton();
        btn_XoaCTPX = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        btn_SuaCTPX = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        btn_Lammoi1 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        btn_XemCT1 = new javax.swing.JButton();
        btn_TimCTPN = new javax.swing.JButton();
        btn_XuatExcel1 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        cboNhomVatTu = new javax.swing.JComboBox<>();
        cboLoaiVatTu = new javax.swing.JComboBox<>();
        cboDonViGoc = new javax.swing.JComboBox<>();
        cboDonViDich = new javax.swing.JComboBox<>();
        txtSoLuong = new javax.swing.JTextField();
        txtKetQua = new javax.swing.JTextField();
        btnChuyenDoi = new javax.swing.JButton();

        tab1_PhieuNhap.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        btn_TimKiem.setText("Tìm Kiếm");
        btn_TimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_TimKiemActionPerformed(evt);
            }
        });

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

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 34)); // NOI18N
        jLabel2.setText("Phiếu Nhập");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Ngày Nhập :");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Mã Kho :");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Mã Nhà Cung Cấp :");

        tbl_PhieuNhap.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã Phiếu Nhập", "Ngày Nhập", "Mã Kho", "Mã Nhà Cung Cấp"
            }
        ));
        tbl_PhieuNhap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_PhieuNhapMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_PhieuNhap);

        btn_LamMoi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btn_LamMoi.setText("Làm Mới");
        btn_LamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_LamMoiActionPerformed(evt);
            }
        });

        btn_XemCT2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btn_XemCT2.setText("Xem Chi Tiết");
        btn_XemCT2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_XemCT2ActionPerformed(evt);
            }
        });

        btn_XuatExcel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btn_XuatExcel2.setText("Xuất Excel");
        btn_XuatExcel2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_XuatExcel2ActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Mã Phiếu Nhập:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_Sua, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGap(0, 3, Short.MAX_VALUE)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(btn_Them, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(48, 48, 48))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_NgayNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_Xoa, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(txt_MaNhaCungCap, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txt_MaKho1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_MaPhieuNhap, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 869, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btn_TimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txt_TimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_LamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_XemCT2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_XuatExcel2, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_TimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_TimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_NgayNhap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_MaPhieuNhap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txt_MaKho1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_MaNhaCungCap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_Them, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_Xoa, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_Sua, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_XuatExcel2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_XemCT2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_LamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(68, 68, 68))))
        );

        tab1_PhieuNhap.addTab("Phiếu Nhập", jPanel1);

        jPanel2.setPreferredSize(new java.awt.Dimension(988, 700));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Số Lượng :");

        tbl_ChiTietPhieuNhap.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã Phiếu Nhập", "Mã Vật Tư", "Đơn vị gốc", "Đơn vị chuyển đổi", "Số lượng nhập", "Số lượng quy đổi"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.Float.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tbl_ChiTietPhieuNhap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_ChiTietPhieuNhapMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbl_ChiTietPhieuNhap);

        btn_ThemCTPX.setText("Thêm");
        btn_ThemCTPX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ThemCTPXActionPerformed(evt);
            }
        });

        btn_XoaCTPX.setText("Xóa");
        btn_XoaCTPX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_XoaCTPXActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel6.setText("Phiếu Nhập Chi Tiết");

        btn_SuaCTPX.setText("Sửa");
        btn_SuaCTPX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SuaCTPXActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Mã Phiếu Nhập :");

        btn_Lammoi1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btn_Lammoi1.setText("Làm Mới");
        btn_Lammoi1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Lammoi1ActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Mã Vật Tư :");

        btn_XemCT1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btn_XemCT1.setText("Xem Chi Tiết");
        btn_XemCT1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_XemCT1ActionPerformed(evt);
            }
        });

        btn_TimCTPN.setText("Tìm Kiếm");
        btn_TimCTPN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_TimCTPNActionPerformed(evt);
            }
        });

        btn_XuatExcel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btn_XuatExcel1.setText("Xuất Excel");
        btn_XuatExcel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_XuatExcel1ActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setText("Chuyển đổi đơn vị");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("Nhóm vật tư:\n");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("Loại vật tư:");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText("Đơn vị gốc:");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("Đơn vị đích:");

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel22.setText("Số lượng chuyển đổi:");

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel23.setText("Kết quả:");

        cboNhomVatTu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));

        txtKetQua.setEditable(false);

        btnChuyenDoi.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnChuyenDoi.setText("Chuyển đổi");
        btnChuyenDoi.setToolTipText("");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addGap(170, 170, 170))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(txt_MaVatTu))
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(txt_MaPhieuNhapCTPN, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                    .addComponent(btn_ThemCTPX, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(46, 46, 46))
                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                    .addGap(36, 36, 36)))
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                    .addComponent(btn_XoaCTPX, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(btn_SuaCTPX, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                                                .addComponent(btnChuyenDoi)
                                                .addComponent(txtSoLuong, javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(txtKetQua)
                                                .addComponent(cboDonViDich, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(cboDonViGoc, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(cboLoaiVatTu, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(cboNhomVatTu, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(txt_soluongNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)))))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btn_TimCTPN, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txt_TimKiemCTPN, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 854, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_Lammoi1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_XemCT1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_XuatExcel1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_TimKiemCTPN, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_TimCTPN, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(txt_MaPhieuNhapCTPN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(txt_MaVatTu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel9)
                                    .addComponent(txt_soluongNhap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel13)
                                .addGap(12, 12, 12)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel18)
                                    .addComponent(cboNhomVatTu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel19)
                                    .addComponent(cboLoaiVatTu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(52, 52, 52))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cboDonViGoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel20)))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(cboDonViDich, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23)
                            .addComponent(txtKetQua, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(btnChuyenDoi, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Lammoi1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_XemCT1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_XuatExcel1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_ThemCTPX, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_XoaCTPX, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_SuaCTPX, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        tab1_PhieuNhap.addTab("Chi Tiết Phiếu Nhập", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tab1_PhieuNhap)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tab1_PhieuNhap, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 683, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tbl_ChiTietPhieuNhapMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_ChiTietPhieuNhapMouseClicked
        //clickHerePNCT();
    }//GEN-LAST:event_tbl_ChiTietPhieuNhapMouseClicked

    private void btn_ThemCTPXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ThemCTPXActionPerformed
try {
    String maPhieu = txt_MaPhieuNhapCTPN.getText().trim();
    String maVatTu = txt_MaVatTu.getText().trim();
    String donViGoc = cboDonViGoc.getSelectedItem().toString();
    String donViDich = cboDonViDich.getSelectedItem().toString();
    float soLuongNhap = Float.parseFloat(txt_soluongNhap.getText().trim());
    float soLuongQuyDoi = Float.parseFloat(txtKetQua.getText().trim());

    DefaultTableModel model = (DefaultTableModel) tbl_ChiTietPhieuNhap.getModel();
    model.addRow(new Object[] {
        maPhieu,             // Cột 0
        maVatTu,             // Cột 1
        donViGoc,            // Cột 2
        donViDich,           // Cột 3
        soLuongNhap,         // Cột 4
        soLuongQuyDoi        // Cột 5
    });

} catch (Exception e) {
    e.printStackTrace();
    JOptionPane.showMessageDialog(this, "❌ Lỗi khi thêm dòng vào bảng: " + e.getMessage());
}         
    }//GEN-LAST:event_btn_ThemCTPXActionPerformed

    private void btn_XoaCTPXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_XoaCTPXActionPerformed
        //deleteCTPhieuNhap();
    }//GEN-LAST:event_btn_XoaCTPXActionPerformed

    private void btn_SuaCTPXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SuaCTPXActionPerformed
        //updateCTPhieuNhap();
    }//GEN-LAST:event_btn_SuaCTPXActionPerformed

    private void btn_Lammoi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Lammoi1ActionPerformed
        //        fillToTableCTPN();
        //        fillToTablePN();
        //        txt_NgayNhap.setDate(null);
        //        txt_MaKho.setText(null);
        //        txt_MaNhaCungCap.setText(null);
        //        txt_MaPhieuNhapCTPN.setText(null);
        //        txt_MaPhieuNhap.setText(null);
        //        txt_soluong.setText(null);
        //        txt_TimKiemCTPN.setText(null);
        //        txt_TimKiemPN.setText(null);
    }//GEN-LAST:event_btn_Lammoi1ActionPerformed

    private void btn_XemCT1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_XemCT1ActionPerformed
        //viewChiTietPhieuNhapInPN();
    }//GEN-LAST:event_btn_XemCT1ActionPerformed

    private void btn_TimCTPNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_TimCTPNActionPerformed
        //        try {
            //            // TODO add your handling code here:
            //            searchChiTietPhieuNhap();
            //        } catch (ParseException ex) {
            //            Logger.getLogger(pnPhieuNhap.class.getName()).log(Level.SEVERE, null, ex);
            //        }
    }//GEN-LAST:event_btn_TimCTPNActionPerformed

    private void btn_XuatExcel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_XuatExcel1ActionPerformed
        //exportToExcel();
    }//GEN-LAST:event_btn_XuatExcel1ActionPerformed

    private void btn_TimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_TimKiemActionPerformed
        searchPhieuNhap();
    }//GEN-LAST:event_btn_TimKiemActionPerformed

    private void btn_SuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SuaActionPerformed
        updatePhieuNhap();
    }//GEN-LAST:event_btn_SuaActionPerformed

    private void tbl_PhieuNhapMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_PhieuNhapMouseClicked
        clickHerePhieuNhap();
    }//GEN-LAST:event_tbl_PhieuNhapMouseClicked

    private void btn_LamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_LamMoiActionPerformed
        refreshForm();
    }//GEN-LAST:event_btn_LamMoiActionPerformed

    private void btn_XemCT2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_XemCT2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_XemCT2ActionPerformed

    private void btn_XuatExcel2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_XuatExcel2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_XuatExcel2ActionPerformed

    private void btn_XoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_XoaActionPerformed
        deletePhieuNhap();
    }//GEN-LAST:event_btn_XoaActionPerformed

    private void btn_ThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ThemActionPerformed
        addPhieuNhap();
    }//GEN-LAST:event_btn_ThemActionPerformed

    @Override
    public boolean formClose() {
        return true;
        
    }

    @Override
    public void formOpen() {
        System.out.println("Duy Dep Trai");
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChuyenDoi;
    private javax.swing.JButton btn_LamMoi;
    private javax.swing.JButton btn_Lammoi1;
    private javax.swing.JButton btn_Sua;
    private javax.swing.JButton btn_SuaCTPX;
    private javax.swing.JButton btn_Them;
    private javax.swing.JButton btn_ThemCTPX;
    private javax.swing.JButton btn_TimCTPN;
    private javax.swing.JButton btn_TimKiem;
    private javax.swing.JButton btn_XemCT1;
    private javax.swing.JButton btn_XemCT2;
    private javax.swing.JButton btn_Xoa;
    private javax.swing.JButton btn_XoaCTPX;
    private javax.swing.JButton btn_XuatExcel1;
    private javax.swing.JButton btn_XuatExcel2;
    private javax.swing.JComboBox<String> cboDonViDich;
    private javax.swing.JComboBox<String> cboDonViGoc;
    private javax.swing.JComboBox<String> cboLoaiVatTu;
    private javax.swing.JComboBox<String> cboNhomVatTu;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane tab1_PhieuNhap;
    private javax.swing.JTable tbl_ChiTietPhieuNhap;
    private javax.swing.JTable tbl_PhieuNhap;
    private javax.swing.JTextField txtKetQua;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JTextField txt_MaKho1;
    private javax.swing.JTextField txt_MaNhaCungCap;
    private javax.swing.JTextField txt_MaPhieuNhap;
    private javax.swing.JTextField txt_MaPhieuNhapCTPN;
    private javax.swing.JTextField txt_MaVatTu;
    private com.toedter.calendar.JDateChooser txt_NgayNhap;
    private javax.swing.JTextField txt_TimKiem;
    private javax.swing.JTextField txt_TimKiemCTPN;
    private javax.swing.JTextField txt_soluongNhap;
    // End of variables declaration//GEN-END:variables
}
