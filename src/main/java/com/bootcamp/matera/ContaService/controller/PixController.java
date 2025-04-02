package com.bootcamp.matera.ContaService.controller;


import com.bootcamp.matera.ContaService.dto.PixRequestDTO;
import com.bootcamp.matera.ContaService.dto.PixResponseDTO;
import com.bootcamp.matera.ContaService.service.PixService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/pix")
@RestController
@RequiredArgsConstructor
public class PixController {
    private final PixService pixService;

    @PostMapping
    public ResponseEntity<PixResponseDTO> pix(@RequestBody @Valid PixRequestDTO pixRequestDTO) {
        PixResponseDTO pixResponseDTO = pixService.realizarPix(pixRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(pixResponseDTO);
    }
}
