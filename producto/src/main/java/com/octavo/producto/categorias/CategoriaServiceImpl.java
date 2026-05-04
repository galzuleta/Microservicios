package com.octavo.producto.categorias;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

@Transactional
@AllArgsConstructor
public class CategoriaServiceImpl implements CategoriaService{
    private final CategoriaRepository categoriaRepository;

    @Override
    public List<Categoria>  findAll() {
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria findById(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
    }


    @Override
    public Categoria save(Categoria categoria) {
        System.out.println("Guardando: " + categoria.getNombre());
        return categoriaRepository.save(categoria);
    }
}