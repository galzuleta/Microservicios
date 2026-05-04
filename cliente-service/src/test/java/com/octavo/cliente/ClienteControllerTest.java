package com.octavo.cliente;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.octavo.cliente.clientes.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClienteService clienteService;

    private ClienteDTO dto() {
        return new ClienteDTO(1L, "Ana", "Garcia",
                "ana@email.com", "0991112233", "Guayaquil", true);
    }

    @Test
    void GET_listarTodos_retorna200ConPagina() throws Exception {
        Page<ClienteDTO> page = new PageImpl<>(
                List.of(dto()), PageRequest.of(0, 10), 1);
        when(clienteService.listarTodos(any())).thenReturn(page);

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nombre").value("Ana"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void GET_buscarPorId_existente_retorna200() throws Exception {
        when(clienteService.buscarPorId(1L)).thenReturn(Optional.of(dto()));

        mockMvc.perform(get("/api/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("ana@email.com"));
    }

    @Test
    void GET_buscarPorId_inexistente_retorna404() throws Exception {
        when(clienteService.buscarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/clientes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void POST_crear_datosValidos_retorna201() throws Exception {
        ClienteDTO nuevo = dto();
        nuevo.setId(null);
        when(clienteService.crear(any())).thenReturn(dto());

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void POST_crear_emailInvalido_retorna400() throws Exception {
        ClienteDTO invalido = dto();
        invalido.setEmail("no-es-un-email");

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void PUT_actualizar_retorna200() throws Exception {
        when(clienteService.actualizar(eq(1L), any())).thenReturn(dto());

        mockMvc.perform(put("/api/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Ana"));
    }

    @Test
    void PATCH_actualizarParcial_retorna200() throws Exception {
        when(clienteService.actualizarParcial(eq(1L), any())).thenReturn(dto());

        ClienteDTO patch = new ClienteDTO();
        patch.setTelefono("0999999999");

        mockMvc.perform(patch("/api/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isOk());
    }

    @Test
    void DELETE_eliminar_retorna204() throws Exception {
        doNothing().when(clienteService).eliminar(1L);

        mockMvc.perform(delete("/api/clientes/1"))
                .andExpect(status().isNoContent());
    }
}
