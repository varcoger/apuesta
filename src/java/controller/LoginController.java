package controller;

import conexion.Conexion;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.Usuario;
import util.SisEventos;

@WebServlet(name = "LoginController", urlPatterns = {"/LoginController"})
public class LoginController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SisEventos ev = new SisEventos();
        if (request.getSession().getAttribute("usr") != null) {
            Usuario u = (Usuario) request.getSession().getAttribute("usr");
            Conexion con = u.getCon();
            if (con != null && con.isConectado()) {
                con.close();
            }
        }
        request.getSession().setAttribute("usr", null);

        String usr = ev.decodeUTF8(request.getParameter("username"));
        String pass = ev.decodeUTF8(request.getParameter("password"));
        Conexion con = new Conexion();

        try {
            if (con.isConectado()) {
                Usuario usrs = new Usuario(con);
                usrs = usrs.Buscar(usr, pass);
                if (usr == null) {
                    response.sendRedirect("index.html");
                } else {
                    con.setUsuario(usrs);
                    usrs.setPassword("");
                    request.getSession().setAttribute("usr", usrs);
                    response.sendRedirect("ingreso.html");
                }
            } else {
                response.sendRedirect("index.html");
            }
        } catch (Exception e) {
            con.close();
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Usuario=" + usr + " INGRESÓ MAL INGRESAR SU CONTRASEÑA EL DÍA " + new Date().toString(), "Usuario=" + usr + " INGRESÓ MAL INGRESAR SU CONTRASEÑA EL DÍA " + new Date().toString());
            response.sendRedirect("index.html");
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
