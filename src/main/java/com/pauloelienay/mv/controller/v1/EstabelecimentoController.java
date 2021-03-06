package com.pauloelienay.mv.controller.v1;

import com.pauloelienay.mv.domain.Estabelecimento;
import com.pauloelienay.mv.domain.dto.GetEstabelecimentoDto;
import com.pauloelienay.mv.service.EstabelecimentoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/estabelecimentos")
public class EstabelecimentoController {
    private final EstabelecimentoService service;

    @GetMapping
    public ResponseEntity<Page<EntityModel<GetEstabelecimentoDto>>> getPageableEstabelecimentos
            (@PageableDefault(size = 10, sort = {"id"}) Pageable pageable,
             @RequestParam(required = false) String nome) {
        return new ResponseEntity<>(service.getPageableEstabelecimentos(pageable, nome)
                .map(this::addLinks), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<GetEstabelecimentoDto>> getEstabelecimentoById(@PathVariable long id) {
        return new ResponseEntity<>(addLinksById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Estabelecimento> createNewEstabelecimento(@RequestBody Estabelecimento estabelecimento) {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(linkTo(EstabelecimentoController.class)
                .slash(service.create(estabelecimento).getId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEstabelecimentoById(@PathVariable long id) {
        service.deleteEstabelecimentoById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Estabelecimento> updateEstabelecimentoById
            (@PathVariable long id, @RequestBody Estabelecimento estabelecimento) {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(linkTo(EstabelecimentoController.class)
                .slash(service.update(id, estabelecimento).getId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    EntityModel<GetEstabelecimentoDto> addLinks(Estabelecimento estabelecimento) {
        EntityModel<GetEstabelecimentoDto> model = EntityModel.of(estabelecimento.convertToDto());
        model.add(linkTo(methodOn(this.getClass()).getPageableEstabelecimentos(null, null))
                .withSelfRel().withType("GET"));
        model.add(linkTo(methodOn(this.getClass()).deleteEstabelecimentoById(estabelecimento.getId()))
                .withRel("delete").withType("DELETE"));
        model.add(linkTo(methodOn(this.getClass()).updateEstabelecimentoById(estabelecimento.getId(), estabelecimento))
                .withRel("update").withType("PUT"));
        model.add(linkTo(methodOn(this.getClass()).getPageableEstabelecimentos(null, null))
                .withRel("estabelecimentos").withType("GET"));
        return model;
    }

    EntityModel<GetEstabelecimentoDto> addLinksById(long id) {
        return addLinks(service.getEstabelecimentoById(id));
    }
}
