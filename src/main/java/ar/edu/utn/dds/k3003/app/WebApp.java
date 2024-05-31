package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.clients.ViandasProxy;
import ar.edu.utn.dds.k3003.facades.dtos.Constants;
import ar.edu.utn.dds.k3003.presentation.HeladerasController;
import ar.edu.utn.dds.k3003.presentation.TemperaturasController;
import ar.edu.utn.dds.k3003.presentation.ViandasController;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;


public class WebApp {
    public static void main(String[] args) {

        var env = System.getenv();
        Fachada fachada = new Fachada();

        var objectMapper = createObjectMapper();
        fachada.setViandasProxy(new ViandasProxy(objectMapper));

        var port = Integer.parseInt(env.getOrDefault("PORT", "8080"));

        var app = Javalin.create(config -> {
            config.jsonMapper(new JavalinJackson().updateMapper(WebApp::configureObjectMapper));
        }).start(port);

        var heladerasController = new HeladerasController(fachada);
        var viandasController = new ViandasController(fachada);
        var temperaturasController = new TemperaturasController(fachada);

        // HeladerasController
        app.post("/heladeras", heladerasController::agregar);
        app.get("/heladeras/{heladeraId}", heladerasController::buscarXId);

        // ViandasController
        app.post("/depositos", viandasController::depositar);
        app.post("/retiros", viandasController::retirar);

        // TemperaturasController

        app.post("/temperaturas", temperaturasController::registrarTemperatura);
        app.get("/heladeras/{heladeraId}/temperaturas", temperaturasController::obtenerTemperaturas);

        app.get("/", ctx -> ctx.result("Hola, soy una API y no un easter egg."));
    }

    public static ObjectMapper createObjectMapper() {
        var objectMapper = new ObjectMapper();
        configureObjectMapper(objectMapper);
        return objectMapper;
    }

    public static void configureObjectMapper(ObjectMapper objectMapper) {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        var sdf = new SimpleDateFormat(Constants.DEFAULT_SERIALIZATION_FORMAT, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        objectMapper.setDateFormat(sdf);
    }
}