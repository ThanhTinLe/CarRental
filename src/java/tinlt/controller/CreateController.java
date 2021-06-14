/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tinlt.controller;

import tinlt.dao.UserDAO;
import dto.UserDTO;
import dto.UserErrorDTO;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Ray Khum
 */
@WebServlet(name = "CreateController", urlPatterns = {"/CreateController"})
public class CreateController extends HttpServlet {

    private static final String SUCCESS = "verify.jsp";
    private static final String ERROR = "create.jsp";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = ERROR;
        try {
            boolean check = true;
            UserDAO udao=new UserDAO();
            UserErrorDTO userE = new UserErrorDTO("", "", "", "", "","");
            String userID = request.getParameter("txtUserID");
            if (userID != null) {
                if (userID.trim().isEmpty()) {
                    check = false;
                    userE.setUserE("userID not empty");
                }else{
                    if((!udao.checkUSer(userID))){
                        check=false;
                        userE.setUserE("userID is exit");
                    }
                }
            }else{
                check = false;
            }
            String name=request.getParameter("txtName");
            if (name != null) {
                if (name.trim().isEmpty()) {
                    check = false;
                    userE.setNameE("Name not empty");
                }
            }else{
                check = false;
            }
            String password = request.getParameter("txtPassword");
            if(password!=null){
                if(password.trim().isEmpty()){
                    check=false;
                    userE.setPasswordE("password not empry");
                }
            }else{
                check=false;
            }
            String confirm=request.getParameter("txtConfirm");
            if(confirm!=null){
                if(confirm.trim().isEmpty()){
                    check=false;
                    userE.setConfrimE("Confirm not empty");
                }else{
                    if(!confirm.equals(password)){
                        check=false;
                        userE.setConfrimE("Confirm is Error");
                    }
                }
            }else{
                check=false;
            }
            String phone=request.getParameter("txtPhone");
            if(phone!=null){
                if(phone.trim().length()!=10){
                    check=false;
                    userE.setPhoneE("Phone is number(10 char)");
                }
            }else{
                check=false;
            }
            String address=request.getParameter("txtAddress");
            if(address!=null){
                if(address.trim().isEmpty()){
                    check=false;
                    userE.setAddressE("Address not empty");
                }
            }else{
                check=false;
            }
            if(check){
                Random rs=new Random();
                int verify=rs.nextInt(900000)+100000;
                Timestamp createDate = new Timestamp(System.currentTimeMillis());
                UserDTO user=new UserDTO(userID, name, password, phone, address, "New", verify+"", createDate.toString(),"");
                udao.createAcount(user);
                HttpSession session=request.getSession();
                session.setAttribute("LOGIN_USER", user);
                url=SUCCESS;
            }else{
                request.setAttribute("CREATEERROR", userE);
            }
        } catch (Exception e) {
            log(e.toString());
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
