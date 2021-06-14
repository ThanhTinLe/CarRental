/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tinlt.controller;

import tinlt.dao.UserDAO;
import dto.UserDTO;
import java.io.IOException;
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
@WebServlet(name = "LoginController", urlPatterns = {"/LoginController"})
public class LoginController extends HttpServlet {
    private static final String ERROR="login.jsp";
    private static final String NEW="verify.jsp";
    private static final String ACTIVE="SearchController";
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
        String url=ERROR;
        try {
            HttpSession session = request.getSession();
            String userID = request.getParameter("txtUserID");
            String password = request.getParameter("txtPassword");
            String userError = "";
            String passwordError = "";
            String loginError = "";
            boolean check = true;
            if (userID.isEmpty()) {
                check = false;
                userError = "User ID not empty";
            }
            if (password.isEmpty()) {
                check = false;
                passwordError = "Password not empty";
            }
            boolean valid = true;
            String errorString = null;
            if (check) {
                String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
                valid = VerifyUtils.verify(gRecaptchaResponse);
                if (!valid) {
                    errorString = "Captcha invalid!";
                    request.setAttribute("capchaError", errorString);
                } else {
                    UserDAO dao = new UserDAO();
                    UserDTO user = dao.checkLogin(userID, password);
                    if (user != null) {
                        session.setAttribute("LOGIN_USER", user);
                        if ("New".equals(user.getRole())) {
                            url=NEW;
                        } else if ("Active".equals(user.getRole())) {
                            url = ACTIVE;
                        }
                    } else {
                        loginError = "UserID or Password";
                    }
                }
            }
            request.setAttribute("ERRORLOGIN", loginError);
            request.setAttribute("ERRORUSER", userError);
            request.setAttribute("ERRORPASSWORD", passwordError);
        } catch (Exception e) {
            log(e.toString());
        }finally{
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
