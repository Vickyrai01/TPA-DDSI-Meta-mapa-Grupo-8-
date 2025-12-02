package core.api.DTO.criterio;

import core.models.entities.colecciones.criterios.CriterioFechaSuceso;

import java.time.LocalDate;

public class CriterioFechaSucesoDTO extends CriterioDTO{
    private LocalDate desde;
    private LocalDate hasta;

    public CriterioFechaSucesoDTO() {}

    public CriterioFechaSucesoDTO(Integer id, LocalDate fechaInicio, LocalDate fechaFin) {
        super(id);
        this.desde = fechaInicio;
        this.hasta = fechaFin;
    }

    @Override
    public CriterioFechaSuceso toEntity(){
        return new CriterioFechaSuceso(desde, hasta);
    }

    public LocalDate getDesde() { return desde; }
    public LocalDate getHasta() { return hasta; }
    public void setDesde(LocalDate desde) { this.desde = desde; }
    public void setHasta(LocalDate hasta) { this.hasta = hasta; }

}
