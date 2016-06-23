/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Beans.LiberacionPago;
import ConexionBD.CompararErrores;
import ConexionBD.IngresoAbd;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Rocio
 */
//@WebServlet(name = "servletLiberacionDePago", urlPatterns = {"/servletLiberacionDePago"})
public class servletLiberacionDePago extends HttpServlet implements Serializable{

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=ISO-8859-1");
        request.setCharacterEncoding("UTF8");
        PrintWriter out = response.getWriter();
        String referencia = request.getParameter("referencia");
        String usuario = request.getParameter("usuario");
        String contra = request.getParameter("contra");

        IngresoAbd bd = new IngresoAbd(usuario, contra);
        LiberacionPago lp = bd.liberarPago(referencia);
        
        try {

            if (!lp.getFicha().contains("NOVALID")) {
                if (bd.getErrorInsert().contentEquals("ninguno")) {
                    
                    int ceneval = bd.verificacionEnSeguimiento(lp.getIdAsp(), 5);

                    if (ceneval == 1 || ceneval == 0) {
                        out.print("ninguno" + ceneval);
                      
                    } else {
                        out.print(bd.getErrorCE() + ceneval);
                    }

                    HttpSession session = request.getSession(true);
                    session.setAttribute("lp", lp);
                } else {
                    out.print("Se ha producido un error, intente nuevamente: " + bd.getErrorInsert());
                }
            } else {
                out.print("Se produjo un error al generar la ficha.");
            }
        } catch (NullPointerException e) {
            if (!bd.getErrorInsert().contentEquals("ninguno")) {
                CompararErrores ce = new CompararErrores();
                if (bd.getError() == 100) {
                    out.print(bd.getError() + ": "+bd.getErrorInsert());
                } else {
                    ce.buscarError(bd.getError());
                }
                
                out.print(ce.getMensajeDeError());
            } else {
                out.print("No existe la referencia");
            }
        }
    }

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
