package ar.edu.utn.dds.k3003.presentation;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.presentation.auxiliar.ErrorResponse;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class PurgarController {

    private Fachada fachada;

    public PurgarController(Fachada fachada){
        this.fachada = fachada;
    }

    public void purgar(Context ctx){
        try{
            this.fachada.purgarTodo();
            ctx.status(HttpStatus.OK);
        }catch(IllegalArgumentException e){
            ctx.status(HttpStatus.BAD_REQUEST);
            ctx.json(new ErrorResponse(1, e.getMessage()));
        }catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR);
            ctx.json(new ErrorResponse(99, "Ups, hubo un error en el endpoint mockHeladeras: "+e));
        }

    }
}