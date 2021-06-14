/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tinlt.controller;

import tinlt.dao.CarDAO;
import dto.CarDTO;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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
@WebServlet(name = "SearchController", urlPatterns = {"/SearchController"})
public class SearchController extends HttpServlet {

    private static final String SUCCESS = "home.jsp";

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
        String url = SUCCESS;
        try {
            String actionSearch = request.getParameter("rdSearch");
            HttpSession session = request.getSession();

            if (actionSearch == null) {
                actionSearch = (String) session.getAttribute("METHODSEARCH");
                if (actionSearch == null) {
                    actionSearch = "sName";
                }
            }
            session.setAttribute("METHODSEARCH", actionSearch);
            actionSearch = (String) session.getAttribute("METHODSEARCH");
            if (actionSearch.trim().isEmpty()) {
                request.setAttribute("searchE", "Choice option Search");
            } else {
                boolean check = true;
                String rentalDate = request.getParameter("txtRentalDate");
                if (rentalDate != null) {
                    if (rentalDate.trim().isEmpty()) {
                        request.setAttribute("rentalE", "Enter Rentaldate");
                        check = false;
                    } else {
                        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
                        Date curentDate = new Date();
                        Date curentDate1 = new Date(curentDate.getTime()+(24 * 3600 * 1000));
                        Date relDate = date.parse(rentalDate);
                        if (relDate.before(date.parse(date.format(curentDate1)))) {
                            check = false;
                            request.setAttribute("rentalE", "check Date Rental");
                        }
                    }
                } else {
                    rentalDate = (String) session.getAttribute("RENTALDATE");
                    if (rentalDate == null) {
                        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
                        Date curentDate = new Date();
                        Date curentDate1 = new Date(curentDate.getTime()+(24 * 3600 * 1000));
                        rentalDate = date.format(curentDate1);
                    }
                }
                session.setAttribute("RENTALDATE", rentalDate);
                rentalDate = (String) session.getAttribute("RENTALDATE");
                String returnDate = request.getParameter("txtReturnDate");
                if (returnDate != null) {
                    if (returnDate.trim().isEmpty()) {
                        request.setAttribute("returnE", "Enter Rentaldate");
                        check = false;
                    } else {
                        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
                        Date relDate = date.parse(rentalDate);
                        Date renDate = date.parse(returnDate);
                        if (renDate.before(date.parse(date.format(relDate)))) {
                            check = false;
                            request.setAttribute("returnE", "check Date return");
                        }
                    }
                } else {
                    returnDate = (String) session.getAttribute("RETURNDATE");
                    if (returnDate == null) {
                        LocalDate rental = java.time.LocalDate.now();
                        LocalDate rdate = rental.plusDays(2);
                        returnDate = rdate.toString();
                    }
                }
                session.setAttribute("RETURNDATE", returnDate);
                returnDate = (String) session.getAttribute("RETURNDATE");
                String amount = request.getParameter("txtAmount");
                if (amount != null) {
                    if (amount.trim().isEmpty()) {
                        request.setAttribute("amount", "Enter amount");
                        check = false;
                    }

                }else{
                    amount=(String) session.getAttribute("AMOUNT");
                    if(amount==null){
                        amount="1";
                    }
                }
                session.setAttribute("AMOUNT", amount);
                amount = (String) session.getAttribute("AMOUNT");
                
                CarDAO cdao = new CarDAO();
                ArrayList<CarDTO> list = new ArrayList<>();
                if (check) {
                    int index = 0;
                    String txtIndex = request.getParameter("index");
                    if (txtIndex == null) {
                        index = 1;
                    } else {
                        if (txtIndex.trim().isEmpty()) {
                            index = 1;
                        } else {
                            index = Integer.parseInt(txtIndex);
                        }
                    }
                    session.setAttribute("INDEX", index);
                    index = (int) session.getAttribute("INDEX");
                    if ("sName".equals(actionSearch)) {
                        String name = request.getParameter("txtNameCar");
                        if(name==null){
                            name=(String) session.getAttribute("SNAME");
                            if(name==null){
                                name="";
                            }
                        }
                        session.setAttribute("SNAME", name);
                        name=(String) session.getAttribute("SNAME");
                        if (name != null) {
                            int num=cdao.countCarName(name, amount, rentalDate, returnDate);
                            session.setAttribute("NUMPAGE", num);
                            list = cdao.getCarName(name, amount, rentalDate, returnDate, index);
                            session.setAttribute("LIST_CAR", list);
                        }
                    } else if ("sAnother".equals(actionSearch)) {
                        String category = request.getParameter("selCategory");
                        if (category == null) {
                            category=(String) session.getAttribute("SSELECT");
                        }
                        session.setAttribute("SSELECT", category);
                        category=(String) session.getAttribute("SSELECT");
                        if (category != null) {
                            if(category.equals("-1")){
                                int num =cdao.coutntAllCarCategory(amount, rentalDate, returnDate);
                                list=cdao.getAllCarCategory(amount, rentalDate, returnDate, index);
                                session.setAttribute("NUMPAGE", num);
                            }else{
                                int num=cdao.coutntCarCategory(category, amount, rentalDate, returnDate);
                                list=cdao.getCarCategory(category, amount, rentalDate, returnDate, index);
                                session.setAttribute("NUMPAGE", num);
                            }
                            session.setAttribute("LIST_CAR", list);
                        }
                    }
                }
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
