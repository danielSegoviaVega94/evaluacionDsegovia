package com.evaluacion.dSegovia.model;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "phones")
public class Phone {
    //Aca se define el modelo de la entidad Phone con el number como llave primaria.
    @Id
    private String number;
    private String citycode;
    private String contrycode;

    @ManyToOne
    @JoinColumn(name = "id")
    private User user;
}
