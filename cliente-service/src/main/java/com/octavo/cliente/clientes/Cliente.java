package com.octavo.cliente.clientes;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100)
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 100)
    @Column(nullable = false)
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no es valido")
    @Column(nullable = false, unique = true)
    private String email;

    @Size(max = 20)
    private String telefono;

    @Size(max = 200)
    private String direccion;

    @Column(nullable = false)
    private Boolean activo = true;

    // ── Constructor vacío (requerido por JPA) ─────────────────────────────
    public Cliente() {}

    // ── Constructor completo ──────────────────────────────────────────────
    public Cliente(Long id, String nombre, String apellido, String email,
                   String telefono, String direccion, Boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.activo = activo;
    }

    // ── Getters ───────────────────────────────────────────────────────────
    public Long getId()          { return id; }
    public String getNombre()    { return nombre; }
    public String getApellido()  { return apellido; }
    public String getEmail()     { return email; }
    public String getTelefono()  { return telefono; }
    public String getDireccion() { return direccion; }
    public Boolean getActivo()   { return activo; }

    // ── Setters ───────────────────────────────────────────────────────────
    public void setId(Long id)               { this.id = id; }
    public void setNombre(String nombre)     { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setEmail(String email)       { this.email = email; }
    public void setTelefono(String tel)      { this.telefono = tel; }
    public void setDireccion(String dir)     { this.direccion = dir; }
    public void setActivo(Boolean activo)    { this.activo = activo; }
}
