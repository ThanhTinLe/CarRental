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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@WebServlet(name = "UpdateController", urlPatterns = {"/UpdateController"})
public class UpdateController extends HttpServlet {

    private static final String SUCCESS = "cart.jsp";

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
            throws ServletException, IOException, ParseException {
        response.setContentType("text/html;charset=UTF-8");
        String url = SUCCESS;
        try {
            HttpSession session = request.getSession();
            String carID = request.getParameter("txtCarID");
            String detailID = request.getParameter("txtDetailID");
            boolean check = true;
            if (carID != null&& detailID!=null) {
                if (!carID.trim().isEmpty() && !detailID.trim().isEmpty()) {
                    String txtQuantity = request.getParameter("txtQuantity");
                    if (txtQuantity == null) {
                        check = false;
                    } else {
                        if (txtQuantity.trim().isEmpty()) {
                            check = false;
                        }
                    }
                    if(check){
                        OrderDTO cart=(OrderDTO) session.getAttribute("CART");
                        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
                        Date cIn = date.parse(cart.getHash().get(detailID).getRentalDate());
                        Date cOut = date.parse(cart.getHash().get(detailID).getReturnDate());
                        long d = (cOut.getTime() - cIn.getTime()) / (24 * 3600 * 1000);
                        if(d==0){
                            d=1;
                        }
                        CarDAO cdao=new CarDAO();
                        ArrayList<CarDTO> list=cdao.getCarNameToUpdate(txtQuantity, cart.getHash().get(detailID).getRentalDate(), cart.getHash().get(detailID).getReturnDate());
                        boolean checkE=true;
                        for (CarDTO carDTO : list) {
                            if(carDTO.getCarID().equals(carID)){
                                OrderDetailDTO detaildto=new OrderDetailDTO(detailID, cdao.getCar(carID), cart.getHash().get(detailID).getRentalDate(), cart.getHash().get(detailID).getReturnDate(), "", Integer.parseInt(txtQuantity), cdao.getCar(carID).getPrice()*d*Integer.parseInt(txtQuantity), true);
                                cart.update(detaildto);
                                session.setAttribute("CART", cart);
                                request.setAttribute("UPSUCCESS", "update Successfull");
                                checkE=false;
                            }
                        }
                        if(checkE){
                            request.setAttribute("quantityE", "Quantity not enough");
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
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(UpdateController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(UpdateController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
