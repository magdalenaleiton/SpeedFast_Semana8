package dao;

import conexion.ConexionDB;
import modelo.Entrega;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** CRUD de entregas. */
public class EntregaDAO {
    public void create(Entrega e) throws SQLException {
        String sql="INSERT INTO entregas(id_pedido,id_repartidor,fecha,hora) VALUES (?,?,?,?)";
        try(Connection c=ConexionDB.conectar();PreparedStatement ps=c.prepareStatement(sql)){ps.setInt(1,e.getIdPedido());ps.setInt(2,e.getIdRepartidor());ps.setDate(3,Date.valueOf(e.getFecha()));ps.setTime(4,Time.valueOf(e.getHora()));ps.executeUpdate();}
    }
    public List<Entrega> readAll() throws SQLException {
        List<Entrega> lista=new ArrayList<>(); String sql="SELECT id,id_pedido,id_repartidor,fecha,hora FROM entregas ORDER BY id";
        try(Connection c=ConexionDB.conectar();PreparedStatement ps=c.prepareStatement(sql);ResultSet rs=ps.executeQuery()){
            while(rs.next()) lista.add(new Entrega(rs.getInt("id"),rs.getInt("id_pedido"),rs.getInt("id_repartidor"),rs.getDate("fecha").toLocalDate(),rs.getTime("hora").toLocalTime()));
        } return lista;
    }
    public void update(Entrega e) throws SQLException {
        String sql="UPDATE entregas SET id_pedido=?, id_repartidor=?, fecha=?, hora=? WHERE id=?";
        try(Connection c=ConexionDB.conectar();PreparedStatement ps=c.prepareStatement(sql)){ps.setInt(1,e.getIdPedido());ps.setInt(2,e.getIdRepartidor());ps.setDate(3,Date.valueOf(e.getFecha()));ps.setTime(4,Time.valueOf(e.getHora()));ps.setInt(5,e.getId());ps.executeUpdate();}
    }
    public void delete(int id) throws SQLException {
        String sql="DELETE FROM entregas WHERE id=?";
        try(Connection c=ConexionDB.conectar();PreparedStatement ps=c.prepareStatement(sql)){ps.setInt(1,id);ps.executeUpdate();}
    }
}
