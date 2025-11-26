package com.tec.usuarioac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tec.usuarioac.entity.Proveedor;


@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long>{

}
