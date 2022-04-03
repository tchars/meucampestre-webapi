package br.com.meucampestre.webapi.dto.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaginacaoBase<T> {

    private List<T> resultados;

    private int numeroDaPagina;

    private int itensPorPagina;

    private long totalDeElementos;

    private int totalDePaginas;

    private boolean estaNaUltima;
}
