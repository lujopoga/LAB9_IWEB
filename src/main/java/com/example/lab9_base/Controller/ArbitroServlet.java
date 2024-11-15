package com.example.lab9_base.Controller;

import com.example.lab9_base.Bean.Arbitro;
import com.example.lab9_base.Dao.DaoArbitros;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "ArbitroServlet", urlPatterns = {"/ArbitroServlet"})
public class ArbitroServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
        RequestDispatcher view;
        ArrayList<String> opciones = new ArrayList<>();
        opciones.add("nombre");
        opciones.add("pais");

        DaoArbitros daoArbitros = new DaoArbitros();

        switch (action) {

            case "buscar":
                /*
                Inserte su código aquí
                */
                String tipo = request.getParameter("tipo");
                String valorBusqueda = request.getParameter("buscar");
                ArrayList<Arbitro> arbitros = new ArrayList<>();
                DaoArbitros daoArbitro = new DaoArbitros();


                if ("nombre".equals(tipo)) {
                    arbitros = daoArbitro.busquedaNombre(valorBusqueda);
                } else if ("pais".equals(tipo)) {
                    arbitros = daoArbitro.busquedaPais(valorBusqueda);
                }


                request.setAttribute("arbitros", arbitros);
                request.setAttribute("opciones", opciones);
                view = request.getRequestDispatcher("/arbitros/list.jsp");
                view.forward(request, response);

                break;

            case "guardar":
                /*
                Inserte su código aquí
                */
                String nombre = request.getParameter("nombre");
                String pais = request.getParameter("pais");

                if (nombre != null && !nombre.isEmpty() && pais != null && !pais.isEmpty()) {
                    Arbitro arbitro = new Arbitro();
                    arbitro.setNombre(nombre);
                    arbitro.setPais(pais);
                    daoArbitros.crearArbitro(arbitro);

                    response.sendRedirect(request.getContextPath() + "/ArbitroServlet?action=lista");
                } else {
                    request.setAttribute("error", "Todos los campos son obligatorios.");
                    view = request.getRequestDispatcher("/arbitros/form.jsp");
                    view.forward(request, response);
                }
                break;

        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
        RequestDispatcher view;
        ArrayList<String> paises = new ArrayList<>();
        paises.add("Peru");
        paises.add("Chile");
        paises.add("Argentina");
        paises.add("Paraguay");
        paises.add("Uruguay");
        paises.add("Colombia");
        ArrayList<String> opciones = new ArrayList<>();
        opciones.add("nombre");
        opciones.add("pais");

        DaoArbitros daoArbitro = new DaoArbitros();

        switch (action) {
            case "lista":
                /*
                Inserte su código aquí
                 */
                ArrayList<Arbitro> arbitros = daoArbitro.listarArbitros();

                request.setAttribute("arbitros", arbitros);
                request.setAttribute("paises", paises);
                request.setAttribute("opciones", opciones);
                view = request.getRequestDispatcher("/arbitros/list.jsp");
                view.forward(request, response);
                break;
            case "crear":
                /*
                Inserte su código aquí
                */
                request.setAttribute("paises", paises);
                view = request.getRequestDispatcher("/arbitros/form.jsp");
                view.forward(request, response);
                break;
            case "borrar":
                /*
                Inserte su código aquí
                */

                int idArbitro = Integer.parseInt(request.getParameter("id"));

                daoArbitro.borrarArbitro(idArbitro);
                response.sendRedirect(request.getContextPath() + "/ArbitroServlet?action=lista");

                break;
        }
    }
}
