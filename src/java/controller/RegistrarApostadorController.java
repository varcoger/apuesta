package controller;

import conexion.Conexion;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.Perfil;
import modelo.Usuario;
import org.json.JSONException;
import org.json.JSONObject;
import util.SisEventos;
import util.StringMD;

@WebServlet(name = "RegistrarApostadorController", urlPatterns = {"/RegistrarApostadorController"})
public class RegistrarApostadorController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");
        Usuario usuario = ((Usuario) request.getSession().getAttribute("usr"));
        if (usuario == null) {
            response.getWriter().write("false");
            return;
        }
        Conexion con = usuario.getCon();
        if (con == null) {
            response.getWriter().write("false");
            return;
        }
        con.transacction();
        try {
            String html = "";
            String evento = request.getParameter("evento");
            switch (evento) {
                case "init":
                    html = init(request, con);
                    break;
                case "guardarUsuario":
                    html = guardarUsuario(request, con);
                    break;
                case "eliminarUsuario":
                    html = eliminarUsuario(request, con);
                    break;
                case "cambiarEstadoUsuario":
                    html = cambiarEstadoUsuario(request, con);
                    break;
                case "datosUsuario":
                    html = datosUsuario(request, con);
                    break;
            }
            con.commit();
            response.getWriter().write(html);
        } catch (SQLException ex) {
            con.error(this, ex);
            response.getWriter().write("false");
        } catch (JSONException ex) {
            con.error(this, ex);
            response.getWriter().write("false");
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

    private String init(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        JSONObject json = new JSONObject();
        json.put("perfiles", new Perfil(con).todos());
        json.put("usuarios", new Usuario(con).todosConPerfil());
        return json.toString();
    }

    private String guardarUsuario(HttpServletRequest request, Conexion con) throws SQLException, JSONException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        int accion = Integer.parseInt(request.getParameter("accion"));
        String usuario = SisEventos.decodeUTF8(request.getParameter("usuario"));
        String contrasena = SisEventos.decodeUTF8(request.getParameter("contrasena"));        
        int perfil = Integer.parseInt(request.getParameter("perfil"));
        String ci = SisEventos.decodeUTF8(request.getParameter("ci"));        
        String nombres = SisEventos.decodeUTF8(request.getParameter("nombres"));        
        String apellidos = SisEventos.decodeUTF8(request.getParameter("apellidos"));                
        String fechaNacimiento = request.getParameter("fechaNacimiento");
        Date fechaNac;
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        try {
            fechaNac = f.parse(fechaNacimiento);
        } catch (ParseException e) {
            fechaNac = null;
        }
        String sexo = request.getParameter("sexo");
        Usuario u;
        String pass;
        switch (accion) {
            case 0: // crear
//                pass = StringMD.getStringMessageDigest(contrasena, StringMD.SHA512);
//                u = new Usuario(0, usuario, pass, nombres, apellidos, fecha_nac, new Date(), ci, sexo, cargo, con.getUsuario().getId(), true);
//                u.setCon(con);
//                u.insert();
//                return u.toJSONObject().toString();
                return "true";
            case 1: // modificar
                u = new Usuario(con).buscar(id);
                if (u == null) {
                    return "false";
                }
                u.setIdPerfil(perfil);
                u.setNombres(nombres);
                u.setApellidos(apellidos);
                u.setFechaNacimiento(fechaNac);
                u.setSexo(sexo);
                u.updateDatos();
                return u.toJSONObject().toString();
            default: // cambiar contrasena
                u = new Usuario(con).buscar(id);
                if (u == null) {
                    return "false";
                }
                pass = StringMD.getStringMessageDigest(contrasena, StringMD.SHA512);
                u.setPassword(pass);
                u.updateContrasena();
                return u.toJSONObject().toString();
        }
    }

    private String datosUsuario(HttpServletRequest request, Conexion con) throws SQLException, JSONException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        Usuario u = new Usuario(con).buscar(id);
        if (u == null) {
            return "false";
        }
        return u.toJSONObject().toString();
    }

    private String eliminarUsuario(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        int id = Integer.parseInt(request.getParameter("id"));
        Usuario u = new Usuario(con);
        u.setId(id);
        u.delete();
        return "true";
    }

    private String cambiarEstadoUsuario(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        int estado = Integer.parseInt(request.getParameter("estado"));
        int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
        Usuario u = new Usuario(con).buscar(idUsuario);
        if (u == null) {
            return "false";
        }
        u.setEstado(estado);
        u.updateEstado();
        return "true";
    }

}
