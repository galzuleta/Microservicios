package com.octavo.producto.categorias;

import java.util.List;

public interface CategoriaService {
    public List<Categoria> findAll();
    Categoria findById(Long id);
    Categoria save(Categoria categoria);

}