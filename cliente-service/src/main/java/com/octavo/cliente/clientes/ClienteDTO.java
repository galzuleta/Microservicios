package com.octavo.cliente.clientes;

import jakarta.validation.constraints.*;

public class ClienteDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 100)
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no es valido")
    private String email;

    @Size(max = 20)
    private String telefono;

    @Size(max = 200)
    private String direccion;

    private Boolean activo;

    // ── Constructor vacío ─────────────────────────────────────────────────
    public ClienteDTO() {}

    // ── Constructor completo ──────────────────────────────────────────────
    public ClienteDTO(Long id, String nombre, String apellido, String email,
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
