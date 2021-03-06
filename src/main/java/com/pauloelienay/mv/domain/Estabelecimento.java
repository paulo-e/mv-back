package com.pauloelienay.mv.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.pauloelienay.mv.domain.dto.GetEstabelecimentoDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "estabelecimentos")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Estabelecimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nome;

    @Embedded
    private Endereco endereco;

    private String telefone;

    @ManyToMany(mappedBy = "estabelecimentos")
    @JsonBackReference("profissionais")
    private Set<Profissional> profissionais = new HashSet<>();

    public GetEstabelecimentoDto convertToDto() {
        return GetEstabelecimentoDto.convertToDto(this);
    }
}
