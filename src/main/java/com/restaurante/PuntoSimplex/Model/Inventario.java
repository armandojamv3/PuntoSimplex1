    package com.restaurante.PuntoSimplex.Model;

    import jakarta.persistence.*;
    import jakarta.validation.constraints.NotBlank;
    import jakarta.validation.constraints.Size;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import java.time.LocalDateTime;

    @Entity
    @Data
    @NoArgsConstructor
    @Table(name = "INVENTARIO")
    public class Inventario {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;



        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "producto_id", nullable = false, unique = true)
        private Producto producto;

        private Integer stockActual;
        private Integer stockMinimo;

        @Column(nullable = false)
        private LocalDateTime ultimaActualizacion;
    }
