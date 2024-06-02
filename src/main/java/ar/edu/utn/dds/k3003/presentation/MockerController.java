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
                       new HeladeraDTO(9997, "prueba_0", 0)
                )
            );
            heladerasTest.add(
                this.fachada.agregar(
                        new HeladeraDTO(9998,"prueba_1", 1)
                )
            );
            heladerasTest.add(
                this.fachada.agregar(
                        new HeladeraDTO(9999, "prueba_2", 5)
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
            this.fachada.temperatura(
                    new TemperaturaDTO(0, 9997, LocalDateTime.now())
            );
            this.fachada.temperatura(
                    new TemperaturaDTO(5, 9997, LocalDateTime.now())
            );
            this.fachada.temperatura(
                    new TemperaturaDTO(-5, 9997, LocalDateTime.now())
            );
            this.fachada.temperatura(
                    new TemperaturaDTO(100, 9998, LocalDateTime.now())
            );
            this.fachada.temperatura(
                    new TemperaturaDTO(10, 9999, LocalDateTime.now())
            );
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
