package ar.edu.utn.dds.k3003.presentation;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.HeladeraDTO;
import ar.edu.utn.dds.k3003.facades.dtos.TemperaturaDTO;
import ar.edu.utn.dds.k3003.presentation.auxiliar.ErrorResponse;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MockerController {

    private Fachada fachada;

    public MockerController(Fachada fachada){
        this.fachada = fachada;
    }

    public void mockHeladeras(Context ctx){
        try{

            List<HeladeraDTO> heladerasTest = new ArrayList<>();

            // Cargar heladeras
            heladerasTest.add(
                this.fachada.agregar(
                       new HeladeraDTO("prueba_0")
                )
            );
            heladerasTest.add(
                this.fachada.agregar(
                        new HeladeraDTO("prueba_1")
                )
            );
            heladerasTest.add(
                this.fachada.agregar(
                        new HeladeraDTO("prueba_2")
                )
            );

            ctx.json(heladerasTest);
            ctx.status(HttpStatus.OK);
        }catch(IllegalArgumentException e){
            ctx.status(HttpStatus.BAD_REQUEST);
            ctx.json(new ErrorResponse(1, e.getMessage()));
        }catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR);
            ctx.json(new ErrorResponse(99, "Ups, hubo un error en el endpoint mockHeladeras: "+e));
        }
    }

    public void mockTemperaturas(Context ctx){
        try{
            Integer id_prueba_0 = this.fachada.buscarXNombre("prueba_0").getId();
            Integer id_prueba_1 = this.fachada.buscarXNombre("prueba_1").getId();
            Integer id_prueba_2 = this.fachada.buscarXNombre("prueba_2").getId();

            // a la heladera prueba_0 le asigno 3 temperaturas
            this.fachada.temperatura(new TemperaturaDTO(0,id_prueba_0, LocalDateTime.now()));
            this.fachada.temperatura(new TemperaturaDTO(5,id_prueba_0, LocalDateTime.now()));
            this.fachada.temperatura(new TemperaturaDTO(10,id_prueba_0, LocalDateTime.now()));

            // a la heladera prueba_1 le asigno 1 temperatura
            this.fachada.temperatura(new TemperaturaDTO(-5,id_prueba_1, LocalDateTime.now()));

            // a la heladera prueba_2 le asigno 2 temperaturas
            this.fachada.temperatura(new TemperaturaDTO(15,id_prueba_2, LocalDateTime.now()));
            this.fachada.temperatura(new TemperaturaDTO(-40,id_prueba_2, LocalDateTime.now()));

            ctx.status(HttpStatus.CREATED);
        }catch(IllegalArgumentException e){
            ctx.status(HttpStatus.BAD_REQUEST);
            ctx.json(new ErrorResponse(1, e.getMessage()));
        }catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR);
            ctx.json(new ErrorResponse(99, "Ups, hubo un error en el endpoint mockTemperaturas: "+e));
        }
    }


}
