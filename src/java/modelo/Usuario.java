package modelo;

import conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import util.StringMD;

public class Usuario {

    private int id;
    private String usuario;
    private String password;
    private String nombres;
    private String apellidos;
    private String foto;
    private Date fechaNacimiento;
    private Date fechaCreacion;
    private Date fechaAnulacion;
    private String ci;
    private String telefono;
    private String direccion;
    private String sexo;
    private int idPerfil;
    private int idUsuarioCreador;
    private int idUsuarioRecomienda;
    private int idUsuarioAprueba;
    private int estado;
    private Conexion con;

    public static final int ESTADO_INACTIVO = 0;
    public static final int ESTADO_CREADO = 1;
    public static final int ESTADO_APROBADO = 2;

    public Usuario(Conexion con) {
        this.con = con;
    }

    public Usuario(String usuario, String password, String nombres, String apellidos, Date fechaNacimiento, String ci, String telefono, String direccion, String sexo, int idUsuarioCreador, int idUsuarioRecomienda, Conexion con) throws SQLException {
        this.con = con;
        this.id = 0;
        this.usuario = usuario;
        this.password = password;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.foto = "dist/img/user2-160x160.jpg";
        this.fechaNacimiento = fechaNacimiento;
        this.fechaCreacion = new Date();
        this.ci = ci;
        this.telefono = telefono;
        this.direccion = direccion;
        this.sexo = sexo;
        this.idPerfil = getApostador();
        this.idUsuarioCreador = idUsuarioCreador;
        this.idUsuarioRecomienda = idUsuarioRecomienda;
        this.idUsuarioAprueba = 0;
        this.estado = ESTADO_CREADO;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaAnulacion() {
        return fechaAnulacion;
    }

    public void setFechaAnulacion(Date fechaAnulacion) {
        this.fechaAnulacion = fechaAnulacion;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public int getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(int idPerfil) {
        this.idPerfil = idPerfil;
    }

    public int getIdUsuarioCreador() {
        return idUsuarioCreador;
    }

    public void setIdUsuarioCreador(int idUsuarioCreador) {
        this.idUsuarioCreador = idUsuarioCreador;
    }

    public int getIdUsuarioRecomienda() {
        return idUsuarioRecomienda;
    }

    public void setIdUsuarioRecomienda(int idUsuarioRecomienda) {
        this.idUsuarioRecomienda = idUsuarioRecomienda;
    }

    public int getIdUsuarioAprueba() {
        return idUsuarioAprueba;
    }

    public void setIdUsuarioAprueba(int idUsuarioAprueba) {
        this.idUsuarioAprueba = idUsuarioAprueba;
    }

    public int isEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public Conexion getCon() {
        return con;
    }

    public void setCon(Conexion con) {
        this.con = con;
    }

    ////////////////////////////////////////////////////////////////////////////
    public int insert() throws SQLException {
        fechaCreacion = new Date();
        String consulta = "INSERT INTO public.\"Usuario\"(\n"
                + "	\"nombres\", \"apellidos\", \"foto\", \"fechaNacimiento\", \"telefono\", \"sexo\", \"direccion\", \"fechaCreacion\", \"idUsuarioAprueba\", \"idUsuarioRecomienda\", \"estado\", \"fechaAnulacion\", \"usuario\", \"password\", \"idPerfil\", \"ci\", \"idUsuarioCreador\")\n"
                + "	VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        this.id = con.ejecutarInsert(consulta, "id", nombres, apellidos, foto, fechaNacimiento == null ? null : new java.sql.Date(fechaNacimiento.getTime()), telefono, sexo, direccion, fechaCreacion == null ? null : new java.sql.Date(fechaCreacion.getTime()), idUsuarioAprueba > 0 ? idUsuarioAprueba : null, idUsuarioRecomienda > 0 ? idUsuarioRecomienda : null, ESTADO_CREADO, fechaAnulacion == null ? null : new java.sql.Date(fechaAnulacion.getTime()), usuario, password, idPerfil > 0 ? idPerfil : null, ci, idUsuarioCreador > 0 ? idUsuarioCreador : null);
        return this.id;
    }

    public void delete() throws SQLException {
        String consulta = "DELETE FROM public.\"Usuario\"\n"
                + "	WHERE \"id\"=?;";
        con.ejecutarSentencia(consulta, id);
    }

    public JSONArray todos() throws SQLException, JSONException {
        String consulta = "SELECT * FROM public.\"Usuario\"\n"
                + "ORDER BY \"usuario\" ASC ";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        java.sql.Date aux;
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("usuario", rs.getString("usuario"));
            obj.put("nombres", rs.getString("nombres"));
            obj.put("apellidos", rs.getString("apellidos"));
            obj.put("foto", rs.getString("foto"));
            aux = rs.getDate("fechaNacimiento");
            obj.put("fechaNacimiento", aux == null ? "" : f.format(aux));
            aux = rs.getDate("fechaCreacion");
            obj.put("fechaCreacion", aux == null ? "" : f.format(aux));
            aux = rs.getDate("fechaAnulacion");
            obj.put("fechaAnulacion", aux == null ? "" : f.format(aux));
            obj.put("ci", rs.getString("ci"));
            obj.put("telefono", rs.getString("telefono"));
            obj.put("direccion", rs.getString("direccion"));
            obj.put("sexo", rs.getString("sexo"));
            obj.put("idPerfil", rs.getInt("idPerfil"));
            obj.put("idUsuarioCreador", rs.getInt("idUsuarioCreador"));
            obj.put("idUsuarioRecomienda", rs.getInt("idUsuarioRecomienda"));
            obj.put("idUsuarioAprueba", rs.getInt("idUsuarioAprueba"));
            obj.put("estado", rs.getInt("estado"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }

    public Usuario buscar(int id) throws SQLException {
        String consulta = "SELECT * FROM public.\"Usuario\"\n"
                + "	WHERE \"id\"=?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        Usuario u = new Usuario(con);
        if (rs.next()) {
            u.setId(rs.getInt("id"));
            u.setUsuario(rs.getString("usuario"));
            u.setNombres(rs.getString("nombres"));
            u.setApellidos(rs.getString("apellidos"));
            u.setFoto(rs.getString("foto"));
            u.setFechaNacimiento(rs.getDate("fechaNacimiento"));
            u.setFechaCreacion(rs.getDate("fechaCreacion"));
            u.setFechaAnulacion(rs.getDate("fechaAnulacion"));
            u.setCi(rs.getString("ci"));
            u.setTelefono(rs.getString("telefono"));
            u.setDireccion(rs.getString("direccion"));
            u.setSexo(rs.getString("sexo"));
            u.setIdPerfil(rs.getInt("idPerfil"));
            u.setIdUsuarioCreador(rs.getInt("idUsuarioCreador"));
            u.setIdUsuarioRecomienda(rs.getInt("idUsuarioRecomienda"));
            u.setIdUsuarioAprueba(rs.getInt("idUsuarioAprueba"));
            u.setEstado(rs.getInt("estado"));
            return u;
        }
        return null;
    }

    public JSONObject toJSONObject() throws SQLException, JSONException {
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("usuario", usuario);
        obj.put("nombres", nombres);
        obj.put("apellidos", apellidos);
        obj.put("foto", foto);
        obj.put("fechaNacimiento", fechaNacimiento == null ? "" : f.format(fechaNacimiento));
        obj.put("fechaCreacion", fechaCreacion == null ? "" : f.format(fechaCreacion));
        obj.put("fechaAnulacion", fechaAnulacion == null ? "" : f.format(fechaAnulacion));
        obj.put("ci", ci);
        obj.put("telefono", telefono);
        obj.put("direccion", direccion);
        obj.put("sexo", sexo);
        obj.put("idPerfil", idPerfil);
        obj.put("idUsuarioCreador", idUsuarioCreador);
        obj.put("idUsuarioRecomienda", idUsuarioRecomienda);
        obj.put("idUsuarioAprueba", idUsuarioAprueba);
        obj.put("estado", estado);
        return obj;
    }

    ////////////////////////////////////////////////////////////////////////////
    public Usuario Buscar(String usr, String pass) throws SQLException {
        pass = StringMD.getStringMessageDigest(pass, StringMD.SHA512);
        String consulta = "select * from public.\"Usuario\" where \"usuario\" = (?) and \"password\" = (?) and \"estado\"=" + ESTADO_APROBADO;
        PreparedStatement ps = con.statamet(consulta);
        ps.setString(1, usr);
        ps.setString(2, pass);
        ResultSet rs = ps.executeQuery();
        List<Usuario> lista = Cargar(rs);
        if (lista.size() > 0) {
            return lista.get(0);
        } else {
            return null;
        }
    }
    public Usuario Buscar(String usr) throws SQLException {        
        String consulta = "select * from public.\"Usuario\" where \"usuario\" = (?) and \"estado\"=" + ESTADO_APROBADO;
        PreparedStatement ps = con.statamet(consulta);
        ps.setString(1, usr);        
        ResultSet rs = ps.executeQuery();
        List<Usuario> lista = Cargar(rs);
        rs.close();
        ps.close();        
        if (lista.size() > 0) {
            return lista.get(0);
        } else {
            return null;
        }
    }

    public List<Usuario> Cargar(ResultSet rs) throws SQLException {
        List<Usuario> lista = new ArrayList<Usuario>();
        while (rs.next()) {
            Usuario u = new Usuario(con);
            u.setId(rs.getInt("id"));
            u.setUsuario(rs.getString("usuario"));
            u.setNombres(rs.getString("nombres"));
            u.setApellidos(rs.getString("apellidos"));
            u.setFoto(rs.getString("foto"));
            u.setFechaNacimiento(rs.getDate("fechaNacimiento"));
            u.setFechaCreacion(rs.getDate("fechaCreacion"));
            u.setFechaAnulacion(rs.getDate("fechaAnulacion"));
            u.setCi(rs.getString("ci"));
            u.setTelefono(rs.getString("telefono"));
            u.setDireccion(rs.getString("direccion"));
            u.setSexo(rs.getString("sexo"));
            u.setIdPerfil(rs.getInt("idPerfil"));
            u.setIdUsuarioCreador(rs.getInt("idUsuarioCreador"));
            u.setIdUsuarioRecomienda(rs.getInt("idUsuarioRecomienda"));
            u.setIdUsuarioAprueba(rs.getInt("idUsuarioAprueba"));
            u.setEstado(rs.getInt("estado"));
            lista.add(u);
        }
        return lista;
    }

    public JSONArray todosConPerfil() throws SQLException, JSONException {
        String consulta = "SELECT \"Usuario\".*, \"Perfil\".\"nombre\" AS perfil\n"
                + "     FROM public.\"Usuario\"\n"
                + "          LEFT JOIN public.\"Perfil\" ON \"Usuario\".\"idPerfil\" = \"Perfil\".\"id\"\n"
                + "ORDER BY \"Usuario\".\"usuario\" ASC ";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        java.sql.Date aux;
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("usuario", rs.getString("usuario"));
            obj.put("nombres", rs.getString("nombres"));
            obj.put("apellidos", rs.getString("apellidos"));
            obj.put("foto", rs.getString("foto"));
            aux = rs.getDate("fechaNacimiento");
            obj.put("fechaNacimiento", aux == null ? "" : f.format(aux));
            aux = rs.getDate("fechaCreacion");
            obj.put("fechaCreacion", aux == null ? "" : f.format(aux));
            aux = rs.getDate("fechaAnulacion");
            obj.put("fechaAnulacion", aux == null ? "" : f.format(aux));
            obj.put("ci", rs.getString("ci"));
            obj.put("telefono", rs.getString("telefono"));
            obj.put("direccion", rs.getString("direccion"));
            obj.put("sexo", rs.getString("sexo"));
            obj.put("idPerfil", rs.getInt("idPerfil"));
            obj.put("perfil", rs.getString("perfil"));
            obj.put("idUsuarioCreador", rs.getInt("idUsuarioCreador"));
            obj.put("idUsuarioRecomienda", rs.getInt("idUsuarioRecomienda"));
            obj.put("idUsuarioAprueba", rs.getInt("idUsuarioAprueba"));
            obj.put("estado", rs.getInt("estado"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }

    public void updateDatos() throws SQLException {
        java.sql.Date fn = fechaNacimiento == null ? null : new java.sql.Date(fechaNacimiento.getTime());
        String consulta = "UPDATE public.\"Usuario\"\n"
                + "	SET \"nombres\"=?,\"apellidos\"=?,\"fechaNacimiento\"=?,\"sexo\"=?,\"idPerfil\"=?,\"telefono\"=?,\"direccion\"=?\n"
                + "	WHERE \"id\"=?;";
        con.ejecutarSentencia(consulta, nombres, apellidos, fn, sexo, idPerfil, telefono, direccion, id);
    }

    public void updateContrasena() throws SQLException {
        String consulta = "UPDATE public.\"Usuario\"\n"
                + "	SET \"password\"=?\n"
                + "	WHERE \"id\"=?;";
        con.ejecutarSentencia(consulta, password, id);
    }

    public void updateEstado() throws SQLException {
        String consulta = "UPDATE public.\"Usuario\"\n"
                + "	SET \"estado\"=?\n"
                + "	WHERE \"id\"=?;";
        con.ejecutarSentencia(consulta, estado, id);
    }

    public void updateIdPerfil() throws SQLException {
        String consulta = "UPDATE public.\"Usuario\"\n"
                + "	SET \"idPerfil\"=?\n"
                + "	WHERE \"id\"=?;";
        con.ejecutarSentencia(consulta, idPerfil, id);
    }

    public void updateFoto() throws SQLException {
        String consulta = "UPDATE public.\"Usuario\"\n"
                + "	SET \"foto\"=?\n"
                + "	WHERE \"id\"=?;";
        con.ejecutarSentencia(consulta, estado, id);
    }

    public void updateFoto(String foto) throws SQLException {
        String consulta = "UPDATE public.\"Usuario\"\n"
                + "	SET \"foto\"=?\n"
                + "	WHERE \"id\"=?;";
        con.ejecutarSentencia(consulta, foto, id);
    }

    public String getNombreCompleto() {
        String res = "";
        if (nombres != null) {
            res += nombres + " ";
        }
        if (apellidos != null) {
            res += apellidos;
        }
        return res;
    }

    ////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) throws SQLException {
        Conexion con = Conexion.getConeccion();
        Usuario u = new Usuario(con);
        if (u.Buscar("apuesta", "apuesta") == null) {
            System.out.println("false");
        } else {
            System.out.println("true");
        }

//        Date dia = new Date();
//        java.sql.Date name = null;
//        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
//        System.out.println(f.format(name));
    }

    public JSONObject getPerfil(int id) throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "\"public\".\"Usuario\".ci,\n"
                + "\"public\".\"Usuario\".nombres,\n"
                + "\"public\".\"Usuario\".apellidos,\n"
                + "\"public\".\"Usuario\".foto,\n"
                + "to_char(\"public\".\"Usuario\".\"fechaNacimiento\",'DD/MM/YYYY') as fechaNacimiento,\n"
                + "\"public\".\"Usuario\".telefono,\n"
                + "\"public\".\"Usuario\".sexo,\n"
                + "\"public\".\"Usuario\".direccion,\n"
                + "\"public\".\"Perfil\".nombre as perfil\n"
                + "FROM\n"
                + "\"public\".\"Usuario\"\n"
                + "INNER JOIN \"public\".\"Perfil\" ON \"public\".\"Usuario\".\"idPerfil\" = \"public\".\"Perfil\".\"id\"\n"
                + "WHERE\n"
                + "\"public\".\"Usuario\".\"id\" = " + id;
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONObject obj = new JSONObject();
        if (rs.next()) {
            Billetera b = new Billetera(con);
            obj.put("ci", rs.getString("ci"));
            obj.put("nombres", rs.getString("nombres"));
            obj.put("apellidos", rs.getString("apellidos"));
            obj.put("foto", rs.getString("foto"));
            obj.put("fechaNacimiento", rs.getString("fechaNacimiento"));
            obj.put("telefono", rs.getString("telefono"));
            obj.put("sexo", rs.getString("sexo"));
            obj.put("direccion", rs.getString("direccion"));
            obj.put("perfil", rs.getString("perfil"));
            obj.put("balance", b.getBalanceUsuario(id));
            obj.put("prestamos", new Prestamo(con).getPrestamoUsuarioPerfil(id));
            obj.put("transacciones", b.getTransaccionesUsuariosPerfil(id));
        }
        rs.close();
        ps.close();
        return obj;
    }

    private int getApostador() throws SQLException {
        String consulta = "SELECT\n"
                + "\"public\".\"Perfil\".\"id\"\n"
                + "FROM\n"
                + "\"public\".\"Perfil\"\n"
                + "WHERE\n"
                + "\"public\".\"Perfil\".\"porDefecto\" = true";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        int id = 0;
        if (rs.next()) {
            id = rs.getInt("id");
        }
        rs.close();
        ps.close();
        return id;
    }

    public JSONArray todosRelacionadores() throws SQLException, JSONException {
        String consulta = "SELECT \"Usuario\".\"id\",\n "
                + "               \"Usuario\".\"nombres\" || ' ' || \"Usuario\".\"apellidos\" AS nombre\n"
                + "     FROM public.\"Usuario\"\n"
                + "          LEFT JOIN public.\"Perfil\" ON \"Usuario\".\"idPerfil\" = \"Perfil\".\"id\"\n"
                + "     WHERE \"Perfil\".\"relacionador\" = true\n"
                + "       AND \"Usuario\".\"estado\" = " + ESTADO_APROBADO + "\n"
                + "ORDER BY \"Usuario\".\"nombres\" ASC, \"Usuario\".\"apellidos\" ASC ";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        java.sql.Date aux;
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("nombre", rs.getString("nombre"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }

    public JSONArray todosCasa() throws SQLException, JSONException {
        String consulta = "SELECT \"Usuario\".\"id\",\n "
                + "               \"Usuario\".\"nombres\" || ' ' || \"Usuario\".\"apellidos\" AS nombre\n"
                + "     FROM public.\"Usuario\"\n"
                + "          LEFT JOIN public.\"Perfil\" ON \"Usuario\".\"idPerfil\" = \"Perfil\".\"id\"\n"
                + "     WHERE \"Perfil\".\"casa\" = true\n"
                + "       AND \"Usuario\".\"estado\" = " + ESTADO_APROBADO + "\n"
                + "ORDER BY \"Usuario\".\"nombres\" ASC, \"Usuario\".\"apellidos\" ASC ";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        java.sql.Date aux;
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("nombre", rs.getString("nombre"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }

    public int getIdCasa() throws SQLException, JSONException {
        String consulta = "SELECT \"Usuario\".\"id\"\n "
                + "     FROM public.\"Usuario\"\n"
                + "          LEFT JOIN public.\"Perfil\" ON \"Usuario\".\"idPerfil\" = \"Perfil\".\"id\"\n"
                + "     WHERE \"Perfil\".\"casa\" = true\n"
                + "       AND \"Usuario\".\"estado\" = " + ESTADO_APROBADO + "\n";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        int idUsuario = rs.next() ? rs.getInt("id") : 0;
        rs.close();
        ps.close();
        return idUsuario;
    }

    public JSONArray todosNoCasaTraspaso(int idUsuario) throws SQLException, JSONException {
        String consulta = "SELECT \"Usuario\".\"id\",\n "
                + "               \"Usuario\".\"usuario\"\n"
                + "     FROM public.\"Usuario\"\n"
                + "          LEFT JOIN public.\"Perfil\" ON \"Usuario\".\"idPerfil\" = \"Perfil\".\"id\"\n"
                + "     WHERE \"Usuario\".\"estado\" = ?\n"
                + "       AND (\"Perfil\".\"casa\" = false OR \"Perfil\".\"casa\" IS NULL)\n"
                + "       AND \"Usuario\".\"id\" <> ?\n"
                + "ORDER BY \"Usuario\".\"usuario\";";
        PreparedStatement ps = con.statametObject(consulta, ESTADO_APROBADO, idUsuario);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("usuario", rs.getString("usuario"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }

    public JSONArray todosConCredito() throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "\"public\".\"Usuario\".id,\n"
                + "\"public\".\"Usuario\".ci,\n"
                + "\"public\".\"Usuario\".\"nombres\" || ' ' || \"Usuario\".\"apellidos\" AS nombre,\n"
                + "\"public\".\"Usuario\".usuario\n"
                + "FROM\n"
                + "\"public\".\"Usuario\"\n"
                + "ORDER BY \"Usuario\".\"nombres\",\n"
                + "         \"Usuario\".\"apellidos\"\n";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        Billetera b = new Billetera(con);
        double credito;
        while (rs.next()) {
            credito = b.getCreditoDisponible(rs.getInt("id"));
            if (credito > 0) {
                obj = new JSONObject();
                obj.put("id", rs.getInt("id"));
                obj.put("ci", rs.getString("ci"));
                obj.put("nombre", rs.getString("nombre"));
                obj.put("usuario", rs.getString("usuario"));
                obj.put("credito", credito);
                json.put(obj);
            }
        }
        rs.close();
        ps.close();
        return json;
    }

    public JSONArray todosConEfectivo() throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "\"public\".\"Usuario\".id,\n"
                + "\"public\".\"Usuario\".ci,\n"
                + "\"public\".\"Usuario\".\"nombres\" || ' ' || \"Usuario\".\"apellidos\" AS nombre,\n"
                + "\"public\".\"Usuario\".usuario\n"
                + "FROM\n"
                + "\"public\".\"Usuario\"\n"
                + "ORDER BY \"Usuario\".\"nombres\",\n"
                + "         \"Usuario\".\"apellidos\"\n";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        PagoEfectivo b = new PagoEfectivo(con);
        double efectivo;
        while (rs.next()) {
            efectivo = b.getEfectivoDisponible(rs.getInt("id"));
//            if (efectivo > 0) {
                obj = new JSONObject();
                obj.put("id", rs.getInt("id"));
                obj.put("ci", rs.getString("ci"));
                obj.put("nombre", rs.getString("nombre"));
                obj.put("usuario", rs.getString("usuario"));
                obj.put("efectivo", efectivo);
                json.put(obj);
//            }
        }
        rs.close();
        ps.close();
        return json;
    }

}
