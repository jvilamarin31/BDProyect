package com.apirest.backend.Model;

import java.util.ArrayList;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import com.apirest.backend.Model.ENUM.EstadoAdministradores;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("Administrador")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdministradorModel {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private ObjectId idAdministrador;
    private EstadoAdministradores estado;
    private ArrayList<PeriodosAdministradores> periodos = new ArrayList<>();
}
