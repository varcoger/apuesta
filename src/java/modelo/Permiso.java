package modelo;

import conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Permiso {

    private int id;
    private int id_perfil;
    private int id_sub_menu;
    private boolean alta;
    private boolean baja;
    private boolean modificacion;
    private Conexion con;

    public Permiso(Conexion con) {
        this.con = con;
    }

    public Permiso(int id, int id_perfil, int id_sub_menu, boolean alta, boolean baja, boolean modificacion) {
        this.id = id;
        this.id_perfil = id_perfil;
        this.id_sub_menu = id_sub_menu;
        this.alta = alta;
        this.baja = baja;
        this.modificacion = modificacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_perfil() {
        return id_perfil;
    }

    public void setId_perfil(int id_perfil) {
        this.id_perfil = id_perfil;
    }

    public int getId_sub_menu() {
        return id_sub_menu;
    }

    public void setId_sub_menu(int id_sub_menu) {
        this.id_sub_menu = id_sub_menu;
    }

    public boolean isAlta() {
        return alta;
    }

    public void setAlta(boolean alta) {
        this.alta = alta;
    }

    public boolean isBaja() {
        return baja;
    }

    public void setBaja(boolean baja) {
        this.baja = baja;
    }

    public boolean isModificacion() {
        return modificacion;
    }

    public void setModificacion(boolean modificacion) {
        this.modificacion = modificacion;
    }

    public Conexion getCon() {
        return con;
    }

    public void setCon(Conexion con) {
        this.con = con;
    }

    ////////////////////////////////////////////////////////////////////////////
    public int insert() throws SQLException {
        String consulta = "INSERT INTO public.\"Permiso\"(\n"
                + "	\"id_perfil\", \"id_sub_menu\", \"alta\", \"baja\", \"modificacion\")\n"
                + "	VALUES (?,?,?,?,?)";
        int id = con.EjecutarInsert(consulta, "id", id_perfil, id_sub_menu, alta, baja, modificacion);
        this.id = id;
        return id;
    }

    public void update() throws SQLException {
        String consulta = "UPDATE public.\"Permiso\"\n"
                + "	SET \"id_perfil\"=?, \"id_sub_menu\"=?, \"alta\"=?, \"baja\"=?, \"modificacion\"=?\n"
                + "	WHERE \"id\"=?;";
        con.EjecutarSentencia(consulta, id_perfil, id_sub_menu, alta, baja, modificacion, id);
    }

    public void delete() throws SQLException {
        String consulta = "DELETE FROM public.\"Permiso\"\n"
                + "	WHERE \"id\"=?;";
        con.EjecutarSentencia(consulta, id);
    }

    public JSONArray todos() throws SQLException, JSONException {
        String consulta = "SELECT * FROM public.\"Permiso\"\n"
                + "ORDER BY \"id\" ASC ";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("id_perfil", rs.getInt("id_perfil"));
            obj.put("id_sub_menu", rs.getInt("id_sub_menu"));
            obj.put("alta", rs.getBoolean("alta"));
            obj.put("baja", rs.getBoolean("baja"));
            obj.put("modificacion", rs.getBoolean("modificacion"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }

    public Permiso buscar(int id) throws SQLException {
        String consulta = "SELECT * FROM public.\"Permiso\"\n"
                + "	WHERE \"id\"=?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        Permiso p = new Permiso(con);
        if (rs.next()) {
            p.setId(rs.getInt("id"));
            p.setId_perfil(rs.getInt("id_perfil"));
            p.setId_sub_menu(rs.getInt("id_sub_menu"));
            p.setAlta(rs.getBoolean("alta"));
            p.setBaja(rs.getBoolean("baja"));
            p.setModificacion(rs.getBoolean("modificacion"));
            return p;
        }
        return null;
    }

    public JSONObject toJSONObject() throws SQLException, JSONException {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("id_perfil", id_perfil);
        obj.put("id_sub_menu", id_sub_menu);
        obj.put("alta", alta);
        obj.put("baja", baja);
        obj.put("modificacion", modificacion);
        return obj;
    }

    ////////////////////////////////////////////////////////////////////////////
    public JSONArray todosXCargo(int id_perfil) throws SQLException, JSONException {
        String consulta = "SELECT \"Permiso\".*\n"
                + "	FROM public.\"Permiso\"\n"
                + "     WHERE \"Permiso\".\"id_perfil\" = ?";
        PreparedStatement ps = con.statametObject(consulta, id_perfil);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("id_perfil", rs.getInt("id_perfil"));
            obj.put("id_sub_menu", rs.getInt("id_sub_menu"));
            obj.put("alta", rs.getBoolean("alta"));
            obj.put("baja", rs.getBoolean("baja"));
            obj.put("modificacion", rs.getBoolean("modificacion"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }

    public void delete(int id_perfil, int id_sub_menu) throws SQLException {
        String consulta = "DELETE FROM public.\"Permiso\"\n"
                + "	WHERE \"Permiso\".\"id_perfil\"=?\n"
                + "           AND \"Permiso\".\"id_sub_menu\"=?;";
        con.EjecutarSentencia(consulta, id_perfil, id_sub_menu);
    }
}