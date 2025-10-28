package com.restaurante.PuntoSimplex.Model;

import jakarta.persistence.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "ITEMS_PEDIDO")
@Data

//Para que tu sistema funcione, el Pedido debe saber qué
// Mesa ocupa y qué Productos tiene. Para gestionar los productos
// dentro de un pedido, necesitamos una entidad intermedia: ItemPedido


public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    private int cantidad;
    private double precioUnitario;
}
