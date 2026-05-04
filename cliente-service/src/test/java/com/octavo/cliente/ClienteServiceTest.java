package com.octavo.cliente;

import com.octavo.cliente.clientes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente clienteEjemplo;
    private ClienteDTO dtoEjemplo;

    @BeforeEach
    void setUp() {
        clienteEjemplo = new Cliente(1L, "Juan", "Perez",
                "juan@email.com", "0991234567", "Quito", true);
        dtoEjemplo = new ClienteDTO(1L, "Juan", "Perez",
                "juan@email.com", "0991234567", "Quito", true);
    }

    @Test
    void listarTodos_retornaPaginaDeClientes() {
        PageRequest pageable = PageRequest.of(0, 10);
        when(clienteRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(List.of(clienteEjemplo)));

        Page<ClienteDTO> resultado = clienteService.listarTodos(pageable);

        assertThat(resultado.getTotalElements()).isEqualTo(1);
        assertThat(resultado.getContent().get(0).getEmail()).isEqualTo("juan@email.com");
    }

    @Test
    void buscarPorId_existente_retornaDTO() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteEjemplo));

        Optional<ClienteDTO> resultado = clienteService.buscarPorId(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombre()).isEqualTo("Juan");
    }

    @Test
    void buscarPorId_inexistente_retornaVacio() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThat(clienteService.buscarPorId(99L)).isEmpty();
    }

    @Test
    void crear_emailNuevo_guardaYRetornaDTO() {
        when(clienteRepository.existsByEmail(anyString())).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteEjemplo);

        ClienteDTO resultado = clienteService.crear(dtoEjemplo);

        assertThat(resultado.getEmail()).isEqualTo("juan@email.com");
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void crear_emailDuplicado_lanzaConflict() {
        when(clienteRepository.existsByEmail(anyString())).thenReturn(true);

        assertThatThrownBy(() -> clienteService.crear(dtoEjemplo))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("juan@email.com");
    }

    @Test
    void actualizar_existente_actualizaCliente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteEjemplo));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteEjemplo);

        ClienteDTO resultado = clienteService.actualizar(1L, dtoEjemplo);

        assertThat(resultado).isNotNull();
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void actualizar_inexistente_lanzaNotFound() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clienteService.actualizar(99L, dtoEjemplo))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void actualizarParcial_soloTelefono_actualizaSoloCampoEnviado() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteEjemplo));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteEjemplo);

        ClienteDTO patch = new ClienteDTO();
        patch.setTelefono("0999999999");

        assertThatCode(() -> clienteService.actualizarParcial(1L, patch))
                .doesNotThrowAnyException();
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void eliminar_existente_eliminaCliente() {
        when(clienteRepository.existsById(1L)).thenReturn(true);
        doNothing().when(clienteRepository).deleteById(1L);

        assertThatCode(() -> clienteService.eliminar(1L)).doesNotThrowAnyException();
        verify(clienteRepository).deleteById(1L);
    }

    @Test
    void eliminar_inexistente_lanzaNotFound() {
        when(clienteRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> clienteService.eliminar(99L))
                .isInstanceOf(ResponseStatusException.class);
    }
}
