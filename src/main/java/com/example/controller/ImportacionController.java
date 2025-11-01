package com.example.controller;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.model.Estudiante;
import com.example.model.Importacion;
import com.example.model.Nota;
import com.example.service.EstudianteService;
import com.example.service.ImportacionService;
import com.example.service.NotaService;
import com.example.utils.ImportacionNotasWrapper;
import com.example.utils.NotaXML;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/importacion")
@RequiredArgsConstructor
public class ImportacionController {

    private final ImportacionService importacionService;
    private final NotaService notaService;
    private final EstudianteService estudianteService;



    @GetMapping
    public String verHistorial(Model model) {
        model.addAttribute("importaciones", importacionService.listar());
        return "importacion/lista";
    }

    @GetMapping("/formulario")
    public String formularioImportar() {
        return "importacion/formulario";
    }

    @PostMapping("/excel")
    public String importarExcel(@RequestParam("archivo") MultipartFile archivo, Model model) {
        if (archivo.isEmpty() || !archivo.getOriginalFilename().endsWith(".xlsx")) {
            model.addAttribute("error", "Debe subir un archivo Excel válido (.xlsx)");
            return "importacion/formulario";
        }

        try (InputStream is = archivo.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet hoja = workbook.getSheetAt(0);

            Importacion importacion = new Importacion();
            importacion.setTipo("Excel");
            importacion.setArchivoNombre(archivo.getOriginalFilename());
            importacion.setFecha(LocalDateTime.now());
            importacion = importacionService.guardar(importacion);

            for (int i = 1; i <= hoja.getLastRowNum(); i++) {
                Row fila = hoja.getRow(i);
                if (fila == null) continue;

                String nombre = fila.getCell(0).getStringCellValue();
                String apellido = fila.getCell(1).getStringCellValue();
                String dni = fila.getCell(2).getStringCellValue();
                LocalDate fechaNacimiento = fila.getCell(3).getLocalDateTimeCellValue().toLocalDate();
                String curso = fila.getCell(4).getStringCellValue();
                double nota1 = fila.getCell(5).getNumericCellValue();
                double nota2 = fila.getCell(6).getNumericCellValue();
                double nota3 = fila.getCell(7).getNumericCellValue();

                Estudiante estudiante = new Estudiante();
                estudiante.setNombre(nombre);
                estudiante.setApellido(apellido);
                estudiante.setDni(dni);
                estudiante.setFechaNacimiento(fechaNacimiento);
                estudiante = estudianteService.guardar(estudiante);

                Nota nota = new Nota();
                nota.setCurso(curso);
                nota.setNota1(nota1);
                nota.setNota2(nota2);
                nota.setNota3(nota3);
                nota.setNotaFinal(notaService.calcularNotaFinal(nota));
                nota.setEstudiante(estudiante);
        


                    notaService.guardar(nota);

            }

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error al procesar el archivo Excel");
            return "importacion/formulario";
        }

        return "redirect:/notas";
    }

    @PostMapping("/xml")
    public String importarXML(@RequestParam("archivo") MultipartFile archivo, Model model) {
        if (archivo.isEmpty() || !archivo.getOriginalFilename().endsWith(".xml")) {
            model.addAttribute("error", "Debe subir un archivo XML válido (.xml)");
            return "importacion/formulario";
        }

        try (InputStream is = archivo.getInputStream()) {

            JAXBContext context = JAXBContext.newInstance(ImportacionNotasWrapper.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            ImportacionNotasWrapper wrapper = (ImportacionNotasWrapper) unmarshaller.unmarshal(is);

            Importacion importacion = new Importacion();
            importacion.setTipo("XML");
            importacion.setArchivoNombre(archivo.getOriginalFilename());
            importacion.setFecha(LocalDateTime.now());
            importacion = importacionService.guardar(importacion);

            for (NotaXML notaXML : wrapper.getNotas()) {
                Estudiante estudiante = new Estudiante();
                estudiante.setNombre(notaXML.getNombre());
                estudiante.setApellido(notaXML.getApellido());
                estudiante.setDni(notaXML.getDni());
                estudiante.setFechaNacimiento(LocalDate.parse(notaXML.getFechaNacimiento()));
                estudiante = estudianteService.guardar(estudiante);

                Nota nota = new Nota();
                nota.setCurso(notaXML.getCurso());
                nota.setNota1(notaXML.getNota1());
                nota.setNota2(notaXML.getNota2());
                nota.setNota3(notaXML.getNota3());
                nota.setNotaFinal(notaService.calcularNotaFinal(nota));
                nota.setEstudiante(estudiante);
        


                    notaService.guardar(nota);

            }

        } catch (Exception e) {
            model.addAttribute("error", "Error al procesar el archivo XML");
            return "importacion/formulario";
        }

        return "redirect:/notas";
    }
}