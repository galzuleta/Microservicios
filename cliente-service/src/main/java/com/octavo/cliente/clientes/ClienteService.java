package com.octavo.cliente.clientes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ClienteService {

    Page<ClienteDTO> listarTodos(Pageable pageable);

    Optional<ClienteDTO> buscarPorId(Long id);

    ClienteDTO crear(ClienteDTO dto);

    ClienteDTO actualizar(Long id, ClienteDTO dto);

    ClienteDTO actualizarParcial(Long id, ClienteDTO dto);

    void eliminar(Long id);
}
