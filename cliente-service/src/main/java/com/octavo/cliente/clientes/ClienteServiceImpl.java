package com.octavo.cliente.clientes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // ── Listar todos con paginacion ───────────────────────────────────────

    @Override
    public Page<ClienteDTO> listarTodos(Pageable pageable) {
        return clienteRepository.findAll(pageable).map(this::toDTO);
    }

    // ── Buscar por ID ─────────────────────────────────────────────────────

    @Override
    public Optional<ClienteDTO> buscarPorId(Long id) {
        return clienteRepository.findById(id).map(this::toDTO);
    }

    // ── Crear ─────────────────────────────────────────────────────────────

    @Override
    public ClienteDTO crear(ClienteDTO dto) {
        if (clienteRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Ya existe un cliente con el email: " + dto.getEmail()
            );
        }
        Cliente cliente = toEntity(dto);
        if (cliente.getActivo() == null) {
            cliente.setActivo(true);
        }
        return toDTO(clienteRepository.save(cliente));
    }

    // ── Actualizar completo (PUT) ─────────────────────────────────────────

    @Override
    public ClienteDTO actualizar(Long id, ClienteDTO dto) {
        Cliente existente = clienteRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Cliente no encontrado con id: " + id
            ));

        // Si el email cambia, verificar que no este en uso
        if (!existente.getEmail().equals(dto.getEmail())
                && clienteRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Ya existe un cliente con el email: " + dto.getEmail()
            );
        }

        existente.setNombre(dto.getNombre());
        existente.setApellido(dto.getApellido());
        existente.setEmail(dto.getEmail());
        existente.setTelefono(dto.getTelefono());
        existente.setDireccion(dto.getDireccion());
        if (dto.getActivo() != null) {
            existente.setActivo(dto.getActivo());
        }

        return toDTO(clienteRepository.save(existente));
    }

    // ── Actualizar parcial (PATCH) ────────────────────────────────────────

    @Override
    public ClienteDTO actualizarParcial(Long id, ClienteDTO dto) {
        Cliente existente = clienteRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Cliente no encontrado con id: " + id
            ));

        if (dto.getNombre() != null)    existente.setNombre(dto.getNombre());
        if (dto.getApellido() != null)  existente.setApellido(dto.getApellido());
        if (dto.getTelefono() != null)  existente.setTelefono(dto.getTelefono());
        if (dto.getDireccion() != null) existente.setDireccion(dto.getDireccion());
        if (dto.getActivo() != null)    existente.setActivo(dto.getActivo());

        if (dto.getEmail() != null && !existente.getEmail().equals(dto.getEmail())) {
            if (clienteRepository.existsByEmail(dto.getEmail())) {
                throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un cliente con el email: " + dto.getEmail()
                );
            }
            existente.setEmail(dto.getEmail());
        }

        return toDTO(clienteRepository.save(existente));
    }

    // ── Eliminar ──────────────────────────────────────────────────────────

    @Override
    public void eliminar(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Cliente no encontrado con id: " + id
            );
        }
        clienteRepository.deleteById(id);
    }

    // ── Mappers privados ──────────────────────────────────────────────────

    private ClienteDTO toDTO(Cliente c) {
        return new ClienteDTO(
            c.getId(), c.getNombre(), c.getApellido(),
            c.getEmail(), c.getTelefono(), c.getDireccion(), c.getActivo()
        );
    }

    private Cliente toEntity(ClienteDTO d) {
        return new Cliente(
            d.getId(), d.getNombre(), d.getApellido(),
            d.getEmail(), d.getTelefono(), d.getDireccion(), d.getActivo()
        );
    }
}
