package ar.edu.utn.dds.k3003.presentation;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.RetiroDTO;
import ar.edu.utn.dds.k3003.presentation.auxiliar.ContextMappers.ViandaDTOMapper;
import ar.edu.utn.dds.k3003.presentation.auxiliar.ErrorResponse;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.NoSuchElementException;

public class ViandasController {

    private Fachada fachada;
    private ViandaDTOMapper viandaDTOMapper;

    public ViandasController(Fachada fachada){
        this.fachada = fachada;
        this.viandaDTOMapper = new ViandaDTOMapper();
    }

    public void depositar(Context ctx) {
        try {
            var viandaDTO = viandaDTOMapper.mapper(ctx); // Eso si que no lo cambio.
            this.fachada.depositar(viandaDTO.getHeladeraId(), viandaDTO.getCodigoQR());
            ctx.status(HttpStatus.OK);
            ctx.result("Vianda depositada correctamente");
        }catch(NoSuchElementException | IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST);
            ctx.result("Error de solicitud"); // No lo puedo cambiar por definicion de la API
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR);
            ctx.json(new ErrorResponse(99, "Ups, hubo un error en el endpoint depositar: "+e));
        }
    }

    public void retirar(Context ctx) {
        try {
            this.fachada.retirar(
                    ctx.bodyAsClass(RetiroDTO.class)
            );
            ctx.status(HttpStatus.OK);
            ctx.result("Vianda retirada correctamente");
        }catch(NoSuchElementException | IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST);
            ctx.result("Error de solicitud"); // IDEM caso anterior...
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR);
            ctx.json(new ErrorResponse(99, "Ups, hubo un error en el endpoint retirar: "+e));
        }
    }
}
