package com.example.lab9_base.Controller;

import com.example.lab9_base.Bean.Partido;
import com.example.lab9_base.Bean.Seleccion;
import com.example.lab9_base.Bean.Arbitro;
import com.example.lab9_base.Dao.DaoPartidos;
import com.example.lab9_base.Dao.DaoSelecciones;
import com.example.lab9_base.Dao.DaoArbitros;
import java.util.ArrayList;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "PartidoServlet", urlPatterns = {"/PartidoServlet", ""})
public class PartidoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "guardar" : request.getParameter("action");

        RequestDispatcher view;

        switch (action) {

            case "guardar":
                try {
                    DaoPartidos daoPartidos = new DaoPartidos();

                    String idSeleccionLocalStr = request.getParameter("seleccionLocal");
                    String idSeleccionVisitanteStr = request.getParameter("seleccionVisitante");
                    String idArbitroStr = request.getParameter("arbitro");
                    String fecha = request.getParameter("fecha");
                    String numeroJornadaStr = request.getParameter("jornada");

                    if (idSeleccionLocalStr == null || idSeleccionLocalStr.isEmpty() ||
                            idSeleccionVisitanteStr == null || idSeleccionVisitanteStr.isEmpty() ||
                            idArbitroStr == null || idArbitroStr.isEmpty() ||
                            fecha == null || fecha.isEmpty() ||
                            numeroJornadaStr == null || numeroJornadaStr.isEmpty()) {

                        request.setAttribute("error", "Todos los campos son obligatorios.");
                        view = request.getRequestDispatcher("/partidos/form.jsp");
                        view.forward(request, response);
                        return;
                    }

                    int idSeleccionLocal = Integer.parseInt(idSeleccionLocalStr);
                    int idSeleccionVisitante = Integer.parseInt(idSeleccionVisitanteStr);
                    int idArbitro = Integer.parseInt(idArbitroStr);
                    int numeroJornada = Integer.parseInt(numeroJornadaStr);

                    if (idSeleccionLocal == idSeleccionVisitante) {
                        request.setAttribute("error", "La selección local no puede ser igual a la selección visitante.");
                        view = request.getRequestDispatcher("/partidos/form.jsp");
                        view.forward(request, response);
                        return;
                    }

                    Partido partido = new Partido();
                    Seleccion seleccionLocal = new Seleccion();
                    seleccionLocal.setIdSeleccion(idSeleccionLocal);
                    partido.setSeleccionLocal(seleccionLocal);

                    Seleccion seleccionVisitante = new Seleccion();
                    seleccionVisitante.setIdSeleccion(idSeleccionVisitante);
                    partido.setSeleccionVisitante(seleccionVisitante);

                    Arbitro arbitro = new Arbitro();
                    arbitro.setIdArbitro(idArbitro);
                    partido.setArbitro(arbitro);

                    partido.setFecha(fecha);
                    partido.setNumeroJornada(numeroJornada);


                    daoPartidos.crearPartido(partido);


                    response.sendRedirect("PartidoServlet?action=lista");

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    request.setAttribute("error", "Error en el formato de los datos. Verifique los campos.");
                    view = request.getRequestDispatcher("/partidos/form.jsp");
                    view.forward(request, response);
                }
                break;

        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");

        RequestDispatcher view;
        switch (action) {
            case "lista":
                /*
                Inserte su código aquí
                 */
                DaoPartidos daoPartidos = new DaoPartidos();
                ArrayList<Partido> partidos = daoPartidos.listaDePartidos();
                request.setAttribute("partidos", partidos);
                view = request.getRequestDispatcher("index.jsp");
                view.forward(request, response);
                break;
            case "crear":
                /*
                Inserte su código aquí
                 */
                DaoSelecciones daoSelecciones = new DaoSelecciones();
                DaoArbitros daoArbitros = new DaoArbitros();
                ArrayList<Seleccion> selecciones = daoSelecciones.listarSelecciones();
                ArrayList<Arbitro> arbitros = daoArbitros.listarArbitros();

                request.setAttribute("selecciones", selecciones);
                request.setAttribute("arbitros", arbitros);

                view = request.getRequestDispatcher("/partidos/form.jsp");
                view.forward(request, response);

                break;

        }

    }
}
