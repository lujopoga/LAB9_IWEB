package com.example.lab9_base.Dao;

import com.example.lab9_base.Bean.Partido;
import com.example.lab9_base.Bean.Seleccion;
import com.example.lab9_base.Bean.Arbitro;
import com.example.lab9_base.Bean.Estadio;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DaoPartidos extends DaoBase{
    public ArrayList<Partido> listaDePartidos() {

        ArrayList<Partido> partidos = new ArrayList<>();

        /*
        Inserte su código aquí*/
        String sql = "SELECT p.idPartido, p.fecha, p.numeroJornada, "
                + "sl.idSeleccion AS seleccionLocalId, sl.nombre AS seleccionLocalNombre, "
                + "sv.idSeleccion AS seleccionVisitanteId, sv.nombre AS seleccionVisitanteNombre, "
                + "e.nombre AS estadioNombre, "
                + "a.idArbitro, a.nombre AS arbitroNombre "
                + "FROM partido p "
                + "JOIN seleccion sl ON p.seleccionLocal = sl.idSeleccion "
                + "JOIN seleccion sv ON p.seleccionVisitante = sv.idSeleccion "
                + "JOIN estadio e ON sl.estadio_idEstadio = e.idEstadio "
                + "JOIN arbitro a ON p.arbitro = a.idArbitro";

        try (Connection connection = this.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Partido partido = new Partido();
                partido.setIdPartido(rs.getInt("idPartido"));
                partido.setNumeroJornada(rs.getInt("numeroJornada"));
                partido.setFecha(rs.getString("fecha"));

                Seleccion seleccionLocal = new Seleccion();
                seleccionLocal.setIdSeleccion(rs.getInt("seleccionLocalId"));
                seleccionLocal.setNombre(rs.getString("seleccionLocalNombre"));
                partido.setSeleccionLocal(seleccionLocal);

                Seleccion seleccionVisitante = new Seleccion();
                seleccionVisitante.setIdSeleccion(rs.getInt("seleccionVisitanteId"));
                seleccionVisitante.setNombre(rs.getString("seleccionVisitanteNombre"));
                partido.setSeleccionVisitante(seleccionVisitante);

                Estadio estadio = new Estadio();
                estadio.setNombre(rs.getString("estadioNombre"));
                seleccionLocal.setEstadio(estadio);

                partido.setSeleccionLocal(seleccionLocal);

                Arbitro arbitro = new Arbitro();
                arbitro.setNombre(rs.getString("arbitroNombre"));
                partido.setArbitro(arbitro);


                partidos.add(partido);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return partidos;
    }

    public void crearPartido(Partido partido) {

        /*
        Inserte su código aquí
        */
        String sql = "INSERT INTO partido (seleccionLocal, seleccionVisitante, arbitro, fecha, numeroJornada) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, partido.getSeleccionLocal().getIdSeleccion());
            pstmt.setInt(2, partido.getSeleccionVisitante().getIdSeleccion());
            pstmt.setInt(3, partido.getArbitro().getIdArbitro());
            pstmt.setString(4, partido.getFecha());
            pstmt.setInt(5, partido.getNumeroJornada());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
