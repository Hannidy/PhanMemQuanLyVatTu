/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import javax.swing.JOptionPane;
        
public class Message {
    /*
     * Hiển thị thông báo cho người dùng
     * @param parent là cửa sổ chứa thông báo
     * @param message là thông báo
    */
    
    public static void alert(String message) {
//        ImageIcon icon = new ImageIcon("C:\\ImageAndIcon\\401285_java_icon.png");
        JOptionPane.showMessageDialog(null, message, 
                "Hệ thống quản lý vật tư", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /*
     * Hiển thị thông báo cho người dùng
     * @param parent là cửa sổ chứa thông báo
     * @param message là lỗi
    */
    public static void error(String message){
        JOptionPane.showMessageDialog(null, message, 
                "Hệ thống quản lý vật tư", JOptionPane.ERROR_MESSAGE);
//        JOptionPane.showMessageDialog(parent, message, message, 0, icon);
    }
    /*
     * Hiển thị thông báo và yêu cầu người dùng xác nhận
     * @param parent là cửa sổ chứa thông báo
     * @param message là câu hỏi yes/no
     * @return là kết quả nhận được true/false
    */    
    public static boolean confirm(String message) {
        int result = JOptionPane.showConfirmDialog(null, message, 
                "Hệ thống quản lý vật tư", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }
    /*
     * Hiển thị thông báo yêu cầu nhập dữ liệu
     * @param parent là cửa sổ chứa thông báo
     * @param message là thông báo nhắc nhở nhập
     * @return là kết quả nhận được từ người sử dụng nhập vào
    */    
    public static String prompt(String message) {
        return JOptionPane.showInputDialog(null, message, 
                "Hệ thống quản lý vật tư", JOptionPane.INFORMATION_MESSAGE);
    }
}
