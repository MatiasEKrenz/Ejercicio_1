import com.google.gson.Gson;

import java.util.Date;

import static spark.Spark.*;

public class Ejemplo1Spark {

    public static void main(String[] args) {

        //port(8080);
        final UsuarioService usuarioService = new UsuarioServiceMapImpl();
        final ProyectoService proyectoService = new ProyectoServiceMapImpl();
        final IncidenteService incidenteService = new IncidenteServiceMapImpl();

        // Crear un usuario
        post("/usuario", (request, response) -> {
            response.type("application/json");
            Usuario usuario = new Gson().fromJson(request.body(), Usuario.class);
            usuarioService.addUsuario(usuario); // podria devolver algo el metodo para saber si fue exitoso o no
            // tengo que comprobar si se persistio el elemento o no. Debo hacer comprobaciones

            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS)); // Success suponiendo que todoo salio bien, deberiamos
            // hacer control de errores
        });

        // Mostrar todos los usuarios creados
        get("/usuario", (request, response) -> {
            //...
            response.type("application/json");
            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(usuarioService.getUsuarios())));
        });

        // Mostrar un usuario por id
        get("/usuario/:id", (request, response) -> {
            //...
            response.type("application/json");
            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(usuarioService.getUsuario(Integer.parseInt(request.params(":id"))))));
        });

        // Modificar un usuario existente
        put("/usuario", (request, response) -> {
            //...
            response.type("application/json");

            Usuario usuario = new Gson().fromJson(request.body(), Usuario.class);

            Usuario usuarioEditado = usuarioService.editUsuario(usuario);

            if(usuarioEditado != null) {
                return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(usuarioEditado)));
            }else {
                return new Gson().toJson(new StandardResponse(StatusResponse.ERROR,"Error al editar el usuario"));
            }
        });

        // Eliminar un usuario
        delete("/usuario/:id", (request, response) -> {
            //...
            response.type("application/json");

            boolean variable = proyectoService.getProyectosPropietario(Integer.parseInt(request.params(":id"))).isEmpty();
            variable = variable | incidenteService.getIncidentesAsignadosUsuario(Integer.parseInt(request.params(":id"))).isEmpty();
            variable = variable | incidenteService.getIncidentesCreadosUsuario(Integer.parseInt(request.params(":id"))).isEmpty();

            if (!variable){
                usuarioService.deleteUsuario(Integer.parseInt(request.params(":id")));
                return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, "Usuario borrado"));
            } else {
                return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, "Error al borrar Usuario, el mismo es propietario de un proyecto, responsable o reportador."));
            }
        });


        /////////////////////////////////////////////////////////////////////

        // Crear un proyecto (pasar el id de usuario para asociarlo al proyecto)
        post("/proyecto", (request, response) -> {
            response.type("application/json");
            Proyecto proyecto = new Gson().fromJson(request.body(), Proyecto.class);

            proyecto.setPropietario(usuarioService.getUsuario(request.attribute("idUsuario")));

            proyectoService.createProyect(proyecto);

            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS));
        });

        // Mostrar todos los proyectos
        get("/proyecto", (request, response) -> {
            //...
            response.type("application/json");
            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(proyectoService.getProyectos())));
        });

        // Mostrar un proyecto por id
        get("/proyecto/:id", (request, response) -> {
            //...
            response.type("application/json");
            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(proyectoService.getProyecto(Integer.parseInt(request.params(":id"))))));
        });

        // Modificar un proyecto existente
        put("/proyecto", (request, response) -> {
            //...
            response.type("application/json");

            Proyecto proyecto = new Gson().fromJson(request.body(), Proyecto.class);

            Proyecto proyectoEditado = proyectoService.editProyecto(proyecto);

            if(proyectoEditado != null) {
                return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(proyectoEditado)));
            }else {
                return new Gson().toJson(new StandardResponse(StatusResponse.ERROR,"Error al editar el proyecto"));
            }
        });

        // Eliminar un proyecto
        delete("/proyecto/:id", (request, response) -> {
            //...
            response.type("application/json");

            boolean variable = incidenteService.getIncidentesProyecto(Integer.parseInt(request.params(":id"))).isEmpty();

            if (!variable){
                proyectoService.deleteProyecto(Integer.parseInt(request.params(":id")));
                return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, "Proyecto borrado"));
            } else {
                return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, "Error al borrar Proyecto, el mismo posee incidentes reportados."));
            }
        });


        /////////////////////////////////////////////////////////////////////

        // Crear un incidente (pasar el id de usuario y el id del proyecto para asociarlos al incidente)
        post("/proyecto", (request, response) -> {
            response.type("application/json");

            Incidente incidente = new Gson().fromJson(request.body(), Incidente.class);

            incidente.setProyecto(proyectoService.getProyecto(request.attribute("idProyecto")));
            incidente.setReportador(usuarioService.getUsuario(request.attribute("idUsrReportador")));
            incidente.setResponsable(usuarioService.getUsuario(request.attribute("idUsrResponsable")));

            incidente.setFechaDeCreacion(new Date());

            incidenteService.createIncidente(incidente);

            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS));
        });

        /*options("/usuario/:id", (request, response) -> {

            response.type("application/json");

            integranteService.deleteIntegrante(request.params(":id"));

            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, (integranteService.integranteExist(request.params(":id")) ? "El integrante existe" : "El integrante no existe")));
        });*/
    }
}