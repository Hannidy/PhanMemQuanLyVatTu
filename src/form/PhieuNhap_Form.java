
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
import java.util.regex.Pattern;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;


public class PhieuNhap_Form extends TabbedForm {

    private DefaultTableModel tbl_ModelPhieuNhap;
    private DefaultTableModel tbl_ModelChiTietPhieuNhap;
    

    PhieuNhapDAO pndao = new PhieuNhapDAO();
    private ChiTietPhieuNhapDAO chitietDAO = new ChiTietPhieuNhapDAO();
    private TonKho_Form tonKhoForm;  // Đối tượng form Tồn Kho

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
       this.tonKhoForm = tonKhoForm;
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

            // Cập nhật giá trị cho cột trạng thái
            Object[] row = new Object[]{
                pn.getMaPhieuNhap(),          // Mã phiếu nhập
                ngayNhapFormatted,            // Ngày nhập
                pn.getMaKho(),                // Mã kho
                pn.getMaNhaCungCap(),         // Mã nhà cung cấp
                pn.getTrangThai()             // Trạng thái
            };

            tbl_ModelPhieuNhap.addRow(row);  // Thêm dòng vào bảng
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách phiếu nhập!");
    }
}

  
    
    // Xử lý khi click vào phiếu nhập trong bảng
 
public void clickHerePhieuNhap() {
    int row = tbl_PhieuNhap.getSelectedRow();
    if (row > -1) {
        try {
            String maPhieuNhap = tbl_PhieuNhap.getValueAt(row, 0).toString(); // Cột 0 (Mã Phiếu Nhập)
            String ngayStr = tbl_PhieuNhap.getValueAt(row, 1).toString();     // Cột 1 (Ngày Nhập)
            String maKho = tbl_PhieuNhap.getValueAt(row, 2).toString();      // Cột 2 (Mã Kho)
            String maNCC = tbl_PhieuNhap.getValueAt(row, 3).toString();      // Cột 3 (Mã Nhà Cung Cấp)

            // Sử dụng định dạng ngày dd-MM-yyyy, ví dụ: "08-12-2024"
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date ngayNhap = sdf.parse(ngayStr);  // Chuyển đổi chuỗi ngày thành đối tượng Date

            // Gán giá trị vào các trường trong form
            txt_MaPhieuNhap.setText(maPhieuNhap);
            txt_MaKho1.setText(maKho);
            txt_MaNhaCungCap.setText(maNCC);
            txt_NgayNhap.setDate(ngayNhap);  // Chuyển ngày đã xử lý vào JDateChooser

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
// Thêm phiếu nhập mới
// Thêm phiếu nhập mới
private void addPhieuNhap() {
    // Kiểm tra dữ liệu nhập vào
    if (
        txt_MaPhieuNhap.getText().isEmpty() ||
        txt_MaKho1.getText().isEmpty() ||
        txt_MaNhaCungCap.getText().isEmpty() ||
        txt_NgayNhap.getDate() == null
    ) {
        Message.alert("Vui lòng nhập đầy đủ thông tin!");
        return;
    }

    // Tạo đối tượng model_PhieuNhap để lưu dữ liệu
    model_PhieuNhap pn = new model_PhieuNhap();
    pn.setMaPhieuNhap(txt_MaPhieuNhap.getText().trim());
    pn.setMaKho(txt_MaKho1.getText().trim());
    pn.setMaNhaCungCap(txt_MaNhaCungCap.getText().trim());
    pn.setNgayNhap(txt_NgayNhap.getDate());
    pn.setTrangThai("Chờ duyệt");

    try {
        // 1. Thêm phiếu nhập vào cơ sở dữ liệu
        pndao.insert(pn);

        // 2. Cập nhật lại bảng phiếu nhập
        fillToTablePhieuNhap();

        // 3. Gán mã phiếu nhập vừa tạo sang phần chi tiết
        txt_MaPhieuNhapCTPN.setText(pn.getMaPhieuNhap());

        // 4. Hiển thị thông báo thành công và mời người dùng nhập chi tiết
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Phiếu nhập " + pn.getMaPhieuNhap() + " đã được tạo. Mời bạn nhập chi tiết cho phiếu nhập này.",
            "Thêm Phiếu Nhập Thành Công", 
            JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

        // 5. Kiểm tra người dùng chọn "Yes" (Xác nhận)
        if (confirm == JOptionPane.YES_OPTION) {
            // Chuyển sang tab Chi Tiết Phiếu Nhập
            tab1_PhieuNhap.setSelectedIndex(1); // Tab thứ 2 (Chi Tiết Phiếu Nhập)
        } else {
            // Nếu người dùng chọn "No" (Hủy), vẫn ở lại tab Phiếu Nhập
            JOptionPane.showMessageDialog(this, "Bạn vẫn ở lại tab Phiếu Nhập để tiếp tục nhập.");
        }

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
// Cập nhật phiếu nhập và sửa ngay trong bảng
// Cập nhật phiếu nhập và sửa ngay trong bảng
// Cập nhật phiếu nhập và sửa ngay trong bảng
// Cập nhật phiếu nhập và sửa ngay trong bảng
private void updatePhieuNhap() {
    int row = tbl_PhieuNhap.getSelectedRow();
    if (row < 0) {
        Message.alert("Bạn phải chọn một dòng để sửa!");
        return;
    }

    // Kiểm tra dữ liệu đầu vào
    if (
        txt_MaPhieuNhap.getText().isEmpty() ||
        txt_MaNhaCungCap.getText().isEmpty() ||
        txt_MaKho1.getText().isEmpty() ||
        txt_NgayNhap.getDate() == null
    ) {
        Message.alert("Vui lòng nhập đầy đủ thông tin!");
        return;
    }

    // Tạo đối tượng model_PhieuNhap để lưu dữ liệu sửa
    model_PhieuNhap pn = new model_PhieuNhap();
    pn.setMaPhieuNhap(txt_MaPhieuNhap.getText().trim());  // Lấy mã phiếu nhập mới từ form
    pn.setNgayNhap(txt_NgayNhap.getDate());  // Ngày nhập mới từ form
    pn.setMaKho(txt_MaKho1.getText().trim());  // Mã kho mới từ form
    pn.setMaNhaCungCap(txt_MaNhaCungCap.getText().trim());  // Mã nhà cung cấp mới từ form
    pn.setTrangThai(tbl_PhieuNhap.getValueAt(row, 4).toString());  // Giữ nguyên trạng thái từ bảng

    boolean confirm = Message.confirm("Bạn có chắc chắn muốn cập nhật phiếu nhập có mã '" + pn.getMaPhieuNhap() + "' ?");
    if (confirm) {
        try {
            // Cập nhật phiếu nhập vào cơ sở dữ liệu
            pndao.update(pn);

            // Chuyển đổi ngày thành định dạng "dd-MM-yyyy"
            SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
            String ngayNhapFormatted = newFormat.format(pn.getNgayNhap());  // Chuyển ngày thành định dạng mong muốn

            // Cập nhật dữ liệu trong bảng ngay lập tức (không làm mới toàn bộ bảng)
            tbl_ModelPhieuNhap.setValueAt(pn.getMaPhieuNhap(), row, 0);  // Cập nhật cột Mã Phiếu Nhập
            tbl_ModelPhieuNhap.setValueAt(ngayNhapFormatted, row, 1);  // Cập nhật cột Ngày Nhập với định dạng "dd-MM-yyyy"
            tbl_ModelPhieuNhap.setValueAt(pn.getMaKho(), row, 2);  // Cập nhật cột Mã Kho
            tbl_ModelPhieuNhap.setValueAt(pn.getMaNhaCungCap(), row, 3);  // Cập nhật cột Mã Nhà Cung Cấp
            tbl_ModelPhieuNhap.setValueAt(pn.getTrangThai(), row, 4);  // Cập nhật cột Trạng Thái

            // Thông báo cho người dùng
            Message.alert("Cập nhật phiếu nhập thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            Message.error("Lỗi khi cập nhật phiếu nhập: " + e.getMessage());
        }
    } else {
        return;
    }
}







    // Tìm kiếm phiếu nhập
    private void searchPhieuNhap() {
    // Lấy giá trị từ JTextField và JComboBox
    String tuKhoa = txt_TimKiem.getText().trim();  // Từ khóa tìm kiếm
    String tieuChi = (String) cbo_TimKiemPhieuNhap.getSelectedItem();  // Tiêu chí tìm kiếm (Mã Phiếu Nhập, Mã Kho, v.v.)

    // Kiểm tra nếu không có từ khóa tìm kiếm
    if (tuKhoa.isEmpty()) {
        Message.alert("Vui lòng nhập từ khóa tìm kiếm!");
        return;
    }

    // Xây dựng câu truy vấn SQL theo tiêu chí
    String sql = "SELECT * FROM PHIEUNHAP WHERE ";

    switch (tieuChi) {
        case "Mã Phiếu Nhập":
            sql += "MaPhieuNhap LIKE ?";
            break;
        case "Mã Kho":
            sql += "MaKho LIKE ?";
            break;
        case "Trạng Thái":
            sql += "TrangThai LIKE ?";
            break;
        case "Mã Nhà Cung Cấp":
            sql += "MaNhaCungCap LIKE ?";
            break;
        default:
            sql = "SELECT * FROM PHIEUNHAP";  // Trường hợp mặc định (không có tiêu chí)
            break;
    }

    // Tìm kiếm với câu truy vấn SQL
    try {
        // Gọi phương thức selectBySql từ DAO để thực hiện tìm kiếm
        List<model_PhieuNhap> list_PhieuNhap = pndao.selectBySql(sql, "%" + tuKhoa + "%");

        // Nếu không tìm thấy kết quả
        if (list_PhieuNhap.isEmpty()) {
            Message.alert("Không tìm thấy kết quả phù hợp!");

            // Khi không có kết quả, hiển thị tất cả phiếu nhập
            fillToTablePhieuNhap(); // Hiển thị lại toàn bộ phiếu nhập
            return;
        }

        // Xóa dữ liệu cũ trong bảng
        tbl_ModelPhieuNhap.setRowCount(0);

        // Hiển thị kết quả tìm kiếm lên bảng
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
        Message.error("Lỗi khi tìm kiếm phiếu nhập: " + e.getMessage());
    }
}


    private void refreshForm(){
    // Làm mới các trường trong form
    txt_MaPhieuNhap.setText("");         // Xóa Mã Phiếu Nhập
    txt_MaKho1.setText("");              // Xóa Mã Kho
    txt_MaNhaCungCap.setText("");        // Xóa Mã Nhà Cung Cấp
    txt_NgayNhap.setDate(new Date());   // Đặt lại ngày nhập về ngày hiện tại

    // Xóa các trường tìm kiếm
    txt_TimKiem.setText("");            // Xóa từ khóa tìm kiếm Phiếu Nhập
    txt_TimKiemCTPN.setText("");        // Xóa từ khóa tìm kiếm Chi Tiết Phiếu Nhập
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


  // Hàm để làm mới bảng chi tiết phiếu nhập
private void fillToTableChiTietPhieuNhap() {
    tbl_ModelChiTietPhieuNhap.setRowCount(0); // Xóa dữ liệu cũ trong bảng

    try {
        // Lấy danh sách chi tiết phiếu nhập từ cơ sở dữ liệu
        List<model_ChiTietPhieuNhap> list = chitietDAO.selectAll();

        // Hiển thị lại dữ liệu vào bảng
        for (model_ChiTietPhieuNhap ct : list) {
            Object[] row = new Object[] {
                ct.getMaPhieuNhap(),
                ct.getMaVatTu(),
                ct.getDonViGoc(),
                ct.getDonViChuyenDoi(),
                ct.getSoLuongNhap(),
                ct.getSoLuongQuyDoi()
            };
            tbl_ModelChiTietPhieuNhap.addRow(row);
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "❌ Lỗi khi tải danh sách chi tiết phiếu nhập!");
    }
}

public void themChiTietPhieuNhap() {
    try {
        // Kiểm tra các combo box có giá trị hay không
        if (cboNhomVatTu.getSelectedItem() == null ||
            cboDonViGoc.getSelectedItem() == null ||
            cboDonViDich.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "⚠ Vui lòng chọn đầy đủ Nhóm, Đơn vị gốc, Đơn vị đích.");
            return;
        }

        // Lấy dữ liệu đầu vào từ các trường nhập liệu
        String maPhieuNhap = txt_MaPhieuNhapCTPN.getText().trim();
        String maVatTu = txt_MaVatTu.getText().trim();
        String nhomVatTu = cboNhomVatTu.getSelectedItem().toString();
        String tenDonViGoc = cboDonViGoc.getSelectedItem().toString();
        String tenDonViDich = cboDonViDich.getSelectedItem().toString();

        if (maPhieuNhap.isEmpty() || maVatTu.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠ Mã phiếu nhập hoặc mã vật tư không được để trống!");
            return;
        }

        double soLuongNhap = 0;
        double soLuongQuyDoi = 0;

        try {
            // Chuyển đổi số lượng nhập và số lượng quy đổi sang kiểu số
            soLuongNhap = Double.parseDouble(txt_soluongNhap.getText().trim());
            soLuongQuyDoi = Double.parseDouble(txtKetQua.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "❌ Vui lòng nhập đúng định dạng số!");
            return;
        }

        // Lấy mã đơn vị gốc và chuyển đổi
        int maDonViGoc = donViTinhMap.getOrDefault(tenDonViGoc, -1);
        int maDonViDich = donViTinhMap.getOrDefault(tenDonViDich, -1);

        if (maDonViGoc == -1 || maDonViDich == -1) {
            JOptionPane.showMessageDialog(this, "⚠ Không tìm thấy mã đơn vị tương ứng!");
            return;
        }

        // Tạo đối tượng chi tiết phiếu nhập
        model_ChiTietPhieuNhap ct = new model_ChiTietPhieuNhap();
        ct.setMaPhieuNhap(maPhieuNhap);                // Mã phiếu nhập
        ct.setMaVatTu(maVatTu);                        // Mã vật tư
        ct.setDonViGoc(maDonViGoc);                    // Đơn vị gốc
        ct.setDonViChuyenDoi(maDonViDich);             // Đơn vị chuyển đổi
        ct.setSoLuongNhap((float) soLuongNhap);        // Số lượng nhập
        ct.setSoLuongQuyDoi((float) soLuongQuyDoi);    // Số lượng quy đổi

        // Gọi DAO để thêm chi tiết phiếu nhập vào cơ sở dữ liệu
        new ChiTietPhieuNhapDAO().insert(ct);

        // Cập nhật lại bảng chi tiết phiếu nhập
        fillToTableChiTietPhieuNhap();

        // Thông báo thành công
        JOptionPane.showMessageDialog(this, "✅ Thêm chi tiết phiếu nhập thành công!");

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "❌ Lỗi khi thêm chi tiết: " + e.getMessage());
    }
}

private void searchChiTietPhieuNhap() {
    // Lấy giá trị từ JTextField và JComboBox
    String tuKhoa = txt_TimKiemCTPN.getText().trim();  // Từ khóa tìm kiếm
    String tieuChi = (String) cbo_TimKiemCTPN.getSelectedItem();  // Tiêu chí tìm kiếm (Mã Phiếu Nhập, Mã Vật Tư, v.v.)

    // Kiểm tra nếu không có từ khóa tìm kiếm
    if (tuKhoa.isEmpty()) {
        Message.alert("Vui lòng nhập từ khóa tìm kiếm!");
        return;
    }

    // Xây dựng câu truy vấn SQL theo tiêu chí
    String sql = "SELECT * FROM CHITIETPHIEUNHAP WHERE ";

    switch (tieuChi) {
        case "Mã Phiếu Nhập":
            sql += "MaPhieuNhap LIKE ?";
            break;
        case "Mã Vật Tư":
            sql += "MaVatTu LIKE ?";
            break;
        default:
            sql = "SELECT * FROM CHITIETPHIEUNHAP";  // Trường hợp mặc định (không có tiêu chí)
            break;
    }

    // Tìm kiếm với câu truy vấn SQL
    try {
        // Gọi phương thức selectBySql từ DAO để thực hiện tìm kiếm
        List<model_ChiTietPhieuNhap> list_ChiTietPhieuNhap = chitietDAO.selectBySql(sql, "%" + tuKhoa + "%");

        // Nếu không tìm thấy kết quả
        if (list_ChiTietPhieuNhap.isEmpty()) {
            Message.alert("Không tìm thấy kết quả phù hợp!");

            // Khi không có kết quả, hiển thị tất cả chi tiết phiếu nhập
            fillToTableChiTietPhieuNhap(); // Hiển thị lại toàn bộ chi tiết phiếu nhập
            return;
        }

        // Xóa dữ liệu cũ trong bảng
        tbl_ModelChiTietPhieuNhap.setRowCount(0);

        // Hiển thị kết quả tìm kiếm lên bảng
        for (model_ChiTietPhieuNhap ct : list_ChiTietPhieuNhap) {
            Object[] row = new Object[]{
                ct.getMaPhieuNhap(),
                ct.getMaVatTu(),
                ct.getDonViGoc(),
                ct.getDonViChuyenDoi(),
                ct.getSoLuongNhap(),
                ct.getSoLuongQuyDoi()
            };
            tbl_ModelChiTietPhieuNhap.addRow(row);  // Thêm dòng vào bảng
        }

    } catch (Exception e) {
        e.printStackTrace();
        Message.error("Lỗi khi tìm kiếm chi tiết phiếu nhập: " + e.getMessage());
    }
}











private void deleteChiTietPhieuNhap() {
     try {
        // Lấy dòng đã chọn trong bảng Chi Tiết Phiếu Nhập
        int selectedRow = tbl_ChiTietPhieuNhap.getSelectedRow();
        
        if (selectedRow < 0) {
            // Nếu chưa chọn dòng nào
            JOptionPane.showMessageDialog(this, "⚠ Vui lòng chọn một dòng để xóa!");
            return;
        }

        // Lấy Mã Phiếu Nhập và Mã Vật Tư từ dòng được chọn
        String maPhieuNhap = tbl_ChiTietPhieuNhap.getValueAt(selectedRow, 0).toString();
        String maVatTu = tbl_ChiTietPhieuNhap.getValueAt(selectedRow, 1).toString();

        // Hỏi người dùng có chắc chắn xóa không
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn xóa chi tiết phiếu nhập này và phiếu nhập?", 
            "Xóa Chi Tiết", 
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Xóa chi tiết phiếu nhập
            new ChiTietPhieuNhapDAO().delete(maPhieuNhap, maVatTu);

            // Kiểm tra xem phiếu nhập có còn chi tiết nào không
            List<model_ChiTietPhieuNhap> remainingDetails = new ChiTietPhieuNhapDAO().selectByPhieuNhap(maPhieuNhap);
            if (remainingDetails.isEmpty()) {
                // Nếu không còn chi tiết nào, xóa phiếu nhập
                new PhieuNhapDAO().delete(maPhieuNhap);
            }

            // Cập nhật lại bảng chi tiết phiếu nhập và phiếu nhập
            fillToTableChiTietPhieuNhap();
            fillToTablePhieuNhap();

            JOptionPane.showMessageDialog(this, "✅ Xóa chi tiết phiếu nhập và phiếu nhập thành công!");
        }

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "❌ Lỗi khi xóa chi tiết phiếu nhập và phiếu nhập: " + e.getMessage());
    }
}

// Phương thức làm mới bảng chi tiết phiếu nhập và các trường nhập liệu
private void refreshChiTietPhieuNhap() {
    // 1. Làm mới các trường nhập liệu
    txt_MaPhieuNhapCTPN.setText("");    // Xóa Mã Phiếu Nhập
    txt_MaVatTu.setText("");            // Xóa Mã Vật Tư
    txt_soluongNhap.setText("");       // Xóa Số Lượng Nhập
    txtKetQua.setText("");             // Xóa Kết Quả
    cboNhomVatTu.setSelectedIndex(0);  // Đặt lại ComboBox Nhóm Vật Tư (hoặc chọn mặc định)
    cboLoaiVatTu.setSelectedIndex(0);  // Đặt lại ComboBox Loại Vật Tư (hoặc chọn mặc định)
    cboDonViGoc.setSelectedIndex(0);   // Đặt lại ComboBox Đơn Vị Gốc
    cboDonViDich.setSelectedIndex(0);  // Đặt lại ComboBox Đơn Vị Đích
    txt_TimKiemCTPN.setText("");       // Xóa ô tìm kiếm

    // 2. Làm mới bảng chi tiết phiếu nhập (nếu cần hiển thị lại dữ liệu từ cơ sở dữ liệu)
    fillToTableChiTietPhieuNhap();     // Đảm bảo bảng chi tiết phiếu nhập được làm mới với dữ liệu mới nhất
}



private void updateChiTietPhieuNhap() {
    // Lấy dữ liệu từ các trường nhập liệu
    String maPhieuNhap = txt_MaPhieuNhapCTPN.getText().trim();
    String maVatTu = txt_MaVatTu.getText().trim();
    String soLuongNhap = txt_soluongNhap.getText().trim();
    String soLuongQuyDoi = txtKetQua.getText().trim();

    String donViGoc = (String) cboDonViGoc.getSelectedItem();
    String donViDich = (String) cboDonViDich.getSelectedItem();

    // Kiểm tra các trường nhập liệu không để trống
    if (maPhieuNhap.isEmpty() || maVatTu.isEmpty() || soLuongNhap.isEmpty() || soLuongQuyDoi.isEmpty()) {
        JOptionPane.showMessageDialog(this, "⚠ Vui lòng nhập đầy đủ thông tin!");
        return;
    }

    try {
        // Lấy mã đơn vị gốc và đích từ các combo box
        int maDonViGoc = donViTinhMap.get(donViGoc);
        int maDonViDich = donViTinhMap.get(donViDich);

        // Tạo đối tượng chi tiết phiếu nhập để cập nhật
        model_ChiTietPhieuNhap ct = new model_ChiTietPhieuNhap();
        ct.setMaPhieuNhap(maPhieuNhap);                // Mã phiếu nhập
        ct.setMaVatTu(maVatTu);                        // Mã vật tư
        ct.setDonViGoc(maDonViGoc);                    // Đơn vị gốc
        ct.setDonViChuyenDoi(maDonViDich);             // Đơn vị chuyển đổi
        ct.setSoLuongNhap(Float.parseFloat(soLuongNhap));  // Số lượng nhập
        ct.setSoLuongQuyDoi(Float.parseFloat(soLuongQuyDoi)); // Số lượng quy đổi

        // Gọi DAO để cập nhật chi tiết phiếu nhập vào cơ sở dữ liệu
        new ChiTietPhieuNhapDAO().update(ct);

        // Cập nhật lại bảng chi tiết phiếu nhập
        fillToTableChiTietPhieuNhap();

        // Thông báo thành công
        JOptionPane.showMessageDialog(this, "✅ Sửa chi tiết phiếu nhập thành công!");
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "❌ Lỗi khi sửa chi tiết: " + e.getMessage());
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
        pn_PhieuNhap = new javax.swing.JPanel();
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
        cbo_TimKiemPhieuNhap = new javax.swing.JComboBox<>();
        pn_ChiTietPhieuNhap = new javax.swing.JPanel();
        txt_MaPhieuNhapCTPN = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txt_MaVatTu = new javax.swing.JTextField();
        txt_soluongNhap = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_ChiTietPhieuNhap = new javax.swing.JTable();
        txt_TimKiemCTPN = new javax.swing.JTextField();
        btn_ThemCTPN = new javax.swing.JButton();
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
        cbo_TimKiemCTPN = new javax.swing.JComboBox<>();

        tab1_PhieuNhap.setBackground(new java.awt.Color(255, 255, 255));

        pn_PhieuNhap.setBackground(new java.awt.Color(255, 255, 255));

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
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã Phiếu Nhập", "Ngày Nhập", "Mã Kho", "Mã Nhà Cung Cấp", "Trạng Thái"
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

        cbo_TimKiemPhieuNhap.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mã Phiếu Nhập", "Mã Kho", "Mã Nhà Cung Cấp", "Trạng Thái" }));

        javax.swing.GroupLayout pn_PhieuNhapLayout = new javax.swing.GroupLayout(pn_PhieuNhap);
        pn_PhieuNhap.setLayout(pn_PhieuNhapLayout);
        pn_PhieuNhapLayout.setHorizontalGroup(
            pn_PhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_PhieuNhapLayout.createSequentialGroup()
                .addGroup(pn_PhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pn_PhieuNhapLayout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pn_PhieuNhapLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pn_PhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pn_PhieuNhapLayout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(pn_PhieuNhapLayout.createSequentialGroup()
                                .addGroup(pn_PhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pn_PhieuNhapLayout.createSequentialGroup()
                                        .addGap(0, 3, Short.MAX_VALUE)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pn_PhieuNhapLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(pn_PhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pn_PhieuNhapLayout.createSequentialGroup()
                                        .addComponent(btn_Them, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(48, 48, 48))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pn_PhieuNhapLayout.createSequentialGroup()
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))))
                        .addGroup(pn_PhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pn_PhieuNhapLayout.createSequentialGroup()
                                .addComponent(btn_Xoa, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btn_Sua, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18))
                            .addGroup(pn_PhieuNhapLayout.createSequentialGroup()
                                .addGroup(pn_PhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txt_NgayNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(pn_PhieuNhapLayout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(txt_MaNhaCungCap, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txt_MaKho1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_MaPhieuNhap, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)))))
                .addGroup(pn_PhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 869, Short.MAX_VALUE)
                    .addGroup(pn_PhieuNhapLayout.createSequentialGroup()
                        .addComponent(btn_TimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txt_TimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbo_TimKiemPhieuNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pn_PhieuNhapLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_LamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_XemCT2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_XuatExcel2, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pn_PhieuNhapLayout.setVerticalGroup(
            pn_PhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_PhieuNhapLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(pn_PhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_TimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_TimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbo_TimKiemPhieuNhap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pn_PhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pn_PhieuNhapLayout.createSequentialGroup()
                        .addGroup(pn_PhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_NgayNhap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(pn_PhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_MaPhieuNhap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17))
                        .addGap(18, 18, 18)
                        .addGroup(pn_PhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txt_MaKho1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(pn_PhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_MaNhaCungCap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(pn_PhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_Them, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_Xoa, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_Sua, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pn_PhieuNhapLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pn_PhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_XuatExcel2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_XemCT2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_LamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(68, 68, 68))))
        );

        tab1_PhieuNhap.addTab("Phiếu Nhập", pn_PhieuNhap);

        pn_ChiTietPhieuNhap.setPreferredSize(new java.awt.Dimension(988, 700));

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

        btn_ThemCTPN.setText("Thêm");
        btn_ThemCTPN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ThemCTPNActionPerformed(evt);
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
        jLabel18.setText("Nhóm vật tư:\n");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel19.setText("Loại vật tư:");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel20.setText("Đơn vị gốc:");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel21.setText("Đơn vị đích:");

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel22.setText("Số lượng chuyển đổi:");

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel23.setText("Kết quả:");

        cboNhomVatTu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));

        txtKetQua.setEditable(false);

        btnChuyenDoi.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnChuyenDoi.setText("Chuyển đổi");
        btnChuyenDoi.setToolTipText("");

        cbo_TimKiemCTPN.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mã Vật Tư", "Mã Phiếu Nhập" }));

        javax.swing.GroupLayout pn_ChiTietPhieuNhapLayout = new javax.swing.GroupLayout(pn_ChiTietPhieuNhap);
        pn_ChiTietPhieuNhap.setLayout(pn_ChiTietPhieuNhapLayout);
        pn_ChiTietPhieuNhapLayout.setHorizontalGroup(
            pn_ChiTietPhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_ChiTietPhieuNhapLayout.createSequentialGroup()
                .addGroup(pn_ChiTietPhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pn_ChiTietPhieuNhapLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pn_ChiTietPhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pn_ChiTietPhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pn_ChiTietPhieuNhapLayout.createSequentialGroup()
                                    .addComponent(jLabel13)
                                    .addGap(170, 170, 170))
                                .addGroup(pn_ChiTietPhieuNhapLayout.createSequentialGroup()
                                    .addGroup(pn_ChiTietPhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pn_ChiTietPhieuNhapLayout.createSequentialGroup()
                                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(txt_MaVatTu))
                                        .addGroup(pn_ChiTietPhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(pn_ChiTietPhieuNhapLayout.createSequentialGroup()
                                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(txt_MaPhieuNhapCTPN, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(pn_ChiTietPhieuNhapLayout.createSequentialGroup()
                                                .addGroup(pn_ChiTietPhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(pn_ChiTietPhieuNhapLayout.createSequentialGroup()
                                                        .addGap(18, 18, 18)
                                                        .addGroup(pn_ChiTietPhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(jLabel18)
                                                            .addGroup(pn_ChiTietPhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                .addComponent(jLabel21)
                                                                .addComponent(jLabel20)
                                                                .addComponent(jLabel19)))
                                                        .addGap(47, 47, 47))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pn_ChiTietPhieuNhapLayout.createSequentialGroup()
                                                        .addComponent(btn_ThemCTPN, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(18, 18, 18)))
                                                .addGroup(pn_ChiTietPhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(pn_ChiTietPhieuNhapLayout.createSequentialGroup()
                                                        .addComponent(btn_XoaCTPX, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(18, 18, 18)
                                                        .addGroup(pn_ChiTietPhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                            .addComponent(txtKetQua)
                                                            .addComponent(cboNhomVatTu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(btn_SuaCTPX, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                                            .addComponent(cboLoaiVatTu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(cboDonViGoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(cboDonViDich, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(txtSoLuong)))
                                                    .addComponent(btnChuyenDoi)))
                                            .addGroup(pn_ChiTietPhieuNhapLayout.createSequentialGroup()
                                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(txt_soluongNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGap(18, 18, 18)))))
                    .addGroup(pn_ChiTietPhieuNhapLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel22))
                    .addGroup(pn_ChiTietPhieuNhapLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jLabel23)))
                .addGap(18, 18, 18)
                .addGroup(pn_ChiTietPhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pn_ChiTietPhieuNhapLayout.createSequentialGroup()
                        .addComponent(btn_TimCTPN, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txt_TimKiemCTPN, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cbo_TimKiemCTPN, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 854, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pn_ChiTietPhieuNhapLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_Lammoi1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_XemCT1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_XuatExcel1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50))
        );
        pn_ChiTietPhieuNhapLayout.setVerticalGroup(
            pn_ChiTietPhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_ChiTietPhieuNhapLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(pn_ChiTietPhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pn_ChiTietPhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_TimKiemCTPN, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_TimCTPN, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbo_TimKiemCTPN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(pn_ChiTietPhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pn_ChiTietPhieuNhapLayout.createSequentialGroup()
                        .addGroup(pn_ChiTietPhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pn_ChiTietPhieuNhapLayout.createSequentialGroup()
                                .addGroup(pn_ChiTietPhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(txt_MaPhieuNhapCTPN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(pn_ChiTietPhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(txt_MaVatTu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(pn_ChiTietPhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel9)
                                    .addComponent(txt_soluongNhap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel13)
                                .addGap(12, 12, 12)
                                .addGroup(pn_ChiTietPhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel18)
                                    .addComponent(cboNhomVatTu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(pn_ChiTietPhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel19)
                                    .addComponent(cboLoaiVatTu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(52, 52, 52))
                            .addGroup(pn_ChiTietPhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cboDonViGoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel20)))
                        .addGap(20, 20, 20)
                        .addGroup(pn_ChiTietPhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(cboDonViDich, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pn_ChiTietPhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pn_ChiTietPhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23)
                            .addComponent(txtKetQua, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(btnChuyenDoi, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pn_ChiTietPhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Lammoi1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_XemCT1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_XuatExcel1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_ThemCTPN, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_XoaCTPX, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_SuaCTPX, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        tab1_PhieuNhap.addTab("Chi Tiết Phiếu Nhập", pn_ChiTietPhieuNhap);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tab1_PhieuNhap)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tab1_PhieuNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 683, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tbl_ChiTietPhieuNhapMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_ChiTietPhieuNhapMouseClicked
        //clickHerePNCT();
    }//GEN-LAST:event_tbl_ChiTietPhieuNhapMouseClicked

    private void btn_Lammoi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Lammoi1ActionPerformed
        refreshChiTietPhieuNhap();
    }//GEN-LAST:event_btn_Lammoi1ActionPerformed

    private void btn_XemCT1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_XemCT1ActionPerformed
        //viewChiTietPhieuNhapInPN();
    }//GEN-LAST:event_btn_XemCT1ActionPerformed

    private void btn_TimCTPNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_TimCTPNActionPerformed
    searchChiTietPhieuNhap();


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

    private void btn_ThemCTPNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ThemCTPNActionPerformed
        themChiTietPhieuNhap();
    }//GEN-LAST:event_btn_ThemCTPNActionPerformed

    private void btn_XoaCTPXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_XoaCTPXActionPerformed
        deleteChiTietPhieuNhap();
    }//GEN-LAST:event_btn_XoaCTPXActionPerformed

    private void btn_SuaCTPXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SuaCTPXActionPerformed
         updateChiTietPhieuNhap();
    }//GEN-LAST:event_btn_SuaCTPXActionPerformed

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
    private javax.swing.JButton btn_ThemCTPN;
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
    private javax.swing.JComboBox<String> cbo_TimKiemCTPN;
    private javax.swing.JComboBox<String> cbo_TimKiemPhieuNhap;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel pn_ChiTietPhieuNhap;
    private javax.swing.JPanel pn_PhieuNhap;
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
