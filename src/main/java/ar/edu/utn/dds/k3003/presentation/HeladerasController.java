package ar.edu.utn.dds.k3003.presentation;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.HeladeraDTO;
import ar.edu.utn.dds.k3003.presentation.auxiliar.ErrorResponse;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.NoSuchElementException;

public class HeladerasController {

    private Fachada fachada;

    public HeladerasController(Fachada fachada){
        this.fachada = fachada;
    }

    public void agregar(Context ctx) {
        try {
            ctx.json( // 3° Retorno el json con el DTO
                    this.fachada.agregar( // 2° Debería poder agregar esa heladeraDTO a la fachada
                            ctx.bodyAsClass(HeladeraDTO.class) // 1° Parseo el body del ctx a HeladeraDTO
                    )
            );
            ctx.status(HttpStatus.CREATED);
        }catch(IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST);
            ctx.json(new ErrorResponse(1, e.getMessage()));
        }catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR);
            ctx.json(new ErrorResponse(99, "Ups, hubo un error en el endpoint agregar: "+e));
        }
    }

    public void buscarXId(Context ctx) {
        try {
            ctx.json(
                    this.fachada.buscarXId(
                            ctx.pathParamAsClass("heladeraId", Integer.class).get()
                    )
            );
            ctx.status(HttpStatus.OK);
        }catch(NoSuchElementException e) {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.json(new ErrorResponse(0, e.getMessage()));
        }catch(IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST);
            ctx.json(new ErrorResponse(1, e.getMessage()));
        }catch(io.javalin.validation.ValidationException e){
            ctx.status(HttpStatus.BAD_REQUEST);
            ctx.json(new ErrorResponse(2, "Se envio un valor no valido como Id"));
        }catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR);
            ctx.json(new ErrorResponse(99, "Ups, hubo un error en el endpoint buscarXId: "+e));
        }
    }

}
