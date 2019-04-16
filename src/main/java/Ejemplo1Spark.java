import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Date;

import static spark.Spark.*;

public class Ejemplo1Spark {

    public static void main(String[] args) {

        //port(8080);
        final UsuarioService usuarioService = new UsuarioServiceMapImpl();
        final ProyectoService proyectoService = new ProyectoServiceMapImpl();
        final IncidenteService incidenteService = new IncidenteServiceMapImpl();

        precarga(usuarioService, proyectoService, incidenteService);

        GsonBuilder gsonBuilder = new GsonBuilder();

        JsonDeserializer<Proyecto> deserializer = new JsonDeserializer<Proyecto>() {
            @Override
            public Proyecto deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                JsonObject jsonObject = json.getAsJsonObject();

                Proyecto proyecto = new Proyecto(
                        jsonObject.get("id").getAsInt(),
                        jsonObject.get("titulo").getAsString(),
                        usuarioService.getUsuario(jsonObject.get("idUsuario").getAsInt())
                );

                return proyecto;
            }
        };

        gsonBuilder.registerTypeAdapter(Proyecto.class, deserializer);


        JsonDeserializer<Incidente> deserializerIncidente = new JsonDeserializer<Incidente>() {
            @Override
            public Incidente deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                JsonObject jsonObject = json.getAsJsonObject();

                Incidente incidente = new Incidente(
                        jsonObject.get("id").getAsInt(),
                        Clasificacion.valueOf(jsonObject.get("clasificacion").getAsString()),
                        jsonObject.get("descripcion").getAsString(),
                        usuarioService.getUsuario(jsonObject.get("idUsrReportador").getAsInt()),
                        usuarioService.getUsuario(jsonObject.get("idUsrResponsable").getAsInt()),
                        Estado.valueOf(jsonObject.get("estado").getAsString()),
                        proyectoService.getProyecto(jsonObject.get("idProyecto").getAsInt())
                );

                return incidente;
            }
        };

        gsonBuilder.registerTypeAdapter(Incidente.class, deserializerIncidente);

        Gson customGson = gsonBuilder.create();


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
            Proyecto proyecto = customGson.fromJson(request.body(), Proyecto.class);

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
        post("/incidente", (request, response) -> {
            response.type("application/json");
            Incidente incidente = customGson.fromJson(request.body(), Incidente.class);

            incidente.setFechaDeCreacion(new Date());
            incidenteService.createIncidente(incidente);

            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS));
        });

        // Agregar descripcion (pasar el id de usuario y el id del proyecto para asociarlos al incidente)
        post("/incidente/:id", (request, response) -> {
            response.type("application/json");
            incidenteService.getIncidente(Integer.parseInt(request.params(":id"))).setDescripcion(request.attribute("descripcion"));

            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS));
        });

        // Cambiar estado de incidente a RESUELTO
        put("/incidente/:id", (request, response) -> {
            response.type("application/json");
            incidenteService.changeEstadoIncidente(Integer.parseInt(request.params(":id")));

            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS));
        });


        ///////////////////////////////////////////////////////

        // Mostrar todos los proyectos de un usuario
        get("/usuario/:id/proyectos", (request, response) -> {
            //...
            response.type("application/json");
            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(proyectoService.getProyectosPropietario(Integer.parseInt(request.params(":id"))))));
        });

        // Incidentes asignados por un usuario
        get("/usuario/:id/incidentes/asignados", (request, response) -> {
            response.type("application/json");

            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(incidenteService.getIncidentesAsignadosUsuario(Integer.parseInt(request.params(":id"))))));
        });

        // Incidentes creados por un usuario
        get("/usuario/:id/incidentes/creados", (request, response) -> {
            response.type("application/json");

            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(incidenteService.getIncidentesCreadosUsuario(Integer.parseInt(request.params(":id"))))));
        });

        // Incidentes asociados a un proyecto
        get("/proyecto/:id/incidentes", (request, response) -> {
            response.type("application/json");

            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(incidenteService.getIncidentesProyecto(Integer.parseInt(request.params(":id"))))));
        });

        // Incidentes abiertos
        get("/incidente/abiertos", (request, response) -> {
            response.type("application/json");

            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(incidenteService.getIncidentesAbiertos())));
        });

        // Incidentes resueltos
        get("/incidente/resueltos", (request, response) -> {
            response.type("application/json");

            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(incidenteService.getIncidentesCerrados())));
        });
    }


    private static void precarga(UsuarioService usuarioService, ProyectoService proyectoService, IncidenteService incidenteService) {
        usuarioService.addUsuario(new Usuario(1, "Abece", "Apellido1"));
        usuarioService.addUsuario(new Usuario(2, "Deeefe", "Apellido2"));
        usuarioService.addUsuario(new Usuario(3, "Gehachei", "Apellido3"));

        proyectoService.createProyect(new Proyecto(1, "Proyecto 1", usuarioService.getUsuario(1)));
        proyectoService.createProyect(new Proyecto(2, "Proyecto 2", usuarioService.getUsuario(3)));

        incidenteService.createIncidente(new Incidente(1, Clasificacion.NORMAL, "Incidente 1",
                usuarioService.getUsuario(3), usuarioService.getUsuario(1), Estado.ASIGNADO,
                proyectoService.getProyecto(1)));

        incidenteService.createIncidente(new Incidente(2, Clasificacion.CRITICO, "Incidente 2",
                usuarioService.getUsuario(3), usuarioService.getUsuario(2), Estado.RESUELTO,
                proyectoService.getProyecto(2)));
    }

}