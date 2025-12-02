package core.api.DTO.criterio;

import core.models.entities.colecciones.criterios.CriterioFechaModificacion;

import java.time.LocalDate;

public class CriterioFechaModificacionDTO extends CriterioDTO{
    private LocalDate desde;
    private LocalDate hasta;

    public CriterioFechaModificacionDTO() {}

    public CriterioFechaModificacionDTO(Integer id, LocalDate fechaInicio, LocalDate fechaFin) {
        super(id);
        this.desde = fechaInicio;
        this.hasta = fechaFin;
    }

    @Override
    public CriterioFechaModificacion toEntity(){
        return new CriterioFechaModificacion(desde, hasta);
    }

    public LocalDate getDesde() { return desde; }
    public LocalDate getHasta() { return hasta; }
    public void setDesde(LocalDate desde) { this.desde = desde; }
    public void setHasta(LocalDate hasta) { this.hasta = hasta; }
}
