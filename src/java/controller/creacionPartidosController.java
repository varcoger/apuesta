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
import modelo.ApuestaAmigo;
import modelo.ApuestaPartido;
import modelo.Equipos;
import modelo.Estadio;
import modelo.Grupo;
import modelo.Partidos;
import modelo.TipoApuesta;
import modelo.Usuario;
import org.json.JSONException;
import org.json.JSONObject;

@WebServlet(name = "creacionPartidosController", urlPatterns = {"/creacionPartidosController"})
public class creacionPartidosController extends HttpServlet {

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
                case "crearPartido":
                    html = crearPartido(request, con);
                    break;
                case "eliminar":
                    html = eliminar(request, con);
                    break;
                case "verApuestas":
                    html = verApuestas(request, con);
                    break;
                case "cambiarPorcApuesta":
                    html = cambiarPorcApuesta(request, con);
                    break;
                case "cerrarPartido":
                    html = cerrarPartido(request, con);
                    break;
                case "okCerrarPartido":
                    html = okCerrarPartido(request, con);
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
        } catch (ParseException ex) {
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
        JSONObject obj = new JSONObject();
        obj.put("Equipos", new Equipos(con).todos());
        obj.put("Partidos", new Partidos(con).todos());
        obj.put("Estadios", new Estadio(con).todos());
        obj.put("Grupos", new Grupo(con).todos());
        return obj.toString();
    }

    private String crearPartido(HttpServletRequest request, Conexion con) throws ParseException, SQLException, JSONException {
        String fecha = request.getParameter("fecha");
        String hora = request.getParameter("hora");
        int id1 = Integer.parseInt(request.getParameter("id1"));
        int id2 = Integer.parseInt(request.getParameter("id2"));
        int idEstadio = Integer.parseInt(request.getParameter("idEstadio"));
        int idGrupo = Integer.parseInt(request.getParameter("idGrupo"));
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy kk:mm");
        Date dfecha = formato.parse(fecha + " " + hora);
        Partidos p = new Partidos(0, dfecha, id1, id2, con.getUsuario().getId(), idEstadio, idGrupo, con);
        int id = p.insert();
        return new Partidos(con).buscarCompleto(id).toString();
    }

    private String eliminar(HttpServletRequest request, Conexion con) throws SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        Partidos p = new Partidos(con);
        p.setId(id);
        p.delete();
        return true + "";
    }

    private String verApuestas(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        int id = Integer.parseInt(request.getParameter("id"));
        JSONObject obj = new JSONObject();
        obj.put("partido", new TipoApuesta(con).todosPartido(id));
        obj.put("goles", new TipoApuesta(con).todosGoles(id));
        return obj.toString();
    }

    private String cambiarPorcApuesta(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        int idPartido = Integer.parseInt(request.getParameter("idPartido"));
        int idTipoApuesta = Integer.parseInt(request.getParameter("idTipoApuesta"));
        double monto = Double.parseDouble(request.getParameter("monto"));

        ApuestaPartido ap = new ApuestaPartido(0, idTipoApuesta, idPartido, monto, con);
        ap.insert();
        return true + "";
    }

    private String cerrarPartido(HttpServletRequest request, Conexion con) throws SQLException, JSONException, ParseException {
        int idPartido = Integer.parseInt(request.getParameter("idPartido"));
        Partidos p = new Partidos(con).buscar(idPartido);
        JSONObject json = new JSONObject();
        json.put("estado", p.getEstado());
        json.put("resultado", p.getResultado(idPartido));
        return json.toString();
    }

    private String okCerrarPartido(HttpServletRequest request, Conexion con) throws SQLException, JSONException, ParseException {
        int idPartido = Integer.parseInt(request.getParameter("idPartido"));
        // Pagar Apuestas
        Partidos p = new Partidos(con);
        JSONObject resultado = p.getResultado(idPartido);
        new ApuestaPartido(con).pagarApuesta(idPartido, resultado);
        new ApuestaAmigo(con).pagarApuesta(idPartido, resultado);
        p.cerrarPartido(idPartido);
        return "true";
    }
}
