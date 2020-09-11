package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAO {
    protected Connection con;
    protected PreparedStatement pst;
    protected ResultSet rs;

    public DAO(){
        this.con = ConexaoDAO.getInstance().getConnection();
        this.pst = null;
        this.rs = null;
    }

    protected void close(){
        if (rs != null) try { rs.close(); } catch (SQLException e) { /* ignored */ }
        if (pst != null) try { pst.close(); } catch (SQLException e) { /* ignored */ }
    }
}
