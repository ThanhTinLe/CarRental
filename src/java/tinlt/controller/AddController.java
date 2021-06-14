/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tinlt.controller;

import tinlt.dao.CarDAO;
import dto.CarDTO;
import dto.OrderDTO;
import dto.OrderDetailDTO;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
@WebServlet(name = "AddController", urlPatterns = {"/AddController"})
public class AddController extends HttpServlet {

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
            HttpSession session = request.getSession();
            String carID = request.getParameter("carID");
            if (carID != null) {
                if (!carID.trim().isEmpty()) {
                    CarDAO cdao = new CarDAO();
                    CarDTO cdto = cdao.getCar(carID);
                    if (cdto == null) {
                        request.setAttribute("ADDE", "Add car Error");
                    } else {
                        OrderDTO cart = (OrderDTO) session.getAttribute("CART");
                        if (cart == null) {
                            cart = new OrderDTO();
                        }
                        OrderDetailDTO orderDetaildto = new OrderDetailDTO();
                        orderDetaildto.setCar(cdao.getCar(carID));
                        orderDetaildto.setRentalDate((String) session.getAttribute("RENTALDATE"));
                        orderDetaildto.setReturnDate((String) session.getAttribute("RETURNDATE"));
                        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
                        Date cIn = date.parse((String) session.getAttribute("RENTALDATE"));
                        Date cOut = date.parse((String) session.getAttribute("RETURNDATE"));
                        long d = (cOut.getTime() - cIn.getTime()) / (24 * 3600 * 1000);
                        if(d==0){
                            d=1;
                        }
                        orderDetaildto.setQuantity(1);
                        orderDetaildto.setStatus(true);
                        orderDetaildto.setPrice(cdao.getCar(carID).getPrice()*d);
                        cart.add(orderDetaildto);
                        request.setAttribute("ADDS", "Add car Successfull");
                        session.setAttribute("CART", cart);
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
