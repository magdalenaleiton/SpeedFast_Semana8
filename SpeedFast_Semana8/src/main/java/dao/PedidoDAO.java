package dao;

import conexion.ConexionDB;
import modelo.Pedido;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** CRUD de pedidos y filtros opcionales. */
public class PedidoDAO {
    public void create(Pedido p) throws SQLException {
        String sql = "INSERT INTO pedidos(direccion,tipo,estado) VALUES (?,?,?)";
        try (Connection c = ConexionDB.conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1,p.getDireccion()); ps.setString(2,p.getTipo()); ps.setString(3,p.getEstado()); ps.executeUpdate();
        }
    }
    public List<Pedido> readAll() throws SQLException { return readFiltered(null, null); }
    public List<Pedido> readFiltered(String estado, String tipo) throws SQLException {
        List<Pedido> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT id,direccion,tipo,estado FROM pedidos WHERE 1=1");
        if (estado != null && !estado.isBlank()) sql.append(" AND estado=?");
        if (tipo != null && !tipo.isBlank()) sql.append(" AND tipo=?");
        sql.append(" ORDER BY id");
        try (Connection c=ConexionDB.conectar(); PreparedStatement ps=c.prepareStatement(sql.toString())) {
            int i=1; if(estado!=null&&!estado.isBlank()) ps.setString(i++,estado); if(tipo!=null&&!tipo.isBlank()) ps.setString(i++,tipo);
            try(ResultSet rs=ps.executeQuery()) { while(rs.next()) lista.add(new Pedido(rs.getInt("id"),rs.getString("direccion"),rs.getString("tipo"),rs.getString("estado"))); }
        }
        return lista;
    }
    public void update(Pedido p) throws SQLException {
        String sql="UPDATE pedidos SET direccion=?, tipo=?, estado=? WHERE id=?";
        try(Connection c=ConexionDB.conectar();PreparedStatement ps=c.prepareStatement(sql)){ps.setString(1,p.getDireccion());ps.setString(2,p.getTipo());ps.setString(3,p.getEstado());ps.setInt(4,p.getId());ps.executeUpdate();}
    }
    public void delete(int id) throws SQLException {
        String sql="DELETE FROM pedidos WHERE id=?";
        try(Connection c=ConexionDB.conectar();PreparedStatement ps=c.prepareStatement(sql)){ps.setInt(1,id);ps.executeUpdate();}
    }
}
