package com.servicio.calidad.app.dgaeaapp;

/**
 * Entidad "peticion"
 */
public class pet {
    private Integer id;
    private String institucion;
    private String tipoSolicitud;
    private String cuentaCheque;
    private String descripcion;
    private Integer monto;

    public pet(Integer id, String institucion,
               String tipoSolicitud, String cuentaCheque, String descripcion, Integer monto) {
        this.id = id;
        this.institucion = institucion;
        this.tipoSolicitud = tipoSolicitud;
        this.cuentaCheque = cuentaCheque;
        this.descripcion = descripcion;
        this.monto = monto;
    }

    public Integer getId() {
        return id;
    }

    public String getInstitucion() {
        return institucion;
    }

    public String getTipoSolicitud() {
        return tipoSolicitud;
    }

    public String getCuentaCheque() {
        return cuentaCheque;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Integer getMonto() {
        return monto;
    }
}