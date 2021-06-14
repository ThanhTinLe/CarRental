/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tinlt.controller;

import tinlt.dao.CarDAO;
import tinlt.dao.OrderDAO;
import dto.CarDTO;
import dto.OrderDTO;
import dto.OrderDetailDTO;
import dto.UserDTO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
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
@WebServlet(name = "CheckOutController", urlPatterns = {"/CheckOutController"})
public class CheckOutController extends HttpServlet {
    private static final String SUCCESS="SearchController";
    private static final String ERROR="cart.jsp";
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
            String txtPrice = request.getParameter("txtTotal");
            if (txtPrice != null) {
                HttpSession session = request.getSession();
                UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
                OrderDTO cart = (OrderDTO) session.getAttribute("CART");
                CarDAO cdao = new CarDAO();
                OrderDAO odao = new OrderDAO();
                long millis = System.currentTimeMillis();
                java.sql.Date date = new java.sql.Date(millis);
                if (cart.getDiscount() == null) {
                    cart.setDiscount(odao.getDiscount("KM0", date.toString()));
                } else {
                    if (cart.getDiscount().getDisID().isEmpty()) {
                        cart.setDiscount(odao.getDiscount("KM0", date.toString()));
                    }
                }
                cart.setUserID(user.getUserID());
                cart.setTotalPrice(Float.parseFloat(txtPrice));
                cart.setOrderDate(date.toString());
                Set<String> key = cart.getHash().keySet();
                ArrayList<String> listTrue = new ArrayList<>();
                boolean checkadd = true;
                for (String cid : key) {
                    String detailid = cid.split("-")[0];
                    cart = (OrderDTO) session.getAttribute("CART");
                    ArrayList<CarDTO> list = cdao.getCarNameToUpdate(cart.getHash().get(cid).getQuantity() + "", cart.getHash().get(cid).getRentalDate(), cart.getHash().get(cid).getReturnDate());
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getCarID().equals(detailid)) {
                            listTrue.add(cid);
                            OrderDetailDTO detaildto = cart.getHash().get(cid);
                            HashMap<String, OrderDetailDTO> hash = new HashMap<>();
                            hash.put(cid, detaildto);
                            OrderDTO cartdemo = new OrderDTO();
                            cartdemo.setUserID(user.getUserID());
                            cartdemo.setTotalPrice(Float.parseFloat(txtPrice));
                            cartdemo.setOrderDate(date.toString());
                            cartdemo.setDiscount(odao.getDiscount("KM0", date.toString()));
                            cartdemo.setHash(hash);
                            if (checkadd) {
                                odao.createOrder(cartdemo);
                                checkadd = false;
                            } else {
                                odao.createOrderDetail(detaildto, odao.getMaxOrderID(user.getUserID()) + "");
                            }
                        }
                    }
                }
                if (listTrue.size() == key.size()) {
                    odao.deleteOrder(odao.getMaxOrderID(user.getUserID()) + "");
                    odao.createOrder(cart);
                    request.setAttribute("CHECKOUT", "Rental successfull");
                    session.setAttribute("CART", null);
                    url = SUCCESS;
                } else {
                    String error = "";
                    odao.deleteOrder(odao.getMaxOrderID(user.getUserID()) + "");
                    for (String cid : key) {
                        boolean check = false;
                        for (int i = 0; i < listTrue.size(); i++) {
                            if (listTrue.get(i).equals(cid)) {
                                check = true;
                            }
                        }
                        if (!check) {
                            error += "-" + cart.getHash().get(cid).getCar().getCarName();
                        }
                    }
                    request.setAttribute("CHECKOUTE", error + " not enough");
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
